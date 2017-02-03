package me.yingrui.segment.tools.ner

import me.yingrui.segment.tools.accurary.SegmentResultCompareHook
import me.yingrui.segment.core.{Word, SegmentResult}
import me.yingrui.segment.filter.ner.NameProbDistribution
import me.yingrui.segment.dict.POSUtil
import me.yingrui.segment.filter.ForeignName

/**
  * Labels for Chinese name recognition
  * B 姓氏
  * C 双名的首字
  * D 双名的末字
  * E 单名
  * F 前缀
  * G 后缀
  * K 人名的上文
  * L 人名的下文
  * M 两个中国人名之间的成分
  * U 人名的上文与姓氏成词
  * V 人名的末字与下文成词
  * X 姓与双名的首字成词
  * Y 姓与单名成词
  * Z 双名本身成词
  * A 其它无关词
  * @param expect
  * @param actual
  */
class PRFCorpusToSerialLabelCompareHooker(expect: SegmentResult, actual: SegmentResult) extends SegmentResultCompareHook {

  var serialLabels = List[(String, String)]()
  var isContainError = false
  var isOtherError = false
  var isError = false

  override def compeleted {
    serialLabels = scanTheSerialLabels
  }


  def scanTheSerialLabels: List[(String, String)] = {
    (for (i <- 0 until serialLabels.length) yield {
      val nextIsXing = i < serialLabels.length - 1 && ("BXY" contains serialLabels(i + 1)._2)
      val nextIsName = i < serialLabels.length - 1 && isLabeledAsName(serialLabels(i + 1)._2)
      if (nextIsXing && serialLabels(i)._2 == "A") {
        (serialLabels(i)._1, "K")
      } else if (nextIsName && serialLabels(i)._2 == "L") {
        (serialLabels(i)._1, "M")
      } else {
        serialLabels(i)
      }
    }).toList
  }

  override def foundCorrectWordHook(expectWord: Word, actualWord: Word, expectWordIndex: Int, actualWordIndex: Int) {
    isContainError = false
    isOtherError = false
    isError = false
    val label = getLabel(expectWord, actualWord, expectWordIndex, actualWordIndex)
    add(actualWord.name, label)
  }

  private def analysisError(expectWord: Word, actualWord: Word, expectWordIndex: Int, actualWordIndex: Int) {
    isContainError = startWith(actualWord, expectWord) && expectWordIndex < expect.length() - 1 && endWith(actualWord, expect(expectWordIndex + 1))
    isOtherError = startWith(actualWord, expectWord) && !isContainError
  }

  override def foundError(expectWord: Word, actualWord: Word, expectWordIndex: Int, actualWordIndex: Int) {
    isError = true
    analysisError(expectWord, actualWord, expectWordIndex, actualWordIndex)
    val label = getLabel(expectWord, actualWord, expectWordIndex, actualWordIndex)
    add(actualWord.name, label)
  }

  private def getLabel(expectWord: Word, actualWord: Word, expectWordIndex: Int, matchedWordIndex: Int): String = {
//    println(expectWord.word + " " + actualWord.word)
    var label = if (isName(expectWord) && isXing(expectWord)) "B" else "A"

    if (isName(expectWord) && isXing(actualWord) && lastLabel == "F" && endWith(expectWord, actualWord))
      label = "B"

    if (isName(expectWord) && isXing(actualWord) && startWith(expectWord, actualWord))
      label = "B"

    if (isName(expectWord) && isSingleWord(expectWord) && lastLabel == "B")
      label = "E"

    if (isName(expectWord) && !isSingleWord(expectWord) && isSingleWord(actualWord) && (lastLabel == "C" || lastLabel == "X"))
      label = "D"

    if (isName(expectWord) && (expectWordIndex + 1 < expect.length() && isName(expect(expectWordIndex + 1)) )&& !isSingleWord(expectWord) && !isSingleWord(actualWord) && overlap(expectWord, actualWord))
      label = "U"

    if (isContainError && isName(expect(expectWordIndex + 1)) && isXing(expect(expectWordIndex + 1)))
      label = "U"

    if (isName(expectWord) && (expectWordIndex + 1 < expect.length() && !isName(expect(expectWordIndex + 1)) )&& !isSingleWord(expectWord) && !isSingleWord(actualWord) && overlap(expectWord, actualWord))
      label = "V"

    if (isContainError && isName(expectWord) && !isName(expect(expectWordIndex + 1)) && isSingleWord(expectWord) && !isSingleWord(actualWord))
      label = "V"

    if (isOtherError && isName(expectWord) && isXing(expectWord) && isName(expect(expectWordIndex + 1)) && isSingleWord(expectWord) && !isSingleWord(actualWord))
      label = "X"

    if (isContainError && isName(expectWord) && isName(expect(expectWordIndex + 1)) && isSingleWord(expectWord) && isXing(expectWord))
      label = "Y"

    if ((lastLabel == "B" || lastLabel == "U") && !isError && actualWord.name.length > 1 && isName(expectWord))
      label = "Z"

    if (isName(expectWord) && !isSingleWord(expectWord) && isSingleWord(actualWord) && (lastLabel == "B" || lastLabel == "U")
        && startWith(expectWord, actualWord))
      label = "C"

    if (isName(expectWord) && expectWord.length == 2 && isSingleWord(actualWord) && !isXing(actualWord) && matchedWordIndex < actual.length() - 1 && isXing(actual(matchedWordIndex + 1))
        && startWith(expectWord, actualWord) && (lastLabel == "A" || lastLabel == "" || !isXing(lastExpectWord(expectWordIndex))))
      label = "F"

    if (isName(expectWord) && !isSingleWord(expectWord) && isSingleWord(actualWord) && !isXing(actualWord)
        && endWith(expectWord, actualWord) && lastLabel == "B" && isSuffix(actualWord))
      label = "G"

    if (!isName(expectWord) && label == "A" && isLabeledAsName(lastLabel))
      label = "L"

    if (startWith(expectWord, actualWord) && isName(expectWord) && isError && label == "A" && isForeignName(expectWord))
      label = "H"

    if (isName(expectWord) && endWith(expectWord, actualWord) && isError && label == "A" && isForeignName(expectWord))
      label = "J"

    if (isName(expectWord) && isError && label == "A" && isForeignName(expectWord))
      label = "I"

    label
  }


  private def isLabeledAsName(label: String): Boolean = !label.isEmpty && "BCDEYZIJ".contains(label)

  private def lastLabel = if(serialLabels.length > 0) serialLabels.last._2 else ""
  private def lastExpectWord(expectWordIndex: Int) = if(expectWordIndex > 0) expect(expectWordIndex - 1) else new Word

  private def isXing(word: Word): Boolean = NameProbDistribution().xingSet.contains(word.name)
  private def isForeignName(word: Word): Boolean = word.name.length >= 4 || ForeignName().isForeignName(word.name)

  private def isName(word: Word) = word.pos == POSUtil.POS_NR
  private def isSingleWord(word: Word) = word.length == 1
  private def startWith(expectWord: Word, actualWord: Word) = expectWord.name.startsWith(actualWord.name)
  private def endWith(expectWord: Word, actualWord: Word) = expectWord.name.endsWith(actualWord.name)
  private def isSuffix(word: Word): Boolean = "子氏老总局工队某妹姐叔婶哥兄弟嫂婆公伯".contains(word.name)
  private def overlap(expectWord: Word, actualWord: Word) = expectWord.name.last == actualWord.name.head

  private def add(word: String, label: String) {
    serialLabels = serialLabels :+(word, label)
  }
}
