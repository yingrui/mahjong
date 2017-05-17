package me.yingrui.segment.core

import me.yingrui.segment.conf.SegmentConfiguration
import me.yingrui.segment.dict.{DictionaryFactory, DictionaryService, IDictionary}
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

  private val dictionaries = mutable.OpenHashMap[String, IDictionary]()
  dictionaries.put(SegmentConfiguration().getCoreDictionaryFile(), df.getCoreDictionary)

  implicit def javaMapToScalaMap(javaMap: java.util.Map[String, String]) = {
    val map = mutable.HashMap[String, String]()
    val iterator = javaMap.entrySet().iterator()
    while (iterator.hasNext) {
      val entry = iterator.next()
      map += (entry.getKey -> entry.getValue)
    }
    map
  }

  def apply(): SegmentWorker = apply(SegmentConfiguration())

  def apply(config: java.util.Map[String, String]): SegmentWorker = apply(SegmentConfiguration(config))

  def apply(props: (String, String)*): SegmentWorker = apply(SegmentConfiguration(props.toMap))

  def apply(config: SegmentConfiguration): SegmentWorker = {
    val dictionaryService = createDictionaryService(config)
    new MPSegmentWorker(config, dictionaryService)
  }

  def apply(config: SegmentConfiguration, filter: SegmentResultFilter): SegmentWorker = {
    val dictionaryService = createDictionaryService(config)
    new MPSegmentWorker(config, filter, dictionaryService)
  }

  private def createDictionaryService(conf: SegmentConfiguration): DictionaryService = {
    val coreDictionary = getCoreDictionary(conf)
    val domainDictionary = if (conf.isLoadDomainDictionary() || conf.isLoadUserDictionary()) df.getDomainDictionary else null
    val englishDictionary = if (conf.isLoadEnglishDictionary) df.getEnglishDictionary else null
    DictionaryService(coreDictionary, englishDictionary, domainDictionary)
  }

  private def getCoreDictionary(conf: SegmentConfiguration): IDictionary = {
    dictionaries.get(conf.getCoreDictionaryFile()) match {
      case Some(dict) => dict
      case _ => {
        val dict = df.loadDictionary(conf.getCoreDictionaryFile())
        dictionaries.put(conf.getCoreDictionaryFile(), dict)
        dict
      }
    }
  }
}

object SegmentWorkerBuilder {
  def build(config: java.util.Map[String, String]) = SegmentWorker(config)
}
