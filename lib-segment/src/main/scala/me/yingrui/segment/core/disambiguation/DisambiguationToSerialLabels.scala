package me.yingrui.segment.core.disambiguation

import me.yingrui.segment.core.{SegmentResult, Word}
import me.yingrui.segment.tools.accurary.SegmentResultCompareHook

/**
  * S 一个词属于正确的词的一部分 (Separated)
  * LC 一个词的最后一个字是下一个词的首字 (Last Character)
  * LL 上一个词的最后一个字，应当属于当前词 (Last word's Last character)
  * @param expect
  * @param actual
  */
class DisambiguationToSerialLabels(expect: SegmentResult, actual: SegmentResult) extends SegmentResultCompareHook {

  var serialLabels = List[(String, String)]()

  override def compeleted: Unit = {

  }

  override def foundCorrectWordHook(expectWord: Word, actualWord: Word, expectWordIndex: Int, actualWordIndex: Int): Unit = {
    add(expectWord.name, "A")
  }

  override def foundError(expectWord: Word, actualWord: Word, expectWordIndex: Int, actualWordIndex: Int): Unit = {
    if (isExpectWordContainsActualWord(expectWord, actualWord)) {
      if (isLastWordLastCharacterBelongToThisWord(expectWord, actualWord)) {
        add(actualWord.name, "LL")
      } else {
        add(actualWord.name, "S")
      }
    } else {
      if (isActualWordContainsExpectedWord(expectWord, actualWord) && isLastCharacterBelongToNextWord(expectWord, actualWord)) {
        add(actualWord.name, "LC")
      }
    }

  }

  def isLastWordLastCharacterBelongToThisWord(expectWord: Word, actualWord: Word): Boolean = {
    serialLabels.last._2 == "LC" && expectWord.name.endsWith(actualWord.name)
  }

  def isLastCharacterBelongToNextWord(expectWord: Word, actualWord: Word): Boolean = {
    actualWord.name.startsWith(expectWord.name) && actualWord.length == expectWord.length + 1
  }

  def isActualWordContainsExpectedWord(expectWord: Word, actualWord: Word): Boolean = {
    actualWord.name.contains(expectWord.name)
  }

  def isExpectWordContainsActualWord(expectWord: Word, actualWord: Word): Boolean = {
    expectWord.name.contains(actualWord.name)
  }

  private def add(word: String, label: String): Unit = {
    serialLabels = serialLabels :+ (word, label)
  }
}
