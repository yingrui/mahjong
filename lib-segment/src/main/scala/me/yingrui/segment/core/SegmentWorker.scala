package me.yingrui.segment.core

import me.yingrui.segment.conf.MPSegmentConfiguration
import me.yingrui.segment.dict.DictionaryFactory
import me.yingrui.segment.filter.SegmentResultFilter

import scala.collection.mutable

trait SegmentWorker extends Tokenizer {

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

  def apply(): SegmentWorker = new MPSegmentWorker(MPSegmentConfiguration())

  def apply(config: java.util.Map[String, String]): SegmentWorker = new MPSegmentWorker(MPSegmentConfiguration(config))

  def apply(props: (String, String)*): SegmentWorker = new MPSegmentWorker(MPSegmentConfiguration(props.toMap))

  def apply(config: MPSegmentConfiguration, filter: SegmentResultFilter): SegmentWorker = new MPSegmentWorker(config, filter)

}

object SegmentWorkerBuilder {
  def build(config: java.util.Map[String, String]) = SegmentWorker(config)
}
