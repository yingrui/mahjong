package me.yingrui.segment.crf

import me.yingrui.segment.math.Matrix

import scala.collection.immutable
import scala.collection.mutable._
import scala.io.Source
import scala.collection.Seq

class CRFCorpus(val docs: Array[CRFDocument], val featureRepository: FeatureRepository, val labelRepository: FeatureRepository) {

  val featuresCount = featureRepository.size
  val labelCount = labelRepository.size
  val grouped = {
    val groupSize = if (docs.length > 6) docs.length / 6 else 1
    docs.grouped(groupSize).toArray
  }

  // Ehat
  val occurrence: Matrix = {
    val featureArray = Matrix(featuresCount, labelCount)

    for (doc_i <- docs) {
      for (t <- 0 until doc_i.data.length; k <- doc_i.data(t)) {
        featureArray(k, doc_i.label(t)) += 1.0D
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
        documents += CRFDocument(rowData, withLastLabel, keepOriginData, featureRepository, labelRepository)
        rowData.clear()
      } else {
        rowData += line
      }
    })

    if (!rowData.isEmpty) documents += CRFDocument(rowData, withLastLabel, keepOriginData, featureRepository, labelRepository)

    new CRFCorpus(documents.toArray, featureRepository, labelRepository)
  }
}

case class CRFDocument(val data: Array[Array[Int]], val label: Array[Int], val rowData: Array[String])

object CRFDocument {

  def apply(data: ListBuffer[String], withLastLabel: Boolean, keepOriginData: Boolean, featureRepository: FeatureRepository, labelRepository: FeatureRepository): CRFDocument = {
    val docs = data.map(line => line.split("\\s"))

    createDocument(data, withLastLabel, keepOriginData,
                   featureRepository, labelRepository,
                   docs, (featureRepo, feature) => featureRepo.add(feature))
  }

  def createDocument(data: Seq[String],
                     withLastLabel: Boolean,
                     keepOriginData: Boolean,
                     featureRepository: FeatureRepository,
                     labelRepository: FeatureRepository,
                     docs: Seq[Array[String]],
                     getFeatureId: (FeatureRepository, String) => Int) = {
    val featureAndLabel = for (i <- 0 until docs.length; doc = docs(i)) yield {
      val label = if (doc.size > 1) getFeatureId(labelRepository, doc.last) else labelRepository.defaultFeature
      val word = getFeatureId(featureRepository, doc(0))

      var f: ListBuffer[Int] = new ListBuffer[Int]()
      f += word

      if (i > 0) {
        val lastWord = "p1->" + docs(i - 1)(0)
        val lastWordFeature = getFeatureId(featureRepository, lastWord)

        val biWord = "pc-word->" + docs(i - 1)(0) + "-" + doc(0)
        val biType = "pc-type->" + docs(i - 1)(0) + "-" + doc(0)
        val biWordFeature = getFeatureId(featureRepository, biWord)
        val biTypeFeature = getFeatureId(featureRepository, biType)

        f ++= Array(lastWordFeature, biWordFeature, biTypeFeature)

        if (withLastLabel) {
          // if there are two labels: O, PER. label feature is gonna be: label0, label1
          val labelFeature = "label" + labelRepository.getFeatureId(docs(i - 1).last)
          val labelFeatureId = getFeatureId(featureRepository, labelFeature)

          f += labelFeatureId
        }
      }

      if (i > 1) {
        val lastWord = "p2->" + docs(i - 2)(0)
        val lastWordFeature = getFeatureId(featureRepository, lastWord)
        val triWord = "p2p1c-word->" + docs(i - 2)(0) + "-" + docs(i - 1)(0) + "-" + doc(0)
        val triWordFeature = getFeatureId(featureRepository, triWord)

        f ++= Array(lastWordFeature, triWordFeature)
      }

      if (i < docs.length - 1) {
        val nextWord = "n1->" + docs(i + 1)(0)
        val nextWordFeature = getFeatureId(featureRepository, nextWord)

        val biWord = "cn-word->" + doc(0) + "-" + docs(i + 1)(0)
        val biType = "cn-type->" + doc(0) + "-" + docs(i + 1)(0)
        val biWordFeature = getFeatureId(featureRepository, biWord)
        val biTypeFeature = getFeatureId(featureRepository, biType)

        f ++= Array(nextWordFeature, biWordFeature, biTypeFeature)
      }

      if (i < docs.length - 2) {
        val nextWord = "n2->" + docs(i + 2)(0)
        val nextWordFeature = getFeatureId(featureRepository, nextWord)

        val triWord = "cn1n2-word->" + doc(0) + "-" + docs(i + 1)(0) + "-" + docs(i + 2)(0)
        val triWordFeature = getFeatureId(featureRepository, triWord)
        f ++= Array(nextWordFeature, triWordFeature)
      }

      if (i < docs.length - 1 && i > 0) {
        val triWord = "pcn-word->" + docs(i - 1)(0) + "-" + doc(0) + "-" + docs(i + 1)(0)
        val triWordFeature = getFeatureId(featureRepository, triWord)

        f ++= Array(triWordFeature)
      }

      (f.toArray, label)
    }

    val features = featureAndLabel.map(t => t._1).toArray
    val labels = featureAndLabel.map(t => t._2).toArray
    CRFDocument(features, labels, if (keepOriginData) data.toArray else Array[String]())
  }

  def apply(sen: String, model: CRFModel): CRFDocument = {
    val data: immutable.IndexedSeq[String] = sen.map(ch => ch.toString);
    createDocument(data, false, false,
                   model.featureRepository, model.labelRepository,
                   data.map(word => Array(word)), (featureRepo, feature) => featureRepo.getFeatureId(feature))
  }
}