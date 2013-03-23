package websiteschema.mpsegment.filter

import websiteschema.mpsegment.concept.Concept
import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.dict.ChNameDictionary
import websiteschema.mpsegment.dict.POSUtil
import websiteschema.mpsegment.util.WordUtil

import websiteschema.mpsegment.util.WordUtil._
import websiteschema.mpsegment.util.WordUtil._

class UnknownNameFilter(config: MPSegmentConfiguration) extends AbstractSegmentFilter {

  private val factor1 = 1.1180000000000001D
  private val factor2 = 0.65000000000000002D
  private val factor3 = 1.6299999999999999D
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
            val _isChineseJieCi = isChineseJieCi(segmentResult.getWord(wordIndex))
            if (hasPossibleFoundName) {
              if ((_isPos_P_C_U_W_UN && !_isChineseJieCi)
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
              && !_isPos_P_C_U_W_UN || _isChineseJieCi) {
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
    return numOfNameWordItem
  }

  private def recognizeNameWordBetween(begin: Int, end: Int): Int = {
    val gap = (end - begin) + 1
    numOfNameWordItem = -1
    if (segmentResult.getWord(begin).length() > 2 || segmentResult.getWord(begin + 1).length() > 2) {
      return numOfNameWordItem
    }
    if (segmentResult.getWord(begin + 1).length() == 1) {
      if (gap >= 3) {
        var d1 = UnknownNameFilter.chNameDict.computeLgLP2(segmentResult.getWord(begin), segmentResult.getWord(begin + 1))
        if (d1 <= 0.0D && segmentResult.getWord(begin).length() == 2) {
          return getNumNR(begin)
        }
        var d4 = UnknownNameFilter.chNameDict.computeLgLP3(segmentResult.getWord(begin), segmentResult.getWord(begin + 1), segmentResult.getWord(begin + 2))
        if (segmentResult.getWord(begin + 2).length() > 1) {
          d4 = 0.0D
        }
        if (d1 > 0.95999999999999996D) {
          d1 *= getRightBoundaryWordLP(segmentResult.getWord(begin + 2))
        }
        if (d4 > d1 && d4 > factor1) {
          numOfNameWordItem = 3
          if (isSpecialMingChar(begin + 2)) {
            val d5 = UnknownNameFilter.chNameDict.computeLgMing23(segmentResult.getWord(begin + 1), segmentResult.getWord(begin + 2))
            numOfNameWordItem = 2
            if (d5 > 1.1339999999999999D || d5 > 0.90000000000000002D && d4 > 1.6000000000000001D && d4 / d1 > 2D) {
              numOfNameWordItem = 3
            }
          } else if (wouldNotBeMingWithSpecialChar(begin + 1)) {
            numOfNameWordItem = -1
          }
        } else if (d1 > d4 && d1 > factor2) {
          numOfNameWordItem = 2
          if (isSpecialMingChar(begin + 1)) {
            numOfNameWordItem = -1
          }
        }
      } else {
        val d2 = UnknownNameFilter.chNameDict.computeLgLP2(segmentResult.getWord(begin), segmentResult.getWord(begin + 1))
        if (d2 > factor2) {
          numOfNameWordItem = 2
          if (isSpecialMingChar(begin + 1)) {
            numOfNameWordItem = -1
          }
        } else if (d2 <= 0.0D && segmentResult.getWord(begin).length() == 2) {
          numOfNameWordItem = getNumNR(begin)
        }
      }
    } else if (segmentResult.getWord(begin + 1).length() == 2) {
      val d3 = UnknownNameFilter.chNameDict.computeLgLP3(segmentResult.getWord(begin), segmentResult.getWord(begin + 1).substring(0, 1), segmentResult.getWord(begin + 1).substring(1, 2))
      if (d3 > factor3) {
        numOfNameWordItem = 2
      }
    }
    return numOfNameWordItem
  }

  private def processForeignName(i1: Int, j1: Int): Int = {
    var l1 = -1
    for (i2 <- i1 to j1) {
      if (!foreignName.isForiegnName(segmentResult.getWord(i2))) {
        return l1
      }
      l1 = i2
    }
    return l1
  }

  private def getNumNR(i1: Int): Int = {
    var byte0 = -1
    val s1 = segmentResult.getWord(i1).substring(0, 1)
    val s2 = segmentResult.getWord(i1).substring(1, 2)
    val s3 = segmentResult.getWord(i1 + 1)
    val d2 = UnknownNameFilter.chNameDict.computeLgLP3_2(segmentResult.getWord(i1), segmentResult.getWord(i1 + 1))
    var d1 = UnknownNameFilter.chNameDict.computeLgLP2(s1, s2)
    if (d1 > 0.95999999999999996D) {
      d1 *= getRightBoundaryWordLP(s3)
    }
    if (d2 > d1 && d2 > factor1) {
      byte0 = 2
      if (isSpecialMingChar(i1 + 1)) {
        var d3 = UnknownNameFilter.chNameDict.computeLgMing23(s2, s3)
        byte0 = -1
        if (d3 > 1.1339999999999999D || d3 > 0.90000000000000002D && d2 > 1.6000000000000001D && d2 / d1 > 2D) {
          byte0 = 2
        }
      }
    }
    return byte0
  }

  private def getRightBoundaryWordLP(s1: String): Double = {
    val d1 = 1.0D + UnknownNameFilter.chNameDict.getRightBoundaryWordLP(s1)
    return d1
  }

  private def isSpecialMingChar(wordIndex: Int): Boolean = {
    val word = segmentResult.getWord(wordIndex)
    return WordUtil.isSpecialMingChar(word)
  }

  private def wouldNotBeMingWithSpecialChar(index: Int): Boolean = {
    val word = segmentResult.getWord(index)
    if (word.equals("以") || word.equals("从")) {
      val d1 = UnknownNameFilter.chNameDict.computeLgMing23(segmentResult.getWord(index), segmentResult.getWord(index + 1))
      if (d1 < 0.92000000000000004D) {
        return true
      }
    } else if (word.equals("得") || word.equals("为") || word.equals("向") || word.equals("自")) {
      val d2 = UnknownNameFilter.chNameDict.computeLgMing23(segmentResult.getWord(index), segmentResult.getWord(index + 1))
      if (d2 <= 0.93000000000000005D) {
        return true
      }
    } else if (word.equals("则")) {
      val d3 = UnknownNameFilter.chNameDict.computeLgMing23(segmentResult.getWord(index), segmentResult.getWord(index + 1))
      if (d3 <= 0.80000000000000004D) {
        return true
      }
    } else if (word.equals("如")) {
      val d4 = UnknownNameFilter.chNameDict.computeLgMing23(segmentResult.getWord(index), segmentResult.getWord(index + 1))
      if (d4 <= 1.0D) {
        return true
      }
    }
    return false
  }
}

object UnknownNameFilter {
  private val chNameDict: ChNameDictionary = new ChNameDictionary()
  chNameDict.loadNameDict(MPSegmentConfiguration().getChNameDict())
}
