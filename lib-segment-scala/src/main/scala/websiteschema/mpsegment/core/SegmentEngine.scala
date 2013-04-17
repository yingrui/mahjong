package websiteschema.mpsegment.core

import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.dict.DictionaryFactory

import collection.mutable.{Map, HashMap}

class SegmentEngine {

  private val configuration = MPSegmentConfiguration()

  implicit def javaMapToScalaMap(javaMap: java.util.Map[String, String]) = {
    val map = HashMap[String, String]()
    val iterator = javaMap.entrySet().iterator()
    while (iterator.hasNext) {
      val entry = iterator.next()
      map += (entry.getKey -> entry.getValue)
    }
    map
  }

  private def loadDictionary() {
    DictionaryFactory().loadDictionary()
    DictionaryFactory().loadDomainDictionary()
    DictionaryFactory().loadUserDictionary()
    DictionaryFactory().loadEnglishDictionary()
  }

  def getSegmentWorker(): SegmentWorker = {
    return new SegmentWorker(configuration)
  }

  def getSegmentWorker(config: java.util.Map[String, String]): SegmentWorker = {
    return new SegmentWorker(MPSegmentConfiguration(config))
  }

  def getSegmentWorker(props: String*): SegmentWorker = {
    var map: Map[String, String] = null
    if (null != props) {
      map = HashMap[String, String]()
      for (p <- props)
      {
        val keyAndValue = p.split("(=|->)")
        if (null != keyAndValue && keyAndValue.length == 2) {
          map += (keyAndValue(0).trim() -> keyAndValue(1).trim())
        }
      }
    }
    return new SegmentWorker(MPSegmentConfiguration(map))
  }
}

object SegmentEngine {
  val instance = new SegmentEngine()
  instance.loadDictionary()
  def apply() = instance
}
