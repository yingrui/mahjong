package me.yingrui.segment.filter

import ner.{HmmNameFilter, ForeignNameRecognizerCreator, ChineseNameRecognizerCreator}
import me.yingrui.segment.conf.MPSegmentConfiguration
import me.yingrui.segment.core.SegmentResult

import collection.mutable.ListBuffer

class SegmentResultFilter(config: MPSegmentConfiguration) {

  private val filters = ListBuffer[ISegmentFilter]()
  filters += (new UnknownPlaceFilter())
  filters += (new NumberAndTimeFilter())

  if (config.isChineseNameIdentify()) {
    if (config.getNameRecognizer == "UnknownNameFilter")
      filters += (new UnknownNameFilter(config))
    if (config.getNameRecognizer == "ChineseNameFilter")
      filters += (new ChineseNameFilter(config, new ChineseNameRecognizerCreator(), 10))
    if (config.getNameRecognizer == "HmmNameFilter")
      filters += HmmNameFilter(config)
  }

  filters += (new ReduplicatingFilter())
  filters += (new QuerySyntaxFilter(config))

  if (config.is("segment.lang.en")) {
    filters += (new EnglishStemFilter(config.is("segment.lang.en.stemming")))
  }

  if (config.isHalfShapeAll() || config.isUpperCaseAll()) {
    filters += (new UpperCaseAndHalfShapeFilter(config.isHalfShapeAll(), config.isUpperCaseAll()))
  }

  def filter(segmentResult: SegmentResult) {
    for (filter <- filters) {
      filter.setSegmentResult(segmentResult)
      filter.filtering()
    }
  }
}
