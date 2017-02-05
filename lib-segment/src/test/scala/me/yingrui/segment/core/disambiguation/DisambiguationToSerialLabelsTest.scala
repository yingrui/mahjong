package me.yingrui.segment.core.disambiguation

import me.yingrui.segment.tools.PFRCorpusLoader._
import me.yingrui.segment.tools.accurary.SegmentResultComparator
import org.scalatest.{FunSuite, Matchers}

class DisambiguationToSerialLabelsTest extends FunSuite with Matchers {

  test("should label separated words as S") {
    val expect = convertToSegmentResult("19980101-01-003-002/m  保护/v 人体/n 健康/n")
    val actual = convertToSegmentResult("19980101-01-003-002/m  保护/v 人/n 体/j 健康/n")

    val hooker = new DisambiguationToSerialLabels(expect, actual)
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    hooker.serialLabels.size should be (4)
    hooker.serialLabels(1)._2 should be ("S")
    hooker.serialLabels(2)._2 should be ("S")
  }

  test("when the word's last character belongs to next word then label it as LC") {
    val expect = convertToSegmentResult("19980101-01-003-002/m  精神/n 病人/n")
    val actual = convertToSegmentResult("19980101-01-003-002/m  精神病/n 人/n")

    val hooker = new DisambiguationToSerialLabels(expect, actual)
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    hooker.serialLabels.size should be (2)
    hooker.serialLabels(0)._2 should be ("LC")
    hooker.serialLabels(1)._2 should be ("LL")
  }
}
