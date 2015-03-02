package websiteschema.mpsegment.crf

import CRFUtils._
import websiteschema.mpsegment.math.Matrix

class CRFClique(doc: CRFDocument, labelCount: Int, factors: Array[Factor]) {

  val z = (0 until labelCount).map(label => factors.map(f => f(label)).sum).toArray //[sum(y1), sum(y2), ...]

  val logZ = logSumExp(z)

  val sumWeights = {
    val sumProducts = for (i <- 0 until factors.length) yield factors(i)(doc.label(i))
    sumProducts.sum
  }

  /**
   * log{ exp(weight) / [exp(w1) + exp(w2) + ... + exp(wn)] }
   *  = weight - log[exp(w1) + exp(w2) + ... + exp(wn)]
   *  = weight - logSumExp([w1, w2, ... ,wn])
   * @param t
   * @param label
   * @param given
   * @return
   */
  def condLogProb(t: Int, label: Int, given: Array[Int]): Double = {
    val sumWeightOfLabel = factors(t)(label)
    sumWeightOfLabel - logSumExp(factors(t).weightFactor)
  }

  def condProb(t: Int, label: Int, given: Array[Int]): Double = Math.exp(condLogProb(t, label, given))
}

class Factor(val weightFactor: Array[Double]) {

  def apply(i: Int) = {
    weightFactor(i)
  }

  val z = logSumExp(weightFactor)
}

object CRFClique {

  def apply(doc: CRFDocument, model: CRFModel, weights: Array[Double]) = {
    val factors = for (datum <- doc.data) yield {
      val weightFactor = (for (label <- 0 until model.labelCount) yield {
        var weight = 0.0D
        for (feature <- datum) weight += weights(model.labelCount * feature + label)
        weight
      }).toArray

      new Factor(weightFactor)
    }

    new CRFClique(doc, model.labelCount, factors.toArray)
  }

  def apply(doc: CRFDocument, model: CRFModel, weights: Matrix) = {
    val factors = for (datum <- doc.data) yield {
      val weightFactor = (for (label <- 0 until model.labelCount) yield {
        var weight = 0.0D
        for (feature <- datum) weight += weights(feature, label)
        weight
      }).toArray

      new Factor(weightFactor)
    }

    new CRFClique(doc, model.labelCount, factors.toArray)
  }
}
