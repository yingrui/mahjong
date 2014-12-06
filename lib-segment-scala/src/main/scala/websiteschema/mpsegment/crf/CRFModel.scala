package websiteschema.mpsegment.crf

import websiteschema.mpsegment.util.SerializeHandler

class CRFModel(val featureRepository: FeatureRepository, val labelRepository: FeatureRepository, val weights: Array[Array[Double]]) {

  def this(featureRepository: FeatureRepository, labelRepository: FeatureRepository) =
        this(featureRepository, labelRepository, CRFUtils.empty2DArray(featureRepository.size, labelRepository.size))

  val featuresCount = featureRepository.size
  val labelCount = labelRepository.size

  def getLabelFeature(labels: Array[Int]) = featureRepository.getLabelFeatureId(labels(0), labels.last)

//  val weights = CRFUtils.empty2DArray(featuresCount, labelCount)
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

  private val maxExpValue = Math.exp(50)

  def exp(x: Double) = if(x > 50) maxExpValue else Math.exp(x)
}

object CRFModel {

  def build(corpus: CRFCorpus) = {
    val model = new CRFModel(corpus.featureRepository, corpus.labelRepository)

    val func = new CRFDiffFunc(corpus, model)

    for(iter <- 0 until 10) {
      val value = func.valueAt(model.weights)
      val grad = if(iter < 3) 1E-1 else 1
      for (i <- 0 until model.featuresCount; j <- 0 until model.labelCount) {
        model.weights(i)(j) = model.weights(i)(j) + grad * func.derivative(i)(j)
      }
      val sum = func.derivative.map(array => array.sum).sum
      println(s"Iteration $iter: $value, $sum")
    }

    model
  }

  def save(model: CRFModel, file: String) {
    val writer = SerializeHandler(new java.io.File(file), SerializeHandler.WRITE_ONLY)

    FeatureRepository.save(model.featureRepository, writer)
    FeatureRepository.save(model.labelRepository, writer)

    writer.serialize2DArrayDouble(model.weights)

    writer.close()
  }

  def apply(file: String) = {
    val reader = SerializeHandler(new java.io.File(file), SerializeHandler.READ_ONLY)

    val featureRepository = FeatureRepository(reader)
    val labelRepository = FeatureRepository(reader)

    val weights = reader.deserialize2DArrayDouble()

    reader.close()

    new CRFModel(featureRepository, labelRepository, weights)
  }
}