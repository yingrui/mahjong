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

  test("should merge two separated words") {
    val segmentResult = convertToSegmentResult("19980101-01-003-002/m  保护/v 人/n 体/j 健康/n")
    val labels = Array(LABEL_A, LABEL_SB, LABEL_SE, LABEL_A)

    createFilter(segmentResult, labels).filtering()

    segmentResult.map(_.name) should be(Array("保护", "人体", "健康"))
    segmentResult.map(_.pos) should be(Array(POS_V, POS_N, POS_N))
  }

  test("should merge separated words") {
    val segmentResult = convertToSegmentResult("19980101-01-003-002/m  在/p 半/m 梦/n 半/m 醒/n  之间/f")
    val labels = Array(LABEL_A, LABEL_SB, LABEL_SM, LABEL_SM, LABEL_SE, LABEL_A)

    createFilter(segmentResult, labels).filtering()

    segmentResult.map(_.name) should be(Array("在", "半梦半醒", "之间"))
    segmentResult.map(_.pos) should be(Array(POS_P, POS_M, POS_F))
  }

  test("should move last character to next words") {
    val segmentResult = convertToSegmentResult("19980101-01-003-002/m  精神病/n 人/n")
    val labels = Array(LABEL_LC, LABEL_LL)

    createFilter(segmentResult, labels).filtering()

    segmentResult.map(_.name) should be(Array("精神", "病人"))
    segmentResult.map(_.pos) should be(Array(POS_N, POS_N))
  }

  private def createFilter(segmentResult: SegmentResult, labels: Array[String]): CRFDisambiguationFilter = {
    val classifier = mock[CRFClassifier]
    when(classifier.findBestLabels(any())).thenReturn(labels)
    val filter = new CRFDisambiguationFilter(classifier)
    filter.setSegmentResult(segmentResult)
    filter
  }
}
