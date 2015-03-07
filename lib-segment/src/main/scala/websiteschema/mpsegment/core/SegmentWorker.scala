package websiteschema.mpsegment.core

import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.dict.DictionaryFactory

import scala.collection.mutable
import scala.collection.mutable.Map

trait SegmentWorker {

  def segment(sen: String): SegmentResult

}

object SegmentWorker {

  DictionaryFactory().loadDictionary()
  DictionaryFactory().loadDomainDictionary()
  DictionaryFactory().loadUserDictionary()
  DictionaryFactory().loadEnglishDictionary()

  implicit def javaMapToScalaMap(javaMap: java.util.Map[String, String]) = {
    val map = mutable.HashMap[String, String]()
    val iterator = javaMap.entrySet().iterator()
    while (iterator.hasNext) {
      val entry = iterator.next()
      map += (entry.getKey -> entry.getValue)
    }
    map
  }

  def apply: SegmentWorker = new MPSegmentWorker(MPSegmentConfiguration())

  def apply(config: java.util.Map[String, String]): SegmentWorker = new MPSegmentWorker(MPSegmentConfiguration(config))

  def apply(props: String*): SegmentWorker = {
    var map: Map[String, String] = null
    if (null != props) {
      map = mutable.HashMap[String, String]()
      for (p <- props) {
        val keyAndValue = p.split("(=|->)")
        if (null != keyAndValue && keyAndValue.length == 2) {
          map += (keyAndValue(0).trim() -> keyAndValue(1).trim())
        }
      }
    }
    new MPSegmentWorker(MPSegmentConfiguration(map))
  }
}

object SegmentWorkerBuilder {
  def build(config: java.util.Map[String, String]) = SegmentWorker(config)
}
