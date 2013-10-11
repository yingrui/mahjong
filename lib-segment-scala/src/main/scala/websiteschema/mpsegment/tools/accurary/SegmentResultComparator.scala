package websiteschema.mpsegment.tools.accurary

import websiteschema.mpsegment.core.{WordAtom, SegmentResult}
import websiteschema.mpsegment.util.{StringUtil, NumberUtil}

class SegmentResultComparator(hooker: SegmentResultCompareHook) {

  var foundError = Set[Int]()

  def compare(expectResult: SegmentResult, actualResult: SegmentResult) {
    var lastMatchIndex = -1
    for (i <- 0 until expectResult.length) {
      val indexInOriginalString = expectResult.getWordIndexInOriginalString(i)
      val matches = lookupMatch(actualResult, expectResult(i), lastMatchIndex + 1, indexInOriginalString)
      if (matches >= 0) {
        lastMatchIndex = matches
        val expectWord = expectResult(i)
        val matchedWord = actualResult(matches)
        hooker.correctWordHook(expectWord, matchedWord)
      } else {
        hooker.errorWordHook
      }
    }
  }

  private def lookupMatch(actualResult: SegmentResult, expectWord: WordAtom, start: Int, indexInOriginalString: Int): Int = {
    for (wordIndex <- start until actualResult.length) {
      val actualWord = actualResult.getWord(wordIndex)
      if (isSameWord(expectWord.word, actualWord)) {
        if (actualResult.getWordIndexInOriginalString(wordIndex) == indexInOriginalString) {
          return wordIndex
        }
      }

    }
    analyzeErrorReason(actualResult, expectWord, start, indexInOriginalString)
    return -1
  }

  private def recordError(index: Int, actual: WordAtom, expect: WordAtom) {
    if (!(foundError contains index)) {
      hooker.foundError(actual, expect)
      foundError += index
    }
  }

  private def analyzeErrorReason(actualResult: SegmentResult, expect: WordAtom, start: Int, from: Int) {
    val possibleErrorWord = lookupErrorWord(actualResult, expect, start, from)
    hooker.analyzeReason(expect, possibleErrorWord)
  }

  private def lookupErrorWord(actualResult: SegmentResult, expect: WordAtom, start: Int, from: Int): String = {
    val to = from + expect.length
    val stringBuilder = new StringBuilder()
    for (wordIndex <- start until actualResult.length) {
      val indexInOriginalString = actualResult.getWordIndexInOriginalString(wordIndex)
      if (indexInOriginalString >= from && indexInOriginalString < to) {
        recordError(wordIndex, actualResult(wordIndex), expect)
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
