package websiteschema.mpsegment.filter

import ner.{RecognizerCreator, NameEntityRecognizer}
import websiteschema.mpsegment.concept.Concept
import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.dict.POSUtil

import websiteschema.mpsegment.util.WordUtil._

class ChineseNameFilter(config: MPSegmentConfiguration, recognizerCreator: RecognizerCreator, maxNameWordLength: Int) extends AbstractSegmentFilter {

  private val useChNameDict = true
  private var segmentResultLength = 0
  protected var nameStartIndex = -1
  protected var nameEndIndex = -1
  protected var hasFoundPossibleName = false
  protected var wordIndex = 0
  protected var numOfNameWordItem = -1
  private var startWithXing = false
  private var nameEntityRecognizer: NameEntityRecognizer = null

  private def processPotentialName: Boolean = {
    val found = if (nameEndIndex - nameStartIndex >= 1) {
      val recognizedNameLength = recognizeNameWord()
      if (recognizedNameLength > 0) {
        wordIndex = nameStartIndex + recognizedNameLength - 1
        true
      } else {
        wordIndex = nameStartIndex
        false
      }
    } else false
    resetStatus()
    found
  }

  override def doFilter() {
    nameEntityRecognizer = recognizerCreator.create(segmentResult)
    if (useChNameDict && config.isChineseNameIdentify()) {
      segmentResultLength = segmentResult.length()
      resetStatus()
      wordIndex = 0
      while (wordIndex < segmentResultLength) {
        if (!isWordConfirmed(wordIndex)) {
          if (hasFoundPossibleName && (upToMaximumNameLength || reachTheEnd)) {
            nameEndIndex = wordIndex
            val found = processPotentialName
            if (reachTheEnd) {
              return
            }
            if(!found) wordIndex += 1
          }

          if (segmentResult.getPOS(wordIndex) == POSUtil.POS_NR) {
            markPositionMaybeName
          } else {
            val _isPos_P_C_U_W_UN = isPos_P_C_U_W_UN(segmentResult.getPOS(wordIndex), segmentResult.getWord(wordIndex))
            val _isChinesePreposition = isChinesePreposition(segmentResult.getWord(wordIndex))
            val _isSingleWord = segmentResult.getWord(wordIndex).length() == 1
            if (hasFoundPossibleName) {
              if (isEndOfName(_isPos_P_C_U_W_UN, _isChinesePreposition)) {
                processPotentialName
              } else {
                markPositionMaybeName
              }
            } else if (_isSingleWord && (!_isPos_P_C_U_W_UN || _isChinesePreposition)) {
              markPositionMaybeName
            }
          }
        }
        wordIndex += 1
      }
    }
  }


  def isEndOfName(_isPos_P_C_U_W_UN: Boolean, _isChinesePreposition: Boolean): Boolean = {
    val isBigWord = segmentResult.getWord(wordIndex).length() > 2
    val isNotSingleWordSinceSecondWord = segmentResult.getWord(wordIndex).length > 1 && wordIndex - nameStartIndex >= 1
    (_isPos_P_C_U_W_UN && !_isChinesePreposition) || isBigWord || isNotSingleWordSinceSecondWord
  }

  private def reachTheEnd: Boolean = (wordIndex + 1) >= segmentResultLength

  private def upToMaximumNameLength: Boolean = wordIndex - nameStartIndex >= maxNameWordLength - 1

  private def markPositionMaybeName() {
    if (nameStartIndex < 0) {
      nameStartIndex = wordIndex
      hasFoundPossibleName = true
    } else {
      nameEndIndex = wordIndex
    }
  }

  private def resetStatus() {
    hasFoundPossibleName = false
    nameStartIndex = -1
    nameEndIndex = -1
  }

  private def recognizeNameWord(): Int = {
    recognizeNameWordBetween(nameStartIndex, nameEndIndex)
    if (numOfNameWordItem > 0 && nameStartIndex > 0 && segmentResult.getPOS(nameStartIndex - 1) == POSUtil.POS_M) {
      numOfNameWordItem = 0
    }
    if (numOfNameWordItem > 0) {
      if (config.isXingMingSeparate()) {
        separateXingMing
      } else if (numOfNameWordItem >= 2) {
        setWordIndexesAndPOSForMerge(nameStartIndex, (nameStartIndex + numOfNameWordItem) - 1, POSUtil.POS_NR)
        segmentResult.setConcept(nameStartIndex, Concept.UNKNOWN.getName())
      }
    }
    numOfNameWordItem
  }

  private def separateXingMing {
    val isChineseName = numOfNameWordItem <= 3
    if (isChineseName && startWithXing) {
      if (numOfNameWordItem >= 2) {
        val numOfMingWord = numOfNameWordItem - 1
        setWordIndexesAndPOSForMerge(nameStartIndex + 1, nameStartIndex + numOfMingWord, POSUtil.POS_NR)
      }
      segmentResult.setPOS(nameStartIndex, POSUtil.POS_NR)
      segmentResult.setConcept(nameStartIndex, Concept.UNKNOWN.getName())
      segmentResult.setConcept(nameStartIndex + 1, Concept.UNKNOWN.getName())
    } else {
      setWordIndexesAndPOSForMerge(nameStartIndex, (nameStartIndex + numOfNameWordItem) - 1, POSUtil.POS_NR)
      segmentResult.setConcept(nameStartIndex, Concept.UNKNOWN.getName())
    }
  }

  private def recognizeNameWordBetween(begin: Int, end: Int): Int = {
    val result = nameEntityRecognizer.recognizeNameWordBetween(begin, end)
    numOfNameWordItem = result.nameWordCount
    startWithXing = result.startWithXing
    numOfNameWordItem
  }

}