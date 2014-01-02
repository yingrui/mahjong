package websiteschema.mpsegment.core

import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.filter.SegmentResultFilter
import java.util
import scala.collection.mutable
import scala.collection.mutable.Map
import websiteschema.mpsegment.dict.DictionaryFactory

class SegmentWorker(config: MPSegmentConfiguration) {

  private var unKnownFilter: SegmentResultFilter = null
  private val maxSegStrLength = 400000
  private var mpSegment: MPSegment = null
  private var recognizePOS: Boolean = true

  mpSegment = new MPSegment(config)
  unKnownFilter = new SegmentResultFilter(config)

  def setUseDomainDictionary(flag: Boolean) {
    mpSegment.setUseDomainDictionary(flag)
  }

  def isUseDomainDictionary(): Boolean = {
    mpSegment.isUseDomainDictionary()
  }

  def segment(sen: String): SegmentResult = {
    var sentence = sen
    var result: SegmentResult = null
    if (sentence != null && sentence.length() > 0) {
      if (sentence.length() > maxSegStrLength) {
        sentence = sentence.substring(0, maxSegStrLength)
      }
      result = mpSegment.segmentMP(sentence, recognizePOS)
      if (recognizePOS) {
        unKnownFilter.filter(result)
      }
    } else {
      result = new SegmentResult(0)
    }
    result
  }

  def isUseContextFreqSegment() = mpSegment.isUseContextFreqSegment()

  def setUseContextFreqSegment(useContextFreqSegment: Boolean) {
    mpSegment.setUseContextFreqSegment(useContextFreqSegment)
  }

  def isRecognizePOS() = recognizePOS

  def setRecognizePOS(recognizePOS: Boolean) {
    this.recognizePOS = recognizePOS
  }
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

  def apply = new SegmentWorker(MPSegmentConfiguration())

  def apply(config: java.util.Map[String, String]) = new SegmentWorker(MPSegmentConfiguration(config))

  def apply(props: String*) = {
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
    new SegmentWorker(MPSegmentConfiguration(map))
  }
}