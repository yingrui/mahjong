package websiteschema.mpsegment.crf

class CRFCliqueTree(doc: CRFDocument, model: CRFModel, factors: Array[Factor]) {

  val z = factors.map(_.z).sum

  val logZ = Math.log(z)

  val sumWeights = {
    val sumProducts = for (i <- 0 until factors.length) yield factors(i)(doc.label(i))
    sumProducts.sum
  }

  def condLogProb: Double = {
    sumWeights - logZ
  }

  def condProb(position: Int, label: Int): Double = {
    val f = factors(position)(label)
    Math.exp(f) / z
  }

}

class Factor(val weightFactor: Array[Double]) {

  def apply(i: Int) = {
    weightFactor(i)
  }

  val z = weightFactor.map(factor => Math.exp(factor)).sum
}

object CRFCliqueTree {

  def apply(doc: CRFDocument, model: CRFModel, weights: Array[Array[Double]]) = {
    val factors = for (datum <- doc.data) yield {
      val weightFactor = (for (label <- 0 until model.labelCount) yield {
        var weight = 0.0D
        for (feature <- datum(0)) weight += weights(feature)(label)
        weight
      }).toArray
      new Factor(weightFactor)
    }

    new CRFCliqueTree(doc, model, factors.toArray)
  }
}
