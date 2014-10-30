package websiteschema.mpsegment.crf

class CRFDiffFunc(corpus: CRFCorpus, model: CRFModel) {

  def valueAt(x: Array[Double]): Double = {
    calculate(x)
  }

  val derivative: Array[Double] = new Array(model.featuresCount * model.labelCount)
  val sigma = 1.0D

  private def calculate(x: Array[Double]): Double = {
    val weights = to2D(x)
    val E = CRFUtils.empty2DArray(model.featuresCount, model.labelCount)

    var prob = 0.0D
    for (doc_i <- corpus.docs) {
      val clique = CRFCliqueTree(doc_i, model, weights)

      for (position_t <- 0 until doc_i.data.length; feature_k <- doc_i.data(position_t)) {
        for (label_y <- 0 until model.labelCount) {

          E(feature_k)(label_y) += clique.condProb(position_t, label_y)
        }
      }

      prob += clique.condLogProb
    }
//    prob = -prob

    val sigmaSq = sigma * sigma
    for (feature <- 0 until model.featuresCount; label <- 0 until model.labelCount) {
      val i = feature * model.labelCount + label
      derivative(i) = corpus.Ehat(feature)(label) - E(feature)(label) - (x(i) / sigmaSq)
      prob -= x(i) * x(i) / 2 / sigmaSq
    }

    prob
  }

  private def to2D(x: Array[Double]): Array[Array[Double]] = {
    var i = 0
    val array = for (feature <- 0 until model.featuresCount) yield {
      (for (index <- 0 until model.getLabelCount(feature)) yield {
        val weight = x(i)
        i += 1
        weight
      }).toArray
    }
    array.toArray
  }

}
