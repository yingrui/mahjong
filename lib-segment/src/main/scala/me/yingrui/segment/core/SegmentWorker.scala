package me.yingrui.segment.core

import me.yingrui.segment.conf.SegmentConfiguration
import me.yingrui.segment.dict.{DictionaryFactory, DictionaryService}
import me.yingrui.segment.filter.SegmentResultFilter

import scala.collection.mutable

trait SegmentWorker extends Tokenizer {

  def segment(sen: String): SegmentResult
}

object SegmentWorker {

  private val df = DictionaryFactory()
  df.loadDictionary()
  df.loadDomainDictionary()
  df.loadUserDictionary()
  df.loadEnglishDictionary()
  val dictionaryService = DictionaryService(df.getCoreDictionary, df.getEnglishDictionary, df.getDomainDictionary)

  implicit def javaMapToScalaMap(javaMap: java.util.Map[String, String]) = {
    val map = mutable.HashMap[String, String]()
    val iterator = javaMap.entrySet().iterator()
    while (iterator.hasNext) {
      val entry = iterator.next()
      map += (entry.getKey -> entry.getValue)
    }
    map
  }

  def apply(): SegmentWorker = new MPSegmentWorker(SegmentConfiguration(), dictionaryService)

  def apply(config: java.util.Map[String, String]): SegmentWorker = new MPSegmentWorker(SegmentConfiguration(config), dictionaryService)

  def apply(props: (String, String)*): SegmentWorker = new MPSegmentWorker(SegmentConfiguration(props.toMap), dictionaryService)

  def apply(config: SegmentConfiguration, filter: SegmentResultFilter): SegmentWorker = new MPSegmentWorker(config, filter, dictionaryService)
  def apply(config: SegmentConfiguration): SegmentWorker = new MPSegmentWorker(config, dictionaryService)

}

object SegmentWorkerBuilder {
  def build(config: java.util.Map[String, String]) = SegmentWorker(config)
}
