package me.yingrui.segment.filter.disambiguation

import me.yingrui.segment.core.SegmentResult
import me.yingrui.segment.core.disambiguation.DisambiguationToSerialLabels._
import me.yingrui.segment.crf.CRFClassifier
import me.yingrui.segment.dict.POSUtil._
import me.yingrui.segment.tools.PFRCorpusLoader._
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FunSuite, Matchers}

class CRFDisambiguationFilterTest extends FunSuite with Matchers with MockitoSugar {

  test("should separate union words") {
    val segmentResult = convertToSegmentResult("19980101-01-003-002/m  很/d 美的/nt")
    val labels = Array(LABEL_A, LABEL_U)

    createFilter(segmentResult, labels).filtering()

    segmentResult.map(_.name) should be(Array("很", "美", "的"))
    segmentResult.map(_.pos) should be(Array(POS_D, POS_NT, POS_NT))
  }

  private def createFilter(segmentResult: SegmentResult, labels: Array[String]): CRFDisambiguationFilter = {
    val classifier = mock[CRFClassifier]
    when(classifier.findBestLabels(any())).thenReturn(labels)
    val filter = new CRFDisambiguationFilter(classifier)
    filter.setSegmentResult(segmentResult)
    filter
  }
}
