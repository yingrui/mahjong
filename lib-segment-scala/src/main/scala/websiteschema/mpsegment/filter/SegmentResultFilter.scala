/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package websiteschema.mpsegment.filter

import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.core.SegmentResult

import java.util.ArrayList
import collection.mutable.ListBuffer

class SegmentResultFilter(config: MPSegmentConfiguration) {

  private val filters = ListBuffer[ISegmentFilter]()
  filters += (new UnknownPlaceFilter())
  filters += (new NumberAndTimeFilter())
  filters += (new UnknownNameFilter(config))
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
