package websiteschema.mpsegment.filter

import websiteschema.mpsegment.core.SegmentResult
import collection.mutable.ListBuffer
import websiteschema.mpsegment.dict.{POSUtil, DictionaryFactory}

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

  class SeparateOperation(index: Int, pos: Int) extends Operation {

    def modify(segmentResult: SegmentResult) {
      val rest = segmentResult(index).word.substring(1)
      val word = DictionaryFactory().getCoreDictionary().getWord(rest)
      if (word != null) {
        segmentResult.separate(index, 1, pos, word.getWordPOSTable()(0)(0))
      } else {
        segmentResult.separate(index, 1, pos, POSUtil.POS_UNKOWN)
      }
    }
  }

  var segmentResult: SegmentResult = null
  private var wordPosIndexes: Array[Int] = null
  private val operationSettings = ListBuffer[Operation]()

  def doFilter()

  override def filtering() {
    doFilter()
    compactSegmentResult()
  }

  override def setSegmentResult(segmentResult: SegmentResult) {
    this.segmentResult = segmentResult
    wordPosIndexes = new Array[Int](segmentResult.length())
  }

  def setWordIndexesAndPOSForMerge(startWordIndex: Int, endWordIndex: Int, POS: Int) {
    operationSettings += (new MergeOperation(startWordIndex, endWordIndex, POS))
    markWordsHasBeenRecognized(startWordIndex, endWordIndex, POS)
  }

  def deleteWordAt(index: Int) {
    operationSettings += (new DeleteOperation(index))
  }

  def separateWordAt(index: Int, partOfSpeech: Int) {
    operationSettings += (new SeparateOperation(index, partOfSpeech))
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
