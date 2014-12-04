package websiteschema.mpsegment.crf

import scala.io.Source
import scala.collection.mutable._

case class CRFDocument(val data: Array[Array[Int]], val label: Array[Int])

class CRFCorpus(val docs: Array[CRFDocument], val featuresCount: Int, val labelCount: Int, val featureRepository: FeatureRepository, val labelRepository: FeatureRepository) {

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

  def apply(file: String): CRFCorpus = {
    val featureRepository = new FeatureRepository(true)
    val labelRepository = new FeatureRepository(false)

    val documents = ListBuffer[CRFDocument]()
    val rowData = ListBuffer[String]()
    Source.fromFile(file).getLines().foreach(line => {
      if (line.trim().isEmpty) {
        documents += createDocument(rowData, featureRepository, labelRepository)
        rowData.clear()
      } else {
        rowData += line
      }
    })

    if (!rowData.isEmpty) documents += createDocument(rowData, featureRepository, labelRepository)

    addLabelFeaturesIfNotExists(featureRepository, labelRepository)

    new CRFCorpus(documents.toArray, featureRepository.size, labelRepository.size, featureRepository, labelRepository)
  }

  def addLabelFeaturesIfNotExists(featureRepository: FeatureRepository, labelRepository: FeatureRepository) {
    for (lastLabel <- labelRepository.features; label <- labelRepository.features) {
      val feature = "label" + labelRepository.getFeatureId(lastLabel) + "+" + labelRepository.getFeatureId(label)
      featureRepository.add(feature)
    }
  }

  private def createDocument(data: ListBuffer[String], featureRepository: FeatureRepository, labelRepository: FeatureRepository): CRFDocument = {
    val docs = data.map(line => line.split("\\s"))

    val featureAndLabel = for (i <- 0 until docs.length; doc = docs(i)) yield {
      val label = labelRepository.add(doc.last)
      val word = featureRepository.add(doc(0))

      if (i > 0) {
        // if there are two labels: O, PER. label feature is gonna be: label0+1, label0+0, label1+1
        val labelFeature = "label" + labelRepository.getFeatureId(docs(i - 1).last) + "+" + labelRepository.getFeatureId(doc.last)
        val labelFeatureId = featureRepository.add(labelFeature)
        (Array(word, labelFeatureId), label)
      } else {
        (Array(word), label)
      }
    }


    val features = featureAndLabel.map(t => t._1).toArray
    val labels = featureAndLabel.map(t => t._2).toArray
    CRFDocument(features, labels)
  }
}
