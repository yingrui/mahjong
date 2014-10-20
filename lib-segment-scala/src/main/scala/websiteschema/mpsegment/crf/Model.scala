package websiteschema.mpsegment.crf

class CRFModel {

  val windowSize = 1
  val classesCount = 2
  val featuresCount = 26
  val weights: Array[Double] = new Array[Double](featuresCount * classesCount)
  val tolerance = 1.0E-4

  def getClassesCount(feature: Int) = classesCount
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

case class CRFDocument(val data: Array[Array[Array[Int]]], val label: Array[Int])

class CRFCorpus(val docs: Array[CRFDocument], model: CRFModel) {

  def getFeatureOccurrence: Array[Array[Double]] = {
    val featureArray = CRFUtils.empty2DArray(model.featuresCount, model.classesCount)

    for(doc_i <- docs) {
      for(t <- 0 until doc_i.data.length; w <- doc_i.data(t); k <- w) {
        featureArray(k)(doc_i.label(t)) += 1.0D
      }
    }

    featureArray
  }

}

object CRFModel {

  def build(corpus: CRFCorpus) = {
    val model = new CRFModel

    model
  }
}

class CRFLogFunction(docs: Array[CRFDocument]) {



  def evaluate {

  }
}