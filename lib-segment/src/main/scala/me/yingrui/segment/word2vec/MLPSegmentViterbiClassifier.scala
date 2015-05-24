package me.yingrui.segment.word2vec

import java.lang.Math.log

import me.yingrui.segment.hmm.{ViterbiResult, Viterbi}
import me.yingrui.segment.math.Matrix
import me.yingrui.segment.neural.NeuralNetwork
import scala.collection.JavaConversions.asJavaCollection

class MLPSegmentViterbiClassifier(network: NeuralNetwork, transitionProb: Matrix) {

  private val labels = asJavaCollection(List(0, 1, 2, 3))
  private val defaultOutput = Matrix(Array(1D - 3E-20, 1E-20, 1E-20, 1E-20))

  def classify(listObserve: Seq[(Int, Matrix)]): ViterbiResult = {
    val probDist = listObserve.map(input => {
      val wordIndex = input._1
      val inputMatrix = input._2

      if(wordIndex > 0) network.computeOutput(inputMatrix) else defaultOutput
    })

    val viterbi = new MLPSegmentViterbi(labels, probDist, transitionProb)
    viterbi.calculateResult(listObserve.map(input => Array(input._1)))
  }

}

class MLPSegmentViterbi(labels: java.util.Collection[Int], probDist: Seq[Matrix], transitionProb: Matrix) extends Viterbi {

  override def getStatesBy(observe: Seq[Int]): java.util.Collection[Int] = labels

  override def calculateProbability(delta: Double, statePath: Array[Int], state: Int, observe: Array[Int]): (Double, Double) = {
    val dist = probDist(currentPostion)
    val b = log(dist(0, state))
    val Aij = log(transitionProb(statePath.last, state))
    val p = delta + Aij + b
    (p, p)
  }

  override def calculateFirstState(firstObserve: Array[Int], state: Int): Double = {
    val dist = probDist(0)
    val b = log(dist(0, state))
    val Aij = log(transitionProb(0, state))
    Aij + b
  }

}
