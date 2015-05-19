package me.yingrui.segment.word2vec

import me.yingrui.segment.math.Matrix

import scala.collection.mutable.ListBuffer
import scala.io.Source

class SegmentCorpus(word2VecModel: Array[Array[Double]], vocab: Vocabulary) {

  val vectorSize = word2VecModel(0).length
  val window = 2
  val labelMap = Map[String, Int]("S" -> 0, "B" -> 1, "M" -> 2, "E" -> 3)

  def load(segmentCorpusFile: String): Seq[(Matrix, Matrix)] = {
    val documents = loadDocuments(segmentCorpusFile)
    documents.map(doc => convert(doc)).flatten
  }

  def convert(document: List[(String, String)]): List[(Matrix, Matrix)] = {
    val inputs = document
      .filter(wordLabel => vocab.getIndex(wordLabel._1) > 0)
      .map(wordLabel => {
        (vocab.getIndex(wordLabel._1), labelMap(wordLabel._2))
      })

    (0 until inputs.length)
      .map(position => (toMatrix(getContextWords(inputs, position)), labelToMatrix(inputs(position)._2)))
      .toList
  }

  def getContextWords(document: List[(Int, Int)], position: Int): List[Int] = getContext(document, position, window).map(word => word._1)

  def getContext[T](document: List[T], position: Int, window: Int): List[T] = {
    val left = document.slice(if (position < window) 0 else position - window, position)
    val right = document.slice(position, if (position + window < document.length) position + window else document.length)
    left ++ List(document(position)) ++ right
  }

  def labelToMatrix(label: Int): Matrix = {
    val data = Array(0D, 0D, 0D, 0D)
    data(label) = 1D
    Matrix(data)
  }

  def toMatrix(inputs: List[Int]): Matrix = {
    val input = Matrix(1, vectorSize)
    inputs.foreach(index => input += Matrix(word2VecModel(index)))
    input / inputs.length.toDouble
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

}
