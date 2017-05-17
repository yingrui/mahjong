package me.yingrui.segment.filter

import me.yingrui.segment.core.SegmentResult
import me.yingrui.segment.dict.{DictionaryFactory, POSUtil}

import scala.collection.mutable.ListBuffer

abstract class AbstractSegmentFilter extends ISegmentFilter {

  trait Operation {
    def modify(segmentResult: SegmentResult)
  }

  class DeleteOperation(index: Int) extends Operation {

    def modify(segmentResult: SegmentResult) {
      segmentResult.markWordToBeDeleted(index)
    }
  }

  class MergeOperation(start: Int, end: Int, pos: Int) extends Operation {

    def modify(segmentResult: SegmentResult) {
      segmentResult.merge(start, end, pos)
    }
  }

  class SeparateOperation(index: Int, pos: Int, secondPos: Int, indexAtWord: Int) extends Operation {

    def modify(segmentResult: SegmentResult) {
      val word = segmentResult(index)
      if (word != null) {
        val rest = word.name.substring(indexAtWord)
        val secondPartOfSpeech = getPartOfSpeechForSecondWord(secondPos, rest)
        segmentResult.separate(index, indexAtWord, pos, secondPartOfSpeech)
      }
    }
  }

  private def getPartOfSpeechForSecondWord(secondPos: Int, rest: String): Int = {
    if (secondPos == POSUtil.POS_UNKOWN) {
      val dict = DictionaryFactory().getCoreDictionary
      val word = if (dict != null) dict.getWord(rest) else null
      if (null != word) word.getWordPOSTable()(0)(0) else POSUtil.POS_UNKOWN
    } else secondPos
  }

  var segmentResult: SegmentResult = null
  private var wordPosIndexes: Array[Int] = null
  private val operationSettings = ListBuffer[Operation]()
  private var separateTimes = 0

  def doFilter()

  override def filtering() {
    resetStatus
    doFilter()
    compactSegmentResult()
  }


  private def resetStatus {
    separateTimes = 0
  }

  override def setSegmentResult(segmentResult: SegmentResult) {
    this.segmentResult = segmentResult
    wordPosIndexes = new Array[Int](segmentResult.length())
  }

  def setWordIndexesAndPOSForMerge(startWordIndex: Int, endWordIndex: Int, POS: Int) {
    operationSettings += (new MergeOperation(startWordIndex + separateTimes, endWordIndex + separateTimes, POS))
    markWordsHasBeenRecognized(startWordIndex, endWordIndex, POS)
  }

  def deleteWordAt(index: Int) {
    operationSettings += (new DeleteOperation(index + separateTimes))
  }

  def separateWordAt(index: Int, partOfSpeech: Int, secondPartOfSpeech: Int = POSUtil.POS_UNKOWN, indexAtWord: Int = 1) {
    operationSettings += (new SeparateOperation(index + separateTimes, partOfSpeech, secondPartOfSpeech, indexAtWord))
    separateTimes += 1
  }

  def compactSegmentResult() {
    if (operationSettings.size > 0) {
      for (mergeSetting <- operationSettings) {
        mergeSetting.modify(segmentResult)
      }
      segmentResult.compact()
      operationSettings.clear
    }
  }

  def isWordConfirmed(wordIndex: Int): Boolean = {
    return wordPosIndexes(wordIndex) > 0
  }

  def isNotMarked(index: Int): Boolean = {
    return wordPosIndexes(index) <= 0
  }

  private def markWordsHasBeenRecognized(startWordIndex: Int, endWordIndex: Int, POS: Int) {
    for (i <- startWordIndex to endWordIndex) {
      wordPosIndexes(i) = POS
    }
  }
}
