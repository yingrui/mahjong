package websiteschema.mpsegment.crf

import websiteschema.mpsegment.crf.CRFUtils._

class CRFDiffFunc(corpus: CRFCorpus, model: CRFModel) {

  def valueAt(x: Array[Double]): Double = {
    calculate(x)
  }

  private def calculate(x: Array[Double]): Double = {
    val weights = to2D(x)
    val E = CRFUtils.empty2DArray(model.featuresCount, model.classesCount)

    for(doc <- corpus.docs) {
      val clique = CRFCliqueTree(doc, model, weights)

    }

    0.0D
  }

  private def to2D(x: Array[Double]): Array[Array[Double]] = {
    var i = 0
    val array = for(feature <- 0 until model.featuresCount) yield {
      (for(index <- 0 until model.getClassesCount(feature)) yield {
        val weight = x(i)
        i += 1
        weight
      }).toArray
    }
    array.toArray
  }

}
