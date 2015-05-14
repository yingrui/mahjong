package me.yingrui.segment.word2vec

import java.lang.Math.sqrt

import scala.util.Random
import Word2VecUtil.simplifiedSigmoid

object BagOfWordNetwork {

  private def random = new Random()

  private def random2DArray(row: Int, col: Int): Array[Array[Double]] = {
    val arrays = new Array[Array[Double]](row)
    for (i <- 0 until row) {
      arrays(i) = randomArray(col)
    }
    arrays
  }

  private def randomArray(size: Int): Array[Double] = {
    (for (i <- 0 until size) yield ((random.nextDouble() - 0.5D) / size.toDouble)).toArray
  }

  private def zero2DArray(row: Int, col: Int): Array[Array[Double]] = {
    val arrays = new Array[Array[Double]](row)
    for (i <- 0 until row) {
      arrays(i) = (for (i <- 0 until col) yield 0D).toArray
    }
    arrays
  }

  def apply(wordsCount: Int, size: Int): BagOfWordNetwork = {
    new BagOfWordNetwork(wordsCount, size, random2DArray(wordsCount, size), zero2DArray(wordsCount, size))
  }

  def apply(wordsCount: Int, size: Int, wordVector: Array[Array[Double]], layer1Weights: Array[Array[Double]]): BagOfWordNetwork = {
    new BagOfWordNetwork(wordsCount, size, wordVector, layer1Weights)
  }
}

class BagOfWordNetwork(val wordsCount: Int, val size: Int, val wordVector: Array[Array[Double]], val layer1Weights: Array[Array[Double]]) extends Word2VecNetwork {

  val layer0Output = new Array[Double](size)

  var loss = 0D
  var learningTimes = 0D

  def learn(input: Array[Int], output: Array[(Int, Int)], alpha: Double): Unit = {

    computeLayer0Output(input.filter(wordIndex => wordIndex > 0))

    val errors = new Array[Double](size)

    for ((wordIndex, label) <- output) yield {
      val grad = computeLayer1Grad(layer0Output, alpha, wordIndex, label)
      updateLayer1WeightsAndPropagateErrors(layer0Output, errors, wordIndex, grad)
    }
//    val grads = computeLayer1Grads(layer0Output, output, alpha)
//    val errors = updateLayer1WeightsAndPropagateErrors(grads, layer0Output)

    updateLayer0Weights(errors, input)

    learningTimes += 1D
  }

  def clearError {
    loss = 0D
    learningTimes = 0D
  }

  def getLoss = sqrt(loss / learningTimes)

  def computeLayer0Output(input: Array[Int]): Array[Double] = {
    //    print("window: %d ".format(input.length))

    update(layer0Output, 0D)
    for (wordIndex <- input) {
      var col = 0
      while (col < size) {
        layer0Output(col) += wordVector(wordIndex)(col)
        col += 1
      }
    }

    val inputWordCount = input.length.toDouble
    var col = 0
    while (col < size) {
      layer0Output(col) /= inputWordCount
      col += 1
    }
    layer0Output
  }

  private def update(a: Array[Double], value: Double) {
    var i = 0
    while (i < a.length) {
      a(i) = value
      i += 1
    }
  }

  private def multiply(a: Array[Double], b: Array[Double]): Double = {
    var result = 0D
    var i = 0
    while (i < a.length) {
      result += a(i) * b(i)
      i += 1
    }
    result
  }

  def computeLayer1Grads(input: Array[Double], output: Array[(Int, Int)], alpha: Double): Array[(Int, Double)] = {
    val grads = for ((wordIndex, label) <- output) yield {
      (wordIndex, computeLayer1Grad(input, alpha, wordIndex, label))
    }
    grads.toArray
  }

  def computeLayer1Grad(input: Array[Double], alpha: Double, wordIndex: Int, label: Int): Double = {
    val weights = layer1Weights(wordIndex)
    val f = multiply(input, weights)
    val output = simplifiedSigmoid(f)
    val error = label.toDouble - output
    loss += Math.pow(error, 2D)
    val grad = error * alpha
//    println("grad: %2.5f  f: %2.5f  alpha: %2.5f  index: %d  output: %d".format(grad, f, alpha, wordIndex, label))
    grad
  }

  def updateLayer1WeightsAndPropagateErrors(grads: Array[(Int, Double)], layer1Input: Array[Double]): Array[Double] = {
    val errors = new Array[Double](size)
    for ((wordIndex, grad) <- grads) {
      updateLayer1WeightsAndPropagateErrors(layer1Input, errors, wordIndex, grad)
    }
    errors
  }

  def updateLayer1WeightsAndPropagateErrors(layer1Input: Array[Double], errors: Array[Double], wordIndex: Int, grad: Double): Unit = {
    var col = 0
    while (col < size) {
      errors(col) += grad * layer1Weights(wordIndex)(col)
      layer1Weights(wordIndex)(col) += grad * layer1Input(col)
      col += 1
    }
  }

  def updateLayer0Weights(errors: Array[Double], wordIndexes: Array[Int]): Unit = {
//    println(wordIndexes.toList + "  " + errors.toList)
    for (wordIndex <- wordIndexes) {
      var col = 0
      while (col < size) {
        wordVector(wordIndex)(col) += errors(col)
        col += 1
      }
    }
  }
}