package me.yingrui.segment.core.disambiguation

import me.yingrui.segment.core.{SegmentResult, Word}
import me.yingrui.segment.tools.accurary.SegmentResultCompareHook

/**
  * SB 一个词属于正确的词的开始 (Separated word Beginning part)
  * SE 一个词属于正确的词的开始 (Separated word Ending part)
  * SH 一个双字词，应该分为两部分，并且将两个字分别加入前后两个词 (Split Half)
  * LC 一个词的最后一个字是下一个词的首字 (Last Character)
  * LL 上一个词的最后一个字，应当属于当前词 (Last word's Last character)
  * U  当前词由两个单字词组成 (Union Words)
  * A  默认标记
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
    addLabel(expectWord, actualWord, expectWordIndex, actualWordIndex)
  }

  def addLabel(expectWord: Word, actualWord: Word, expectWordIndex: Int, actualWordIndex: Int): String = {
    if (isExpectWordContainsActualWord(expectWord, actualWord)) {
      if (isLastWordLastCharacterBelongToThisWord(expectWord, actualWord)) {
        add(actualWord.name, "LL")
      } else if (expectWord.name.startsWith(actualWord.name)) {
        add(actualWord.name, "SB")
      } else if (expectWord.name.endsWith(actualWord.name)) {
        add(actualWord.name, "SE")
      } else {
        add(actualWord.name, "A")
      }
    } else {
      if (isActualWordStartsWithExpectedWordAndLastCharacterBelongsToNextWord(expectWord, actualWord, expectWordIndex)) {
        add(actualWord.name, "LC")
      } else if (isTwoCharactersWord(actualWord) && isActualWordComposedOfTwoExpectedWords(expectWord, actualWord, expectWordIndex)) {
        add(actualWord.name, "U")
      } else if (isTwoCharactersWord(actualWord) && shouldActualWordSeparateToJoinPreviousAndNextWords(expectWord, actualWord, expectWordIndex)) {
        add(actualWord.name, "SH")
      } else {
        add(actualWord.name, "A")
      }
    }
  }

  def shouldActualWordSeparateToJoinPreviousAndNextWords(expectWord: Word, actualWord: Word, expectWordIndex: Int): Boolean = {
    isNotLastWord(expectWordIndex) && expectWord.name.endsWith(actualWord.name.substring(0, 1)) && getNextExpectWord(expectWordIndex).startsWith(actualWord.name.substring(1))
  }

  private def isTwoCharactersWord(actualWord: Word): Boolean = {
    actualWord.length == 2
  }

  private def isActualWordComposedOfTwoExpectedWords(expectWord: Word, actualWord: Word, expectWordIndex: Int): Boolean = {
    isNotLastWord(expectWordIndex) && actualWord.name == (expectWord.name + getNextExpectWord(expectWordIndex))
  }

  private def isNotLastWord(expectWordIndex: Int): Boolean = expectWordIndex + 1 < expect.length()

  private def getNextExpectWord(index: Int): String = expect.getWord(index + 1)

  private def isActualWordStartsWithExpectedWordAndLastCharacterBelongsToNextWord(expectWord: Word, actualWord: Word, expectWordIndex: Int): Boolean = {
    isNotLastWord(expectWordIndex) && isActualWordContainsExpectedWord(expectWord, actualWord) && isLastCharacterBelongToNextWord(expectWord, actualWord) && getNextExpectWord(expectWordIndex).length > 1
  }

  private def isLastWordLastCharacterBelongToThisWord(expectWord: Word, actualWord: Word): Boolean = {
    isLastLabel("LC") && expectWord.name.endsWith(actualWord.name)
  }

  def isLastLabel(label: String): Boolean = {
    serialLabels.length > 0 && serialLabels.last._2 == label
  }

  private def isLastCharacterBelongToNextWord(expectWord: Word, actualWord: Word): Boolean = {
    actualWord.name.startsWith(expectWord.name) && actualWord.length == expectWord.length + 1
  }

  private def isActualWordContainsExpectedWord(expectWord: Word, actualWord: Word): Boolean = {
    actualWord.name.contains(expectWord.name)
  }

  private def isExpectWordContainsActualWord(expectWord: Word, actualWord: Word): Boolean = {
    expectWord.name.contains(actualWord.name)
  }

  private def add(word: String, label: String): String = {
    serialLabels = serialLabels :+ (word, label)
    label
  }
}
