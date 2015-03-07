package websiteschema.mpsegment.tools.accurary

import websiteschema.mpsegment.core.Word

trait SegmentResultCompareHook {

  def errorWordHook {}

  def compeleted {}

  def correctWordHook(expectWord: Word, matchedWord: Word, expectWordIndex: Int, matchedWordIndex: Int) {}

  def analyzeReason(expect: Word, possibleErrorWord: String) {}

  def foundError(expect: Word, word: Word, expectWordIndex: Int, errorWordIndex: Int) {}
}
