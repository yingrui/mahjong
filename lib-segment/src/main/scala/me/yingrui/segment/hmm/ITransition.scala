package me.yingrui.segment.hmm

import me.yingrui.segment.graph.SparseMatrix

trait ITransition {

  def getConditionProb(condition: Int, state: Int): Double

  def getConditionProb(condition: Array[Int], state: Int): Double
}

class BigramTransition(N: Int) extends ITransition {

  val matrix = new SparseMatrix[Double](N)

  def setProb(s1: Int, s2: Int, prob: Double) {
    matrix.set(s1, s2, 1, prob)
  }

  def getConditionProb(condition: Int, state: Int): Double = {

    matrix.getObject(condition, state) match {
      case Some(prob) => prob
      case _ => 0.0D
    }
  }

  def getConditionProb(condition: Array[Int], state: Int): Double = {
    getConditionProb(condition.last, state)
  }

}