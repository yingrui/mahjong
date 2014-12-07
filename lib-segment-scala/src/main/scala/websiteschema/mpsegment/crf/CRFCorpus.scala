package websiteschema.mpsegment.crf

import scala.io.Source
import scala.collection.mutable._

case class CRFDocument(val data: Array[Array[Int]], val label: Array[Int])

class CRFCorpus(val docs: Array[CRFDocument], val featureRepository: FeatureRepository, val labelRepository: FeatureRepository) {

  val featuresCount = featureRepository.size
  val labelCount = labelRepository.size

  val Ehat: Array[Array[Double]] = {
    val featureArray = CRFUtils.empty2DArray(featuresCount, labelCount)

    for (doc_i <- docs) {
      for (t <- 0 until doc_i.data.length; k <- doc_i.data(t)) {
        featureArray(k)(doc_i.label(t)) += 1.0D
      }
    }
    featureArray
  }
}

object CRFCorpus {

  def apply(file: String): CRFCorpus = apply(file, true)

  def apply(file: String, withLastLabel: Boolean): CRFCorpus = {
    val featureRepository = new FeatureRepository(true)
    val labelRepository = new FeatureRepository(false)
    apply(file, withLastLabel, featureRepository, labelRepository)
  }

  def apply(file: String, withLastLabel: Boolean, featureRepository: FeatureRepository, labelRepository: FeatureRepository): CRFCorpus = {

    val documents = ListBuffer[CRFDocument]()
    val rowData = ListBuffer[String]()
    Source.fromFile(file).getLines().foreach(line => {
      if (line.trim().isEmpty) {
        documents += createDocument(rowData, withLastLabel, featureRepository, labelRepository)
        rowData.clear()
      } else {
        rowData += line
      }
    })

    if (!rowData.isEmpty) documents += createDocument(rowData, withLastLabel, featureRepository, labelRepository)

    new CRFCorpus(documents.toArray, featureRepository, labelRepository)
  }

  private def createDocument(data: ListBuffer[String], withLastLabel: Boolean, featureRepository: FeatureRepository, labelRepository: FeatureRepository): CRFDocument = {
    val docs = data.map(line => line.split("\\s"))

    val featureAndLabel = for (i <- 0 until docs.length; doc = docs(i)) yield {
      val label = labelRepository.add(doc.last)
      val word = featureRepository.add(doc(0))

      if (i > 0) {
        // if there are two labels: O, PER. label feature is gonna be: label0, label1
        val labelFeature = "label" + labelRepository.getFeatureId(docs(i - 1).last)
        val labelFeatureId = featureRepository.add(labelFeature)

        val lastWord = "n-1->" + labelRepository.getFeatureId(docs(i - 1)(0))
        val lastWordFeature = featureRepository.add(lastWord)

        if (withLastLabel) {
          (Array(word, lastWordFeature, labelFeatureId), label)
        } else {
          (Array(word, lastWordFeature), label)
        }
      } else {
        (Array(word), label)
      }
    }


    val features = featureAndLabel.map(t => t._1).toArray
    val labels = featureAndLabel.map(t => t._2).toArray
    CRFDocument(features, labels)
  }
}
