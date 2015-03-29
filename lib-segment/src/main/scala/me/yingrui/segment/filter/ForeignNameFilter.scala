package me.yingrui.segment.filter

import ner.{RecognizerCreator, NameEntityRecognizer}
import me.yingrui.segment.concept.Concept
import me.yingrui.segment.conf.MPSegmentConfiguration
import me.yingrui.segment.dict.POSUtil

import me.yingrui.segment.util.WordUtil._

class ForeignNameFilter(config: MPSegmentConfiguration, recognizerCreator: RecognizerCreator, maxNameWordLength: Int) extends ChineseNameFilter(config, recognizerCreator, maxNameWordLength) {

  private val foreignName = ForeignName()

  override def isEndOfName(_isPos_P_C_U_W_UN: Boolean, _isChinesePreposition: Boolean): Boolean = {
    val isBigWord = segmentResult.getWord(wordIndex).length() > 2
    val isNotSingleWordSinceSecondWord = segmentResult.getWord(wordIndex).length > 1 && wordIndex - nameStartIndex >= 1
    val isNotForeignName = !foreignName.isForeignName(segmentResult.getWord(wordIndex))
    (_isPos_P_C_U_W_UN && !_isChinesePreposition) || isBigWord || isNotSingleWordSinceSecondWord || isNotForeignName
  }

}