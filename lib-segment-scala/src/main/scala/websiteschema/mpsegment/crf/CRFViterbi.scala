package websiteschema.mpsegment.crf

import websiteschema.mpsegment.hmm.Viterbi

class CRFViterbi(model: CRFModel) extends Viterbi {

  val labels = java.util.Arrays.asList(0, 1)

  def getStatesBy(observe: Seq[Int]): java.util.Collection[Int] = labels

  def calculateProbability(delta: Double, statePath: Array[Int], state: Int, observe: Array[Int]): (Double, Double) = {
    val f = model.getLabelFeature(Array(statePath(0), state))
    val product = observe.map(feature => model.weights(feature)(state)).sum + model.weights(f)(state)
    val p = delta + product
    (p, p)
  }

  def calculateFirstState(firstObserve: Array[Int], state: Int): Double = {
    val product = firstObserve.map(feature => model.weights(feature)(state)).sum
    product
  }

}
