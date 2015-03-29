package me.yingrui.segment.tools.accurary

import me.yingrui.segment.core.{Word, SegmentResult}
import me.yingrui.segment.util.{StringUtil, NumberUtil}

class SegmentResultComparator(hooker: SegmentResultCompareHook) {

  var foundError = Set[Int]()
  var indexInOriginalString = -1

  def compare(expectResult: SegmentResult, actualResult: SegmentResult) {
    var lastMatchIndex = -1
    for (wordIndex <- 0 until expectResult.length) {
      indexInOriginalString = expectResult.getWordStartAt(wordIndex)
      val matchIndex = lookupMatch(actualResult, expectResult(wordIndex), lastMatchIndex + 1, wordIndex)
      if (matchIndex >= 0) {
        lastMatchIndex = matchIndex
        val expectWord = expectResult(wordIndex)
        val matchedWord = actualResult(matchIndex)
        hooker.correctWordHook(expectWord, matchedWord, wordIndex, matchIndex)
      } else {
        hooker.errorWordHook
      }
    }
    hooker.compeleted
  }

  private def lookupMatch(actualResult: SegmentResult, expectWord: Word, start: Int, expectWordIndex: Int): Int = {
    for (wordIndex <- start until actualResult.length) {
      val actualWord = actualResult.getWord(wordIndex)
      if (isSameWord(expectWord.name, actualWord)) {
        if (actualResult.getWordStartAt(wordIndex) == indexInOriginalString) {
          return wordIndex
        }
      }

    }
    analyzeErrorReason(actualResult, expectWord, start, expectWordIndex)
    return -1
  }

  private def recordError(errorWordIndex: Int, actual: Word, expect: Word, expectWordIndex: Int) {
    if (!(foundError contains errorWordIndex)) {
      hooker.foundError(expect, actual, expectWordIndex, errorWordIndex)
      foundError += errorWordIndex
    }
  }

  private def analyzeErrorReason(actualResult: SegmentResult, expect: Word, start: Int, expectWordIndex: Int) {
    val possibleErrorWord = lookupErrorWord(actualResult, expect, start, expectWordIndex)
    hooker.analyzeReason(expect, possibleErrorWord)
  }

  private def lookupErrorWord(actualResult: SegmentResult, expect: Word, start: Int, expectWordIndex: Int) = {
    val to = indexInOriginalString + expect.length
    val stringBuilder = new StringBuilder()
    for (wordIndex <- start until actualResult.length) {
      val indexInActualResult = actualResult.getWordStartAt(wordIndex)
      if (indexInActualResult >= indexInOriginalString && indexInActualResult < to) {
        recordError(wordIndex, actualResult(wordIndex), expect, expectWordIndex)
        stringBuilder.append(actualResult.getWord(wordIndex)).append(" ")
      }
    }
    stringBuilder.toString().trim()
  }


  private def isSameWord(expect: String, actual: String): Boolean = {
    val expectWord = StringUtil.doUpperCaseAndHalfShape(expect)
    if (expectWord.equalsIgnoreCase(actual)) {
      return true
    }
    if (Character.isDigit(actual.charAt(0))) {
      val number = NumberUtil.chineseToEnglishNumberStr(expect)
      if (actual.equals(number)) {
        return true
      }
    }
    return false
  }


}
