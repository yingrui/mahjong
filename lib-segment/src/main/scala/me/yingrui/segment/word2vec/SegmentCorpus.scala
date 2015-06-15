package me.yingrui.segment.word2vec

import java.lang.Math.pow

import me.yingrui.segment.math.Matrix

import scala.collection.mutable.ListBuffer
import scala.io.Source

class SegmentCorpus(word2VecModel: Array[Array[Double]], vocab: Vocabulary, ngram: Int = 1) {

  val vectorSize = word2VecModel(0).length
  val window = 1
  val labelMap = Map[String, Int]("S" -> 0, "B" -> 1, "M" -> 2, "E" -> 3)
  val labelTransitionProb = Matrix(4, 4)

  def buildLabelTransitionProb: Matrix = {
    for (i <- 0 until labelTransitionProb.row) {
      val total = labelTransitionProb.row(i).sum
      for (j <- 0 until labelTransitionProb.col) {
        labelTransitionProb(i, j) /= total
      }
    }
    labelTransitionProb
  }

  def loadSegmentDataSet(segmentCorpusFile: String): List[Seq[(Int, Matrix, Int)]] = {
    loadDocuments(segmentCorpusFile).map(doc => convertToSegmentDataSet(doc))
  }

  private def convertToSegmentDataSet(document: List[(String, String)]): List[(Int, Matrix, Int)] = {
    val inputs = document
      .map(wordAndLabel => (vocab.getIndex(wordAndLabel._1), labelMap(wordAndLabel._2)))

    (0 until inputs.length).map(position => {
      val label = inputs(position)._2
      val wordIndex = inputs(position)._1
      if (wordIndex > 0)
        (wordIndex, toMatrix(getContextWords(inputs, position)), label)
      else
        (wordIndex, Matrix(1, vectorSize), label)
    }).toList
  }

  def load(segmentCorpusFile: String): Seq[(Matrix, Matrix)] = {
    loadDocuments(segmentCorpusFile).map(doc => convert(doc)).flatten
  }

  def convert(document: List[(String, String)]): List[(Matrix, Matrix)] = {
    val inputs = document
      .filter(wordLabel => vocab.getIndex(wordLabel._1) > 0)
      .map(wordAndLabel => {
      (vocab.getIndex(wordAndLabel._1), labelMap(wordAndLabel._2))
    })

    (0 until inputs.length).map(position => {
      if (position > 0) {
        val label = inputs(position)._2
        val lastLabel = inputs(position - 1)._2
        recordLabelTransition(lastLabel, label)
      }
      (toMatrix(getContextWords(inputs, position)), labelToMatrix(getLabels(inputs, position)))
    }).toList
  }

  private def getLabels(inputs: List[(Int, Int)], position: Int): Array[Int] = {
    (0 until ngram).map(i => {
      val index = position + i
      if (index < inputs.length) inputs(index)._2 else 0
    }).toArray
  }

  private def recordLabelTransition(lastLabel: Int, label: Int): Unit = {
    labelTransitionProb(lastLabel, label) += 1D
  }

  private def getContextWords(document: List[(Int, Int)], position: Int): List[Int] = getContext(document, position, window).map(word => word._1)

  private def getContext[T](document: List[T], position: Int, window: Int): List[T] = {
    val left = document.slice(if (position < window) 0 else position - window, position)
    val right = document.slice(position, if (position + window < document.length) position + window else document.length)
    left ++ List(document(position)) ++ right
  }

  private def labelToMatrix(labels: Array[Int]): Matrix = {
    val classNumber = labelMap.size
    val data = new Array[Double](pow(classNumber, ngram).toInt)
    val index = (1 to ngram).foldLeft(0)((x, i) => {
      labels(i - 1) * pow(classNumber, ngram - i).toInt + x
    })

    data(index) = 1D
    Matrix(data)
  }

  private def toMatrix(inputs: List[Int]): Matrix = {
    val input = Matrix(1, vectorSize)
    inputs.foreach(index => if (index > 0) input += Matrix(word2VecModel(index)))
    input / inputs.length.toDouble
  }

  private def loadDocuments(segmentCorpusFile: String): List[List[(String, String)]] = {
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

}
