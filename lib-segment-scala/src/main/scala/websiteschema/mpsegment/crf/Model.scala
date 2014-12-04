package websiteschema.mpsegment.crf

class CRFModel(val featureRepository: FeatureRepository, val labelRepository: FeatureRepository) {

  val featuresCount = featureRepository.size
  val labelCount = labelRepository.size

  def getLabelFeature(labels: Array[Int]) = featureRepository.getLabelFeatureId(labels(0), labels.last)

  val weights = CRFUtils.empty2DArray(featuresCount, labelCount)
  val tolerance = 1.0E-4

  def getLabelCount(feature: Int) = labelCount
}

object CRFUtils {

  def empty2DArray(i: Int, j: Int) = {
    val array = new Array[Array[Double]](i)
    for(index <- 0 until i) {
      array(index) = new Array[Double](j)
    }
    array
  }

}

object CRFModel {

  def build(corpus: CRFCorpus) = {
    val model = new CRFModel(corpus.featureRepository, corpus.labelRepository)

    val func = new CRFDiffFunc(corpus, model)

    for(iter <- 0 until 10) {
      val value = func.valueAt(model.weights)
      val grad = if(iter < 3) 0.1 else 0.5
      for (i <- 0 until model.featuresCount; j <- 0 until model.labelCount) {
        model.weights(i)(j) = model.weights(i)(j) + grad * func.derivative(i)(j)
      }
      val sum = func.derivative.map(array => array.sum).sum
      println(s"Iteration $iter: $value, $sum")
    }

    model
  }
}