package websiteschema.mpsegment.crf

class CRFDiffFunc(corpus: CRFCorpus, model: CRFModel) {

  def valueAt(x: Array[Array[Double]]): Double = {
    calculate(x)
  }

  val derivative = CRFUtils.empty2DArray(model.featuresCount, model.labelCount)
  val sigma = 1.0D

  private def calculate(weights: Array[Array[Double]]): Double = {
    val E = CRFUtils.empty2DArray(model.featuresCount, model.labelCount)

    val prob = (for (doc_i <- corpus.docs) yield {
      val clique = CRFCliqueTree(doc_i, model, weights)

      for (position_t <- 0 until doc_i.data.length; feature_k <- doc_i.data(position_t)) {
        for (label_y <- 0 until model.labelCount) {
          E(feature_k)(label_y) += clique.condProb(position_t, label_y)
        }
      }

      clique.condLogProb
    }).sum

    val sigmaSq = sigma * sigma
    val regular = (for (feature <- 0 until model.featuresCount; label <- 0 until model.labelCount) yield {
      val x_i = weights(feature)(label)
      derivative(feature)(label) = corpus.Ehat(feature)(label) - E(feature)(label) - (x_i / sigmaSq)
      x_i * x_i / 2 / sigmaSq
    }).sum

    prob - regular
  }

}
