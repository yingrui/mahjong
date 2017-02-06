package me.yingrui.segment.core.disambiguation

import me.yingrui.segment.tools.PFRCorpusLoader._
import me.yingrui.segment.tools.accurary.SegmentResultComparator
import org.scalatest.{FunSuite, Matchers}

class DisambiguationToSerialLabelsTest extends FunSuite with Matchers {

  def compareSegmentResult(expectedSegmentResult: String, actualSegmentResult: String): DisambiguationToSerialLabels = {
    val expect = convertToSegmentResult(expectedSegmentResult)
    val actual = convertToSegmentResult(actualSegmentResult)

    val hooker = new DisambiguationToSerialLabels(expect, actual)
    new SegmentResultComparator(hooker).compare(expect, actual)
    hooker
  }

  test("should label separated words as SB and SE") {
    val hooker = compareSegmentResult(
      "19980101-01-003-002/m  保护/v 人体/n 健康/n",
      "19980101-01-003-002/m  保护/v 人/n 体/j 健康/n"
    )

    hooker.serialLabels.size should be(4)
    hooker.serialLabels.map(_._2) should be(List("A", "SB", "SE", "A"))
  }

  test("when the word's last character belongs to next word then label it as LC") {
    val hooker = compareSegmentResult(
      "19980101-01-003-002/m  精神/n 病人/n",
      "19980101-01-003-002/m  精神病/n 人/n"
    )

    hooker.serialLabels.size should be(2)
    hooker.serialLabels.map(_._2) should be(List("LC", "LL"))
  }

  test("when two single character words be put into one word then label it as U") {
    val hooker = compareSegmentResult(
      "19980101-01-003-002/m  很/d 美/a 的/u",
      "19980101-01-003-002/m  很/d 美的/nt"
    )

    hooker.serialLabels.size should be(2)
    hooker.serialLabels.map(_._2) should be(List("A", "U"))
  }

  test("when two single character words should be separate and join the previous and next word then label it as SH") {
    val hooker = compareSegmentResult(
      "19980101-01-003-002/m  神经病变/n 成为/v",
      "19980101-01-003-002/m  神经病/d 变成/v 为/d"
    )

    hooker.serialLabels.size should be(3)
    hooker.serialLabels.map(_._2) should be(List("SB", "SH", "SE"))
  }
}
