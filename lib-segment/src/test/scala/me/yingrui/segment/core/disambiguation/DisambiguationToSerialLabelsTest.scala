package me.yingrui.segment.core.disambiguation

import me.yingrui.segment.tools.PFRCorpusLoader._
import me.yingrui.segment.tools.accurary.SegmentResultComparator
import org.scalatest.{FunSuite, Matchers}

class DisambiguationToSerialLabelsTest extends FunSuite with Matchers {

  import DisambiguationToSerialLabels._
  
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

    hooker.serialLabels.map(_._2) should be(List(LABEL_A, LABEL_SB, LABEL_SE, LABEL_A))
  }

  test("should label separated words as SB, SM and SE") {
    val hooker = compareSegmentResult(
      "19980131-04-013-017/m  在/p 半梦半醒/l  之间/f  。/w",
      "19980131-04-013-017/m  在/p 半/m 梦/n 半/m 醒/n  之间/f  。/w"
    )

    hooker.serialLabels.map(_._2) should be(List(LABEL_A, LABEL_SB, LABEL_SM, LABEL_SM, LABEL_SE, LABEL_A, LABEL_A))
  }

  test("should label separated reduplicated words as SB, SM and SE") {
    val hooker = compareSegmentResult(
      "19980131-04-013-017/m  有/p 一批批/m",
      "19980131-04-013-017/m  在/p 一/q 批/m 批/m"
    )

    hooker.serialLabels.map(_._2) should be(List(LABEL_A, LABEL_SB, LABEL_SM, LABEL_SE))
  }

  test("when the word's last character belongs to next word then label it as LC") {
    val hooker = compareSegmentResult(
      "19980101-01-003-002/m  精神/n 病人/n",
      "19980101-01-003-002/m  精神病/n 人/n"
    )

    hooker.serialLabels.map(_._2) should be(List(LABEL_LC, LABEL_LL))
  }

  test("when the previous word's last character belongs to this word then label it as LL") {
    val hooker = compareSegmentResult(
      "19980101-01-003-002/m  瓦楞/n 纸板箱包/n",
      "19980101-01-003-002/m  瓦楞纸/n 板/n 箱/n 包/n"
    )

    hooker.serialLabels.map(_._2) should be(List(LABEL_LC, LABEL_LL, LABEL_LL, LABEL_LL))
  }

  test("when two single character words be put into one word then label it as U") {
    val hooker = compareSegmentResult(
      "19980101-01-003-002/m  很/d 美/a 的/u",
      "19980101-01-003-002/m  很/d 美的/nt"
    )

    hooker.serialLabels.map(_._2) should be(List(LABEL_A, LABEL_U))
  }

  test("when two words be put into one word then label it as U") {
    val hooker = compareSegmentResult(
      "19980101-01-003-002/m  中国/NS 跨/V 世纪/N 发展/V 的/U 行动/VN 纲领/N",
      "19980101-01-003-002/m  中国/NS 跨世纪/VN 发展/V 的/U 行动/VN 纲领/N"
    )

    hooker.serialLabels.map(_._2) should be(List(LABEL_A, LABEL_U, LABEL_A, LABEL_A, LABEL_A, LABEL_A))
  }

  test("when two double characters words be put into one word then label it as U") {
    val hooker = compareSegmentResult(
      "19980101-01-003-002/m  参与/V 亚/J 太/J 经合/J 组织/N 的/U 活动/VN",
      "19980101-01-003-002/m  参与/V 亚/J 太/J 经合组织/N 的/U 活动/VN"
    )

    hooker.serialLabels.map(_._2) should be(List(LABEL_A, LABEL_A, LABEL_A, LABEL_U, LABEL_A, LABEL_A))
  }

  test("when three single character words be put into one word then label it as UT") {
    val hooker = compareSegmentResult(
      "19980101-01-003-002/m  离/V 不/D 开/V 精神文明/N 建设/VN",
      "19980101-01-003-002/m  离不开/V 精神文明/N 建设/VN"
    )

    hooker.serialLabels.map(_._2) should be(List(LABEL_UT, LABEL_A, LABEL_A))
  }

  test("when two single character words should be separate and join the previous and next word then label it as SH") {
    val hooker = compareSegmentResult(
      "19980101-01-003-002/m  神经病变/n 成为/v",
      "19980101-01-003-002/m  神经病/d 变成/v 为/d"
    )

    hooker.serialLabels.map(_._2) should be(List(LABEL_SB, LABEL_SH, LABEL_SE))
  }

  test("when two single character words should be separate and first character belongs tto previous label it as FL") {
    val hooker = compareSegmentResult(
      "19980101-01-003-002/m  起来/v 了/u",
      "19980101-01-003-002/m  起/v 来了/v"
    )

    hooker.serialLabels.map(_._2) should be(List(LABEL_SB, LABEL_FL))
  }

  test("when first character belongs to previous word then label it as FL") {
    val hooker = compareSegmentResult(
      "19980101-01-003-002/m  济南市/n 政府/n",
      "19980101-01-003-002/m  济南/n 市政府/n"
    )

    hooker.serialLabels.map(_._2) should be(List(LABEL_SB, LABEL_FL))
  }

  test("when all words match then label them as A") {
    val hooker = compareSegmentResult(
      "19980101-01-003-002/m  建筑/n  面积/n  ４２４８０/m  平方米/q",
      "19980101-01-003-002/m  建筑/n  面积/n  ４２４８０/m  平方米/q"
    )

    hooker.serialLabels.map(_._2) should be(List(LABEL_A, LABEL_A, LABEL_A, LABEL_A))
  }
}
