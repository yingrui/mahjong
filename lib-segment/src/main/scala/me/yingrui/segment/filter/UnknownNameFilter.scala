package me.yingrui.segment.filter

import ner.{NameEntityRecognizer, NeuralNetworkNameRecognizer, ProbChineseNameRecognizer}
import me.yingrui.segment.concept.Concept
import me.yingrui.segment.conf.MPSegmentConfiguration
import me.yingrui.segment.dict.POSUtil

import me.yingrui.segment.util.WordUtil._

class UnknownNameFilter(config: MPSegmentConfiguration) extends AbstractSegmentFilter {

  private var foreignName: ForeignName = null
  private val useChNameDict: Boolean = true
  private val useForeignNameDict = false
  private var segmentResultLength: Int = 0
  private val maxNameWordLength = 6
  private var nameStartIndex = -1
  private var nameEndIndex = -1
  private var hasPossibleFoundName: Boolean = false
  private var wordIndex = 0
  private var numOfNameWordItem: Int = -1
  private var startWithXing = false
  private var nameEntityRecognizer: NameEntityRecognizer = null

  if (useChNameDict) {
    if (useForeignNameDict && foreignName == null) {
      foreignName = new ForeignName()
      foreignName.loadNameWord()
    }
  }

  private def reachTheEnd(wordIndex: Int): Boolean = {
    return (wordIndex + 1) >= segmentResultLength
  }

  private def processPotentialName: Boolean = {
    val found = if (nameEndIndex - nameStartIndex >= 1) {
      val recognizedNameLength = recognizeNameWord()
      if (numOfNameWordItem >= 2) {
        wordIndex = nameStartIndex + recognizedNameLength
        true
      } else {
        wordIndex = nameStartIndex
        false
      }
    } else false
    markPositionImpossibleToBeName()
    found
  }

  override def doFilter() {
    nameEntityRecognizer = new ProbChineseNameRecognizer(segmentResult)
//    nameEntityRecognizer = NeuralNetworkNameRecognizer(segmentResult)
    if (useChNameDict && config.isChineseNameIdentify()) {
      segmentResultLength = segmentResult.length()
      markPositionImpossibleToBeName()
      wordIndex = 0
      while (wordIndex < segmentResultLength) {
        if (!isWordConfirmed(wordIndex)) {
          if (hasPossibleFoundName && (nameEndIndex - nameStartIndex >= maxNameWordLength || reachTheEnd(wordIndex))) {
            if (reachTheEnd(wordIndex)) {
              nameEndIndex = wordIndex
            }
            val found = processPotentialName
            if (reachTheEnd(wordIndex)) {
              return
            }
            if(!found) wordIndex += 1
          }

          if (segmentResult.getPOS(wordIndex) == POSUtil.POS_NR) {
            markPositionMaybeName()
          } else {
            val _isPos_P_C_U_W_UN = isPos_P_C_U_W_UN(segmentResult.getPOS(wordIndex), segmentResult.getWord(wordIndex))
            val _isChinesePreposition = isChinesePreposition(segmentResult.getWord(wordIndex))
            if (hasPossibleFoundName) {
              if ((_isPos_P_C_U_W_UN && !_isChinesePreposition)
                || segmentResult.getWord(wordIndex).length() > 2
                || (segmentResult.getWord(wordIndex).length > 1 && wordIndex - nameStartIndex >= 1)) {
                processPotentialName
              } else {
                nameEndIndex = wordIndex
                if (wordIndex + 1 == segmentResultLength && nameEndIndex - nameStartIndex >= 1) {
                  assert(false)
                  recognizeNameWord()
                }
              }
            } else if (segmentResult.getWord(wordIndex).length() == 1
              && !_isPos_P_C_U_W_UN || _isChinesePreposition) {
              markPositionMaybeName()
            }
          }
        }
        wordIndex += 1
      }
    }
  }

  private def markPositionMaybeName() {
    if (nameStartIndex < 0) {
      nameStartIndex = wordIndex
      hasPossibleFoundName = true
    } else {
      nameEndIndex = wordIndex
    }
  }

  private def markPositionImpossibleToBeName() {
    hasPossibleFoundName = false
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
    } else if (useForeignNameDict) {
      numOfNameWordItem = processForeignName(nameStartIndex, nameEndIndex)
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
//    val result = nameEntityRecognizer.recognizeNameWordBetween(begin, end)
    val result = nameEntityRecognizer.recognizeNameWordBetween(begin, end)
    numOfNameWordItem = result.nameWordCount
    startWithXing = result.startWithXing
    numOfNameWordItem
  }

  private def processForeignName(i1: Int, j1: Int): Int = {
    var wordCount = -1
    for (i2 <- i1 to j1) {
      if (!foreignName.isForeignName(segmentResult.getWord(i2))) {
        return wordCount
      }
      wordCount = i2
    }
    wordCount
  }

}