package websiteschema.mpsegment.crf

import java.util

class FeatureRepository(private val reserveForUnknownFeature: Boolean) {

  private val repo = scala.collection.mutable.HashMap[String, Int]()
  private val defaultFeature = if (reserveForUnknownFeature) 0 else -1

  def getFeatureId(feature: String): Int = repo.getOrElse(feature, defaultFeature)

  def features = repo.keys

  def featureIds = {
    val list = new util.ArrayList[Int]()
    repo.values.foreach(list.add(_))
    list
  }

  def add(feature: String) = if (contains(feature)) getFeatureId(feature) else addFeature(feature)

  def contains(feature: String) = repo.contains(feature)

  def size = if (reserveForUnknownFeature) repo.size + 1 else repo.size
  
  def getLabelFeatureId(lastLabel: Int, label: Int) = getFeatureId("label"+lastLabel+"+"+label)

  private def addFeature(feature: String) = {
    val index = size
    repo += (feature -> index)
    index
  }
}
