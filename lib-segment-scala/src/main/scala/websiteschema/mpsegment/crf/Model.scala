package websiteschema.mpsegment.crf

class CRFModel {

  val windowSize = 1
  val labelCount = 2
  val featuresCount = 26 + 2 ^ labelCount
  def getLabelFeature(labels: Array[Int]) = labels match {
    case Array(0, 0) => 26
    case Array(0, 1) => 27
    case Array(1, 0) => 28
    case Array(1, 1) => 29
    case _ => throw new RuntimeException
  }
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

  /*
   *  log(exp(lx) + exp(ly))
   */
  def logAdd(lx: Double, ly: Double) = {

    var max: Double = 0D
    var negDiff: Double = 0D
    if (lx > ly) {
      max = lx
      negDiff = ly - lx
    } else {
      max = ly
      negDiff = lx - ly
    }
    if (max == Double.NegativeInfinity) {
      max
    } else if (negDiff < -30.0) {
      max
    } else {
      max + Math.log(1.0 + Math.exp(negDiff))
    }
  }

}

object CRFModel {

  def build(corpus: CRFCorpus) = {
    val model = new CRFModel

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