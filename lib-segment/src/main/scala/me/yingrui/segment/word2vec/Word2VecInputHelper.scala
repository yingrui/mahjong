package me.yingrui.segment.word2vec

import java.lang.Math.pow

import me.yingrui.segment.math.Matrix

class Word2VecInputHelper(ngram: Int, inputVectorSize: Int, word2VecModel: Array[Array[Double]]) {

  val labelMap = Map[String, Int]("S" -> 0, "B" -> 1, "M" -> 2, "E" -> 3)

  val defaultLabel = "S"
  val defaultLabelIndex = labelMap(defaultLabel)

  def defaultLabels(ngram: Int): Array[Int] = (0 until ngram).map(_ => defaultLabelIndex).toArray

  def isStartLabel(label: Int): Boolean = {
    label <= 1
  }

  def getContext[T](document: Seq[T], position: Int, window: Int, skipSelf: Boolean = false): Seq[T] = {
    val left = document.slice(if (position < window) 0 else position - window, position)
    val right = document.slice(position + 1, if (position + window < document.length) position + window + 1 else document.length)
    if (skipSelf)
      left ++ right
    else
      left ++ List(document(position)) ++ right
  }

  def labelToMatrix(labels: Array[Int]): Matrix = {
    val classNumber = labelMap.size
    val data = new Array[Double](pow(classNumber, ngram).toInt)
    val index = (1 to ngram).foldLeft(0)((x, i) => {
      labels(i - 1) * pow(classNumber, ngram - i).toInt + x
    })

    data(index) = 1D
    Matrix(data)
  }

  def toMatrix(inputs: Seq[Int]): Matrix = {
    if (inputs.isEmpty) {
      Matrix(1, inputVectorSize)
    } else {
      val input = Matrix(1, inputVectorSize)
      inputs.foreach(index => if (index > 0) input += Matrix(word2VecModel(index)))
      input / inputs.length.toDouble
    }
  }

}
