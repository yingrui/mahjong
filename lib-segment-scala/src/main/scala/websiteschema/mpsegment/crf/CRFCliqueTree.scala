package websiteschema.mpsegment.crf

import CRFUtils._

class CRFCliqueTree(doc: CRFDocument, model: CRFModel, factors: Array[Factor]) {

  val z = (0 until model.labelCount).map(label => factors.map(f => f(label)).sum).toArray //[sum(y1), sum(y2), ...]

  val logZ = logSum(z)

  val sumWeights = {
    val sumProducts = for (i <- 0 until factors.length) yield factors(i)(doc.label(i))
    sumProducts.sum
  }

  def condLogProb: Double = sumWeights - logZ

  def condProb(position: Int, label: Int): Double = {
    val f = factors(position)
    prob(f(label), z)
  }
}

class Factor(val weightFactor: Array[Double]) {

  def apply(i: Int) = {
    weightFactor(i)
  }

  val z = weightFactor.sum
}

object CRFCliqueTree {

  def apply(doc: CRFDocument, model: CRFModel, weights: Array[Array[Double]]) = {
    val factors = for (datum <- doc.data) yield {
      val weightFactor = (for (label <- 0 until model.labelCount) yield {
        var weight = 0.0D
        for (feature <- datum) weight += weights(feature)(label)
        weight
      }).toArray
      new Factor(weightFactor)
    }

    new CRFCliqueTree(doc, model, factors.toArray)
  }
}
