package me.yingrui.segment.tools.accurary

import me.yingrui.segment.core.Word

trait SegmentResultCompareHook {

  def errorWordHook {}

  def compeleted {}

  def foundCorrectWordHook(expectWord: Word, matchedWord: Word, expectWordIndex: Int, matchedWordIndex: Int) {}

  def analyzeReason(expect: Word, possibleErrorWord: String) {}

  def foundError(expect: Word, word: Word, expectWordIndex: Int, errorWordIndex: Int) {}
}
