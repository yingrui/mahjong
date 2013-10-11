package websiteschema.mpsegment.tools.accurary

import websiteschema.mpsegment.core.WordAtom

trait SegmentResultCompareHook {

  def errorWordHook {}

  def correctWordHook(expectWord: WordAtom, matchedWord: WordAtom) {}

  def analyzeReason(expect: WordAtom, possibleErrorWord: String) {}

  def foundError(word: WordAtom, expect: WordAtom) {}
}
