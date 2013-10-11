package websiteschema.mpsegment.tools.accurary

import websiteschema.mpsegment.core.WordAtom

trait SegmentResultCompareHook {

  def errorWordHook {}

  def correctWordHook(expectWord: WordAtom, matchedWord: WordAtom, expectWordIndex: Int, matchedWordIndex: Int) {}

  def analyzeReason(expect: WordAtom, possibleErrorWord: String) {}

  def foundError(expect: WordAtom, word: WordAtom, expectWordIndex: Int, errorWordIndex: Int) {}
}
