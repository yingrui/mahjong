package me.yingrui.segment.filter.disambiguation

import me.yingrui.segment.tools.PFRCorpusLoader._
import org.scalatest.{FunSuite, Ignore}

@Ignore
class CRFDisambiguationFilterTest extends FunSuite {

  test("should separate union words") {
    val segmentResult = convertToSegmentResult("19980101-01-003-002/m  很/d 美的/nt")
  }

}
