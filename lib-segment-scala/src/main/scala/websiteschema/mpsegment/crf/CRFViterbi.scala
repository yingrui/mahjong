package websiteschema.mpsegment.crf

import websiteschema.mpsegment.hmm.Viterbi
import CRFUtils._

class CRFViterbi(model: CRFModel) extends Viterbi {

  val labels = model.labelRepository.featureIds
  val labelsArray = model.labelRepository.features.map(f => model.labelRepository.getFeatureId(f))

  def getStatesBy(observe: Seq[Int]): java.util.Collection[Int] = labels

  def calculateProbability(delta: Double, statePath: Array[Int], state: Int, observe: Array[Int]): (Double, Double) = {
    val f = model.getLabelFeature(statePath)
    val product = observe.map(feature => model.weight(feature, state)).sum
    val z = labelsArray.map(label => observe.map(feature => model.weight(feature, label)).sum).toArray

    val b = model.weight(f, state) - logSumExp(labelsArray.map(label => model.weight(f, label)).toArray)

    val p = delta + (product - logSumExp(z)) + b
//    val p = delta + product
    (p, p)
  }

  def calculateFirstState(firstObserve: Array[Int], state: Int): Double = {
    val product = firstObserve.map(feature => model.weight(feature, state)).sum
    val z = labelsArray.map(label => firstObserve.map(feature => model.weight(feature, label)).sum).toArray
    product - logSumExp(z)
//    product
  }

}
