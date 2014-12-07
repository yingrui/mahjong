package websiteschema.mpsegment.crf

import websiteschema.mpsegment.hmm.Viterbi
import CRFUtils._

class CRFViterbi(model: CRFModel) extends Viterbi {

  val labels = model.labelRepository.featureIds
  val labelsArray = model.labelRepository.features.map(f => model.labelRepository.getFeatureId(f))

  def getStatesBy(observe: Seq[Int]): java.util.Collection[Int] = labels

  def calculateProbability(delta: Double, statePath: Array[Int], state: Int, observe: Array[Int]): (Double, Double) = {
    //val f = model.getLabelFeature(statePath)
    val product = observe.map(feature => model.weights(feature)(state)).sum //+ model.weights(f)(state)
    val z = labelsArray.map(label => observe.map(feature => model.weights(feature)(label)).sum).toArray

    val p = delta + (product - logSum(z))
//    val p = delta + product
    (p, p)
  }

  def calculateFirstState(firstObserve: Array[Int], state: Int): Double = {
    val product = firstObserve.map(feature => model.weights(feature)(state)).sum
    val z = labelsArray.map(label => firstObserve.map(feature => model.weights(feature)(label)).sum).toArray
    product - logSum(z)
  }

}
