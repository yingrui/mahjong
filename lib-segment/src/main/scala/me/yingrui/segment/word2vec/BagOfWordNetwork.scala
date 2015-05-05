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
    (for(i <- 0 until size) yield random.nextDouble() * 1E-1).toArray
  }

  def apply(wordsCount: Int, size: Int): BagOfWordNetwork = {
    new BagOfWordNetwork(wordsCount, size, random2DArray(wordsCount, size), random2DArray(wordsCount, size))
  }

  def apply(wordsCount: Int, size: Int, wordVector: Array[Array[Double]], layer1Weights: Array[Array[Double]]): BagOfWordNetwork = {
    new BagOfWordNetwork(wordsCount, size, wordVector, layer1Weights)
  }
}

class BagOfWordNetwork(val wordsCount: Int, val size: Int, val wordVector: Array[Array[Double]], val layer1Weights: Array[Array[Double]]) extends Word2VecNetwork {

  val layer0Output = new Array[Double](size)
  val alpha = 0.1D

  var loss = 0D
  var learningTimes = 0D

  def learn(input: Array[Int], output: Array[(Int, Int)]): Unit = {

    computeLayer0Output(input)
    val grads = computeLayer1Grads(layer0Output, output)
    val errors = updateLayer1WeightsAndPropagateErrors(grads, layer0Output)
    updateLayer0Weights(errors, output.map(t => t._1))

    learningTimes += 1D
  }

  def clearError {
    loss = 0D
    learningTimes = 0D
  }

  def getLoss = sqrt(loss / learningTimes)

  def computeLayer0Output(input: Array[Int]): Array[Double] = {
    for(wordIndex <- input) {
      val wordVec = wordVector(wordIndex)
      for(i <- 0 until size) layer0Output(i) += wordVec(i)
    }
    val inputWordCount = input.length.toDouble
    for(i <- 0 until size) layer0Output(i) /= inputWordCount
    layer0Output
  }

  def computeLayer1Grads(input: Array[Double], output: Array[(Int, Int)]): Array[(Int, Double)] = {
    val grads = for((wordIndex, label) <- output) yield {
      val weights = layer1Weights(wordIndex)
      val output = simplifiedSigmoid((for(i <- 0 until size) yield {input(i) * weights(i)}).sum)
      val error = label.toDouble - output
      loss += Math.pow(error, 2D)
      val grad = error * alpha
      (wordIndex, grad)
    }
    grads.toArray
  }

  def updateLayer1WeightsAndPropagateErrors(grads: Array[(Int, Double)], layer1Input: Array[Double]): Array[Double] = {
    val errors = new Array[Double](size)
    for((wordIndex, grad) <- grads) {
      for(col <- 0 until size) {
        errors(col) += grad * layer1Weights(wordIndex)(col)
        layer1Weights(wordIndex)(col) += grad * layer1Input(col)
      }
    }
    errors
  }

  def updateLayer0Weights(errors: Array[Double], wordIndexes: Array[Int]): Unit = {
    for(wordIndex <- wordIndexes) {
      for(i <- 0 until size) {
        wordVector(wordIndex)(i) += errors(i)
      }
    }
  }
}