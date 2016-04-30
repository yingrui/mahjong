package me.yingrui.segment.word2vec

import java.io.FileWriter

import me.yingrui.segment.math.Matrix

import scala.collection.mutable.ListBuffer
import scala.io.Source

class SegmentCorpus(word2VecModel: Array[Array[Double]], vocab: Vocabulary, ngram: Int = 1) {

  val vectorSize = word2VecModel(0).length
  val window = 1
  val labelTransitionProb = Matrix(4, 4)
  val word2vecInputHelper = new Word2VecInputHelper(ngram, vectorSize, word2VecModel)

  def splitCorpus(segmentCorpusFile: String, numberOfPieces: Int): Seq[String] = {
    var i = 0

    val writers = (for (index <- 0 until numberOfPieces) yield {
      val file = segmentCorpusFile + "_" + index + ".txt"
      (index, new FileWriter(file))
    }).toMap

    foreachDocuments(segmentCorpusFile) { document =>
      val id = i % numberOfPieces
      val writer = writers(id)
      val newLine = "\n"
      document.foreach(_ match {
        case (word, label) =>
          val line = s"$word\t${label}${newLine}"
//          print(id + "  " +line)
          writer.write(line)
        case _ =>
      })
      writer.write(newLine)
      i += 1
    }

    writers.map(_._2).foreach(_.close())
    (0 until numberOfPieces).map(index => segmentCorpusFile + "_" + index + ".txt")
  }

  def getLabelTransitionProb(segmentCorpusFile: String): Matrix = {
    foreachDocuments(segmentCorpusFile) { document =>
      (0 until document.length).foreach(position => {
        if (position > 0) {
          val label = getLabelIndex(document(position))
          val lastLabel = getLabelIndex(document(position - 1))
          recordLabelTransition(lastLabel, label)
        }
      })
    }

    buildLabelTransitionProb
  }

  def loadSegmentDataSet(segmentCorpusFile: String): List[Seq[(Int, Matrix, Int)]] = {
    loadDocuments(segmentCorpusFile).map(doc => convertToSegmentDataSet(doc))
  }

  def load(segmentCorpusFile: String): Seq[(Matrix, Matrix)] = {
    loadDocuments(segmentCorpusFile).map(doc => convert(doc)).flatten
  }

  def convert(document: List[(String, String)]): List[(Matrix, Matrix)] = {
    val inputs = document
      .filter(wordLabel => vocab.getIndex(wordLabel._1) > 0)
      .map(wordAndLabel => {
      (vocab.getIndex(wordAndLabel._1), getLabelIndex(wordAndLabel))
    })

    (0 until inputs.length).map(position => {
      if (position > 0) {
        val label = inputs(position)._2
        val lastLabel = inputs(position - 1)._2
        recordLabelTransition(lastLabel, label)
      }
      (word2vecInputHelper.toMatrix(getContextWords(inputs, position)), getOutputMatrix(inputs, position))
    }).toList
  }

  private def buildLabelTransitionProb: Matrix = {
    for (i <- 0 until labelTransitionProb.row) {
      val total = labelTransitionProb.row(i).sum
      for (j <- 0 until labelTransitionProb.col) {
        labelTransitionProb(i, j) /= total
      }
    }
    labelTransitionProb
  }

  def convertToMatrix(input: List[Int]): Matrix = word2vecInputHelper.toMatrix(input)

  def convertToWordIndexes(document: List[(String, String)]): List[(List[Int], Int, Matrix)] = {
    val inputs = getWordIndexesAndLabelIndexes(document)

    (0 until inputs.length).map(position => {
      (getContextWords(inputs, position), inputs(position)._1, getOutputMatrix(inputs, position))
    }).toList
  }

  def getOutputMatrix(inputs: List[(Int, Int)], position: Int): Matrix = {
    getOutputMatrixByLabels(getLabels(inputs, position))
  }

  def getOutputMatrixByLabels(labels: Array[Int]) = word2vecInputHelper.labelToMatrix(labels)

  def getDefaultOutputMatrix(): Matrix = {
    val labels = word2vecInputHelper.defaultLabels(ngram)
    getOutputMatrixByLabels(labels)
  }

  def getWordIndexesAndLabelIndexes(document: List[(String, String)]): List[(Int, Int)] = {
    document
      .filter(wordLabel => vocab.getIndex(wordLabel._1) > 0)
      .map(wordAndLabel => {
      (vocab.getIndex(wordAndLabel._1), getLabelIndex(wordAndLabel))
    })
  }

  def loadDocuments(segmentCorpusFile: String): List[List[(String, String)]] = {
    val documents = ListBuffer[List[(String, String)]]()
    val rowData = ListBuffer[String]()
    Source.fromFile(segmentCorpusFile).getLines().foreach(line => {
      if (line.trim().isEmpty) {
        documents += rowData.toList.map(wordAndLabel => {
          val wordLabelPair = wordAndLabel.split("\t")
          (wordLabelPair(0), wordLabelPair(1))
        })
        rowData.clear()
      } else {
        rowData += line
      }
    })
    documents.toList
  }

  def foreachDocuments(segmentCorpusFile: String)(process: (List[(String, String)]) => Unit): Unit = {
    val rowData = ListBuffer[String]()
    Source.fromFile(segmentCorpusFile).getLines().foreach(line => {
      if (line.trim().isEmpty) {
        val document = rowData.toList.map(wordAndLabel => {
          val wordLabelPair = wordAndLabel.split("\t")
          (wordLabelPair(0), wordLabelPair(1))
        })
        process(document)
        rowData.clear()
      } else {
        rowData += line
      }
    })
  }

  def convertToSegmentDataSet(document: List[(String, String)], skipSelf: Boolean = false): List[(Int, Matrix, Int)] = {
    val inputs = document
      .map(wordAndLabel => (vocab.getIndex(wordAndLabel._1), getLabelIndex(wordAndLabel)))

    (0 until inputs.length).map(position => {
      if (position > 0) {
        val label = inputs(position)._2
        val lastLabel = inputs(position - 1)._2
        recordLabelTransition(lastLabel, label)
      }
      val label = inputs(position)._2
      val wordIndex = inputs(position)._1
      if (wordIndex > 0)
        (wordIndex, word2vecInputHelper.toMatrix(getContextWords(inputs, position, skipSelf)), label)
      else
        (wordIndex, Matrix(1, vectorSize), label)
    }).toList
  }

  def getLabelIndex(wordAndLabel: (String, String)): Int = word2vecInputHelper.labelMap(wordAndLabel._2)

  private def getLabels(inputs: List[(Int, Int)], position: Int): Array[Int] = {
    (1 to ngram).map(i => {
      val index = position + (i - ngram)
      if (index >= 0) inputs(index)._2 else 0
    }).toArray
  }

  private def recordLabelTransition(lastLabel: Int, label: Int): Unit = {
    labelTransitionProb(lastLabel, label) += 1D
  }

  def getContextWords(document: List[(Int, Int)], position: Int, skipSelf: Boolean = false): List[Int] = word2vecInputHelper.getContext(document, position, window, skipSelf).map(word => word._1).toList

}
