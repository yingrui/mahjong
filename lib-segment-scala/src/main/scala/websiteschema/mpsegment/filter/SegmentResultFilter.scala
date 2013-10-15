package websiteschema.mpsegment.filter

import ner.{ForeignNameRecognizerCreator, ChineseNameRecognizerCreator}
import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.core.SegmentResult

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
