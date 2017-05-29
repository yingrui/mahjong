package me.yingrui.segment.word2vec

import java.lang.Math.log

import me.yingrui.segment.hmm.{ViterbiResult, Viterbi}
import me.yingrui.segment.math.Matrix
import me.yingrui.segment.neural.{BackPropagation, NeuralNetwork}
import scala.collection.JavaConversions.asJavaCollection

class RNNSegmentViterbiClassifier(val networks: Seq[BackPropagation], val transitionProb: Matrix, val ngram: Int) {

  private val labels = asJavaCollection(List(0, 1, 2, 3))

  def classify(listObserve: Seq[(Int, Matrix)]): Seq[Int] = {
    val probDist = listObserve.map(input => {
      val wordIndex = input._1
      val inputMatrix = input._2

      networks(wordIndex).computeOutput(inputMatrix)
    })

    val viterbi = new NeuralNetworkSegmentViterbi(labels, probDist, transitionProb, 1)
    val result = viterbi.calculateResult(listObserve.map(input => Array(input._1)))
    result.getBestPath
  }

  def findMaximum(actualOutput: Matrix): Int = {
    var maxIndex = 0
    var maxValue = 0D
    for (i <- 0 until actualOutput.col) {
      if (actualOutput(0, i) > maxValue) {
        maxValue = actualOutput(0, i)
        maxIndex = i
      }
    }
    maxIndex
  }

}

class MNNSegmentViterbiClassifier(val networks: Seq[NeuralNetwork], val transitionProb: Matrix, val ngram: Int) {
  private val labels = asJavaCollection(List(0, 1, 2, 3))

  def classify(listObserve: Seq[(Int, Matrix)]): ViterbiResult = {
    val probDist = listObserve.map(input => {
      val wordIndex = input._1
      val inputMatrix = input._2

      networks(wordIndex).computeOutput(inputMatrix)
    })

    val viterbi = new NeuralNetworkSegmentViterbi(labels, probDist, transitionProb, ngram)
    viterbi.calculateResult(listObserve.map(input => Array(input._1)))
  }
}

class NeuralNetworkSegmentViterbi(val labels: java.util.Collection[Int], val probDist: Seq[Matrix], val transitionProb: Matrix, val ngram: Int) extends Viterbi {

  private val numberOfLabels = labels.size()

  override def getStatesBy(observe: Seq[Int]): java.util.Collection[Int] = labels

  override def calculateProbability(delta: Double, statePath: Array[Int], state: Int, observe: Array[Int]): (Double, Double) = {
    val b = log(prob(currentPosition, state))
    val Aij = log(transitionProb(currentPosition, statePath, state))
    val p = delta + Aij + b
    (p, p)
  }

  override def calculateFirstState(firstObserve: Array[Int], state: Int): Double = {
    val b = log(prob(0, state))
    val Aij = log(transitionProb(0, Array(0), state))
    Aij + b
  }

  private def prob(position: Int, state: Int): Double = {
    val dist = probDist(position)
    val prob = if (ngram == 2)
      (0 until numberOfLabels).foldLeft(0D)((p, index) => p + dist(0, index * numberOfLabels + state))
    else
      dist(0, state)

    if(ngram > 1 && position < probDist.length - 1) {
      val next = (0 until numberOfLabels).foldLeft(0D)((p, index) => p + probDist(position + 1)(numberOfLabels * state, index))
      prob * next
    } else
      prob
  }

  private def transitionProb(position: Int, statePath: Array[Int], state: Int): Double = {
    val last = statePath.last
    transitionProb(last, state)
//    val dist = probDist(position)
//    dist(0, last * numberOfLabels + state)
//    transitionProb(last, state) * dist(0, last * numberOfLabels + state)
  }

}
