package me.yingrui.segment.core.disambiguation

import me.yingrui.segment.core.{SegmentResult, Word}
import me.yingrui.segment.tools.accurary.SegmentResultCompareHook

class DisambiguationToSerialLabels(expect: SegmentResult, actual: SegmentResult) extends SegmentResultCompareHook {

  var serialLabels = List[(String, String)]()

  override def compeleted: Unit = {

  }

  override def foundCorrectWordHook(expectWord: Word, actualWord: Word, expectWordIndex: Int, actualWordIndex: Int): Unit = {
    add(expectWord.name, "A")
  }

  override def foundError(expectWord: Word, actualWord: Word, expectWordIndex: Int, actualWordIndex: Int): Unit = {
    add(actualWord.name, "S")
  }

  private def add(word: String, label: String): Unit = {
    serialLabels = serialLabels :+ (word, label)
  }
}
