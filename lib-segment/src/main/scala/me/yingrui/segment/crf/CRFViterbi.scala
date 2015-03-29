package me.yingrui.segment.crf

import me.yingrui.segment.hmm.Viterbi
import CRFUtils._

class CRFViterbi(model: CRFModel) extends Viterbi {

  val labels = model.labelRepository.featureIds
  val labelsArray = model.labelRepository.features.map(f => model.labelRepository.getFeatureId(f))

  def getStatesBy(observe: Seq[Int]): java.util.Collection[Int] = labels

  def calculateProbability(delta: Double, statePath: Array[Int], state: Int, observe: Array[Int]): (Double, Double) = {
    val p = calculateLogProb(delta, statePath, state, observe)
    (p, p)
  }

  def calculateFirstState(firstObserve: Array[Int], state: Int): Double = {
    calculateLogProb(delta = 0, statePath = Array[Int](), state, firstObserve)
  }

  private def calculateLogProb(delta: Double, statePath: Array[Int], state: Int, observe: Array[Int]): Double = {
    val b: Double = getTransitionProb(statePath, state)

    val product = observe.map(feature => model.weight(feature, state)).sum
    val z = labelsArray.map(label => observe.map(feature => model.weight(feature, label)).sum).toArray

    delta + (product - logSumExp(z)) + b
  }

  def getTransitionProb(statePath: Array[Int], state: Int): Double = {
    if(statePath.isEmpty) {
      0D
    } else {
      val f = model.getLabelFeature(statePath)
      val b = model.weight(f, state) - logSumExp(labelsArray.map(label => model.weight(f, label)).toArray)
      b
    }
  }
}
