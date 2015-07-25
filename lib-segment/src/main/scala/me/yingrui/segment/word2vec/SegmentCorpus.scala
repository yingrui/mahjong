package me.yingrui.segment.word2vec

import me.yingrui.segment.math.Matrix

import scala.collection.mutable.ListBuffer
import scala.io.Source

class SegmentCorpus(word2VecModel: Array[Array[Double]], vocab: Vocabulary, ngram: Int = 1) {

  val vectorSize = word2VecModel(0).length
  val window = 1
  val labelTransitionProb = Matrix(4, 4)
  val word2vecInputHelper = new Word2VecInputHelper(ngram, vectorSize, word2VecModel)

  def getLabelTransitionProb(segmentCorpusFile: String): Matrix = {
    foreachDocuments(segmentCorpusFile) { document =>
      (0 until document.length).foreach(position => {
        if (position > 0) {
          val label = word2vecInputHelper.labelMap(document(position)._2)
          val lastLabel = word2vecInputHelper.labelMap(document(position - 1)._2)
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
      (vocab.getIndex(wordAndLabel._1), word2vecInputHelper.labelMap(wordAndLabel._2))
    })

    (0 until inputs.length).map(position => {
      if (position > 0) {
        val label = inputs(position)._2
        val lastLabel = inputs(position - 1)._2
        recordLabelTransition(lastLabel, label)
      }
      (word2vecInputHelper.toMatrix(getContextWords(inputs, position)), word2vecInputHelper.labelToMatrix(getLabels(inputs, position)))
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
    val inputs = document
      .filter(wordLabel => vocab.getIndex(wordLabel._1) > 0)
      .map(wordAndLabel => {
      (vocab.getIndex(wordAndLabel._1), word2vecInputHelper.labelMap(wordAndLabel._2))
    })

    (0 until inputs.length).map(position => {
      (getContextWords(inputs, position), inputs(position)._1, word2vecInputHelper.labelToMatrix(getLabels(inputs, position)))
    }).toList
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

  private def convertToSegmentDataSet(document: List[(String, String)]): List[(Int, Matrix, Int)] = {
    val inputs = document
      .map(wordAndLabel => (vocab.getIndex(wordAndLabel._1), word2vecInputHelper.labelMap(wordAndLabel._2)))

    (0 until inputs.length).map(position => {
      if (position > 0) {
        val label = inputs(position)._2
        val lastLabel = inputs(position - 1)._2
        recordLabelTransition(lastLabel, label)
      }
      val label = inputs(position)._2
      val wordIndex = inputs(position)._1
      if (wordIndex > 0)
        (wordIndex, word2vecInputHelper.toMatrix(getContextWords(inputs, position)), label)
      else
        (wordIndex, Matrix(1, vectorSize), label)
    }).toList
  }

  private def getLabels(inputs: List[(Int, Int)], position: Int): Array[Int] = {
    (1 to ngram).map(i => {
      val index = position + (i - ngram)
      if (index >= 0) inputs(index)._2 else 0
    }).toArray
  }

  private def recordLabelTransition(lastLabel: Int, label: Int): Unit = {
    labelTransitionProb(lastLabel, label) += 1D
  }

  private def getContextWords(document: List[(Int, Int)], position: Int): List[Int] = word2vecInputHelper.getContext(document, position, window).map(word => word._1).toList


}
