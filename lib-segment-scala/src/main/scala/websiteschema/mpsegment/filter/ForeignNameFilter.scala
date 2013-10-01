package websiteschema.mpsegment.filter

import ner.{RecognizerCreator, NameEntityRecognizer}
import websiteschema.mpsegment.concept.Concept
import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.dict.POSUtil

import websiteschema.mpsegment.util.WordUtil._

class ForeignNameFilter(config: MPSegmentConfiguration, recognizerCreator: RecognizerCreator, maxNameWordLength: Int) extends ChineseNameFilter(config, recognizerCreator, maxNameWordLength) {

  private val foreignName = ForeignName()

  override def isEndOfName(_isPos_P_C_U_W_UN: Boolean, _isChinesePreposition: Boolean): Boolean = {
    val isBigWord = segmentResult.getWord(wordIndex).length() > 2
    val isNotSingleWordSinceSecondWord = segmentResult.getWord(wordIndex).length > 1 && wordIndex - nameStartIndex >= 1
    val isNotForeignName = !foreignName.isForeignName(segmentResult.getWord(wordIndex))
    (_isPos_P_C_U_W_UN && !_isChinesePreposition) || isBigWord || isNotSingleWordSinceSecondWord || isNotForeignName
  }

}