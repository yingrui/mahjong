package websiteschema.mpsegment.crf

import scala.io.Source
import scala.collection.mutable._

case class CRFDocument(val data: Array[Array[Int]], val label: Array[Int], val rowData: Array[String])

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
    apply(file, withLastLabel, false, featureRepository, labelRepository)
  }

  def apply(file: String, withLastLabel: Boolean, keepOriginData: Boolean, featureRepository: FeatureRepository, labelRepository: FeatureRepository): CRFCorpus = {

    val documents = ListBuffer[CRFDocument]()
    val rowData = ListBuffer[String]()
    Source.fromFile(file).getLines().foreach(line => {
      if (line.trim().isEmpty) {
        documents += createDocument(rowData, withLastLabel, keepOriginData, featureRepository, labelRepository)
        rowData.clear()
      } else {
        rowData += line
      }
    })

    if (!rowData.isEmpty) documents += createDocument(rowData, withLastLabel, keepOriginData, featureRepository, labelRepository)

    new CRFCorpus(documents.toArray, featureRepository, labelRepository)
  }

  private def createDocument(data: ListBuffer[String], withLastLabel: Boolean, keepOriginData: Boolean, featureRepository: FeatureRepository, labelRepository: FeatureRepository): CRFDocument = {
    val docs = data.map(line => line.split("\\s"))

    val featureAndLabel = for (i <- 0 until docs.length; doc = docs(i)) yield {
      val label = labelRepository.add(doc.last)
      val word = featureRepository.add(doc(0))

      var f: ListBuffer[Int] = new ListBuffer[Int]()
      f += word

      if (i > 0) {
        val lastWord = "p1->" + docs(i - 1)(0)
        val lastWordFeature = featureRepository.add(lastWord)

        val biWord = "pc-word->" + docs(i - 1)(0) + "-" + doc(0)
        val biWordFeature = featureRepository.add(biWord)

        f ++= Array(lastWordFeature, biWordFeature)

        if (withLastLabel) {
          // if there are two labels: O, PER. label feature is gonna be: label0, label1
          val labelFeature = "label" + labelRepository.getFeatureId(docs(i - 1).last)
          val labelFeatureId = featureRepository.add(labelFeature)

          f += labelFeatureId
        }
      }

      if (i > 1) {
        val lastWord = "p2->" + docs(i - 2)(0)
        val lastWordFeature = featureRepository.add(lastWord)
        val triWord = "p2p1c-word->" + docs(i - 2)(0) + "-" + docs(i - 1)(0) + "-" + doc(0)
        val triWordFeature = featureRepository.add(triWord)

        f ++= Array(lastWordFeature, triWordFeature)
      }

      if (i < docs.length - 1) {
        val nextWord = "n1->" + docs(i + 1)(0)
        val nextWordFeature = featureRepository.add(nextWord)

        val biWord = "cn-word->" + doc(0) + "-" + docs(i + 1)(0)
        val biWordFeature = featureRepository.add(biWord)

        f ++= Array(nextWordFeature, biWordFeature)
      }

      if (i < docs.length - 2) {
        val nextWord = "n2->" + docs(i + 2)(0)
        val nextWordFeature = featureRepository.add(nextWord)

        val triWord = "cn1n2-word->" + doc(0) + "-" + docs(i + 1)(0) + "-" + docs(i + 2)(0)
        val triWordFeature = featureRepository.add(triWord)
        f ++= Array(nextWordFeature, triWordFeature)
      }

      if (i < docs.length - 1 && i > 0) {
        val triWord = "pcn-word->" + docs(i - 1)(0) + "-" + doc(0) + "-" + docs(i + 1)(0)
        val triWordFeature = featureRepository.add(triWord)

        f ++= Array(triWordFeature)
      }

      (f.toArray, label)
    }

    val features = featureAndLabel.map(t => t._1).toArray
    val labels = featureAndLabel.map(t => t._2).toArray
    CRFDocument(features, labels, if(keepOriginData) data.toArray else Array[String]())
  }
}
