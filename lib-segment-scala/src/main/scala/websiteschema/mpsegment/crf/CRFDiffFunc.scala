package websiteschema.mpsegment.crf

class CRFDiffFunc(corpus: CRFCorpus, model: CRFModel) {

  def valueAt(x: Array[Array[Double]]): Double = {
    calculate(x)
  }

  val derivative = CRFUtils.empty2DArray(model.featuresCount, model.labelCount)
  val sigma = 1.0D
  val sigmaSq = sigma * sigma

  private def calculate(weights: Array[Array[Double]]): Double = {
    val E = CRFUtils.empty2DArray(model.featuresCount, model.labelCount)

    val prob = (for (doc_i <- corpus.docs) yield {
      val clique = CRFCliqueTree(doc_i, model, weights)

      for (t <- 0 until doc_i.data.length; feature <- doc_i.data(t)) {
        val label = doc_i.label(t)
        val p = clique.condProb(t, label)
        E(feature)(label) += p
      }

      clique.condLogProb
    }).sum

    val regular = (for (feature <- 0 until model.featuresCount; label <- 0 until model.labelCount) yield {
      val x_i = weights(feature)(label)
      val ehat = corpus.Ehat(feature)(label)
      val e = E(feature)(label)
      derivative(feature)(label) = ehat - e - (x_i / sigmaSq)
      x_i * x_i / 2 / sigmaSq
    }).sum

    prob - regular
  }

}
