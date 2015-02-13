package websiteschema.mpsegment.crf

import websiteschema.mpsegment.math.Matrix

class CRFDiffFunc(corpus: CRFCorpus, model: CRFModel) {

  def valueAt(x: Matrix): Double = calculate(x)

  val derivative = Matrix(model.featuresCount, model.labelCount)
  val sigma = 1.0D
  val sigmaSq = sigma * sigma

  private def calculate(weights: Matrix): Double = {
    val E = Matrix(model.featuresCount, model.labelCount)

    var prob = 0.0D

    for (doc_i <- corpus.docs) {
      val clique = CRFClique(doc_i, model, weights)

      for (t <- 0 until doc_i.data.length) {
        prob += clique.condLogProb(t, doc_i.label(t), Array[Int]())

        for (label <- 0 until model.labelCount) {
          val p = clique.condProb(t, label, Array[Int]())
          for (feature <- doc_i.data(t)) {
            E(feature, label) = E(feature, label) + p
          }
        }
      }
    }

    val regular = (for (feature <- 0 until model.featuresCount; label <- 0 until model.labelCount) yield {
      val x_i = weights(feature, label)
      val e = E(feature, label)
      derivative(feature, label) = corpus.Ehat(feature)(label) - e - (x_i / sigmaSq)
      x_i * x_i / 2 / sigmaSq
    }).sum

    regular - prob
  }

}
