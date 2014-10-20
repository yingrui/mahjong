package websiteschema.mpsegment.crf

class CRFCliqueTree {

  def condLogProb: Double = 0.0D

}

object CRFCliqueTree {

  def apply(doc: CRFDocument, model: CRFModel, weights: Array[Array[Double]]) = {
    val cliqueTree = new CRFCliqueTree

    for (datum <- doc.data) yield {

    }

    cliqueTree
  }
}
