package websiteschema.mpsegment.crf

import java.util

import websiteschema.mpsegment.util.SerializeHandler

class FeatureRepository(private val reserveUnknownFeature: Boolean, private val repo: scala.collection.mutable.Map[String, Int]) {

  def this(unknownFeature: Boolean) = this(unknownFeature, scala.collection.mutable.Map[String, Int]())

  private val defaultFeature = if (reserveUnknownFeature) 0 else -1

  def getFeatureId(feature: String): Int = repo.getOrElse(feature, defaultFeature)

  def features = repo.keys

  def getFeature(id: Int) = {
    repo.find(t => t._2 == id) match {
      case Some((feature, i)) => feature
      case _ => ""
    }
  }

  def featureIds = {
    val list = new util.ArrayList[Int]()
    repo.values.foreach(list.add(_))
    list
  }

  def add(feature: String) = if (contains(feature)) getFeatureId(feature) else addFeature(feature)

  def contains(feature: String) = repo.contains(feature)

  def size = if (reserveUnknownFeature) repo.size + 1 else repo.size

  def getLabelFeatureId(lastLabel: Int) = getFeatureId("label" + lastLabel)

  private def addFeature(feature: String) = {
    val index = size
    repo += (feature -> index)
    index
  }
}

object FeatureRepository {

  def apply(reader: SerializeHandler) = {
    val unknownFeature = reader.deserializeInt() > 0
    val repo = reader.deserializeScalaMapStringInt()

    new FeatureRepository(unknownFeature, repo)
  }


  def save(featureRepository: FeatureRepository, writer: SerializeHandler) {
    writer.serializeInt(if(featureRepository.reserveUnknownFeature) 1 else 0)
    writer.serializeMapStringInt(featureRepository.repo)
  }
}