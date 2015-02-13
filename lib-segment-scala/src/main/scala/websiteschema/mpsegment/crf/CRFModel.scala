package websiteschema.mpsegment.crf

import websiteschema.mpsegment.math.Matrix
import websiteschema.mpsegment.util.SerializeHandler

class CRFModel(val featureRepository: FeatureRepository, val labelRepository: FeatureRepository, val weights: Matrix) {

  def this(featureRepository: FeatureRepository, labelRepository: FeatureRepository) =
    this(featureRepository, labelRepository, Matrix(featureRepository.size, labelRepository.size))

  val featuresCount = featureRepository.size
  val labelCount = labelRepository.size

  def getLabelFeature(labels: Array[Int]) = featureRepository.getLabelFeatureId(labels.last)

  val tolerance = 1.0E-4

  def getLabelCount(feature: Int) = labelCount

  def weight(feature: Int, label: Int) = weights(feature, label)
}

object CRFUtils {

  def empty2DArray(i: Int, j: Int) = {
    val array = new Array[Array[Double]](i)
    for (index <- 0 until i) {
      array(index) = new Array[Double](j)
    }
    array
  }

  import Math._

  private val LOG_TOLERANCE = 30.0D

  /**
   * log(x1 + x2 + ... + xn)
   * @param inputs An array of numbers [log(x1), ..., log(xn)]
   * @return log(x1 + x2 + ... + xn)
   */
  def logSum(inputs: Array[Double]): Double = {
    val range = 0 until inputs.length
    val maxId = range.reduceLeft((i, j) => if (inputs(i) >= inputs(j)) i else j)
    val max = inputs(maxId)
    val cutoff = max - LOG_TOLERANCE
    val inter = range.foldLeft(0.0D)((intermediate, i) =>
      if (i != maxId && inputs(i) > cutoff)
        intermediate + exp(inputs(i) - max)
      else
        intermediate
    )
    max + log(1 + inter)
  }

  /**
   * log(exp(x1) + exp(x2) + ... + exp(xn))
   * @param inputs An array of numbers [x1, ..., xn]
   * @return log(exp(x1) + exp(x2) + ... + exp(xn))
   */
  def logSumExp(inputs: Array[Double]): Double = {
    inputs.reduceLeft((z, xi) => {
      val max = if (z > xi) z else xi
      val min = if (z > xi) xi else z
      if (max - min > LOG_TOLERANCE)
        max
      else
        max + log(1 + exp(min - max))
    })
  }
}

object CRFModel {

  def build(corpus: CRFCorpus) = {
    val model = new CRFModel(corpus.featureRepository, corpus.labelRepository)

    val func = new CRFDiffFunc(corpus, model)

    model.weights := LBFGS(model.weights).search(func)

    model
  }

  def save(model: CRFModel, file: String) {
    val writer = SerializeHandler(new java.io.File(file), SerializeHandler.WRITE_ONLY)

    FeatureRepository.save(model.featureRepository, writer)
    FeatureRepository.save(model.labelRepository, writer)

    writer.serializeMatrix(model.weights)

    writer.close()
  }

  def apply(file: String) = {
    val reader = SerializeHandler(new java.io.File(file), SerializeHandler.READ_ONLY)

    val featureRepository = FeatureRepository(reader)
    val labelRepository = FeatureRepository(reader)

    val weights = reader.deserializeMatrix()

    reader.close()

    new CRFModel(featureRepository, labelRepository, weights)
  }
}