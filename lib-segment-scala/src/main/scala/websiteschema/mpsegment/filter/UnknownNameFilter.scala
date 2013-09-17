package websiteschema.mpsegment.filter

import ner.{NeuralNetworkChineseNameRecognizer, ProbChineseNameRecognizer}
import websiteschema.mpsegment.concept.Concept
import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.dict.POSUtil

import websiteschema.mpsegment.util.WordUtil._

class UnknownNameFilter(config: MPSegmentConfiguration) extends AbstractSegmentFilter {

  private var foreignName: ForeignName = null
  private val useChNameDict: Boolean = true
  private val useForeignNameDict = false
  private var segmentResultLength: Int = 0
  private val maxNameWordLength = 5
  private var nameStartIndex = -1
  private var nameEndIndex = -1
  private var hasPossibleFoundName: Boolean = false
  private var wordIndex = 0
  private var numOfNameWordItem: Int = -1

  if (useChNameDict) {
    if (useForeignNameDict && foreignName == null) {
      foreignName = new ForeignName()
      foreignName.loadNameWord()
    }
  }

  private def reachTheEnd(wordIndex: Int): Boolean = {
    return (wordIndex + 1) >= segmentResultLength
  }

  private def processPotentialName() {
    if (nameEndIndex - nameStartIndex >= 1) {
      val recognizedNameLength = recognizeNameWord()
      if (numOfNameWordItem >= 2) {
        wordIndex = nameStartIndex + recognizedNameLength
      } else {
        wordIndex = nameStartIndex + 1
      }
    }
    markPositionImpossibleToBeName()
  }

  override def doFilter() {
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
            processPotentialName()
            if (wordIndex + 1 > segmentResultLength) {
              return
            }
          }

          if (segmentResult.getPOS(wordIndex) == POSUtil.POS_NR) {
            markPositionMaybeName()
          } else {
            val _isPos_P_C_U_W_UN = isPos_P_C_U_W_UN(segmentResult.getPOS(wordIndex))
            val _isChinesePreposition = isChinesePreposition(segmentResult.getWord(wordIndex))
            if (hasPossibleFoundName) {
              if ((_isPos_P_C_U_W_UN && !_isChinesePreposition)
                || segmentResult.getWord(wordIndex).length() > 2) {
                processPotentialName()
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
        wordIndex+=1
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
        if (numOfNameWordItem >= 3) {
          val numOfMingWord = numOfNameWordItem - 1
          setWordIndexesAndPOSForMerge(nameStartIndex + 1, nameStartIndex + numOfMingWord, POSUtil.POS_NR)
        }
        segmentResult.setPOS(nameStartIndex, POSUtil.POS_NR)
        segmentResult.setConcept(nameStartIndex, Concept.UNKNOWN.getName())
        segmentResult.setConcept(nameStartIndex + 1, Concept.UNKNOWN.getName())
      } else if (numOfNameWordItem >= 2) {
        setWordIndexesAndPOSForMerge(nameStartIndex, (nameStartIndex + numOfNameWordItem) - 1, POSUtil.POS_NR)
        segmentResult.setConcept(nameStartIndex, Concept.UNKNOWN.getName())
      }
    } else if (useForeignNameDict) {
      numOfNameWordItem = processForeignName(nameStartIndex, nameEndIndex)
    }
    numOfNameWordItem
  }

  private def recognizeNameWordBetween(begin: Int, end: Int): Int = {
//    numOfNameWordItem = new ProbChineseNameRecognizer(segmentResult).recognizeNameWordBetween(begin, end)
    numOfNameWordItem = NeuralNetworkChineseNameRecognizer(segmentResult).recognizeNameWordBetween(begin, end)
    numOfNameWordItem
  }

  private def processForeignName(i1: Int, j1: Int): Int = {
    var l1 = -1
    for (i2 <- i1 to j1) {
      if (!foreignName.isForiegnName(segmentResult.getWord(i2))) {
        return l1
      }
      l1 = i2
    }
    l1
  }

}