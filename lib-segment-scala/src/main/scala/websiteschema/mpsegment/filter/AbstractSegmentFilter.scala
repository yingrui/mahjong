package websiteschema.mpsegment.filter

import websiteschema.mpsegment.core.SegmentResult
import collection.mutable.ListBuffer

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
