package websiteschema.mpsegment.filter.ner

import websiteschema.mpsegment.core.SegmentResult
import websiteschema.mpsegment.util.WordUtil
import websiteschema.mpsegment.dict.ChNameDictionary
import websiteschema.mpsegment.conf.MPSegmentConfiguration

class NameEntityRecognizeResult(val nameWordCount: Int, val startWithXing: Boolean, val isForeignName: Boolean) {}

trait NameEntityRecognizer {

  val segmentResult: SegmentResult
  def recognizeNameWordBetween(begin: Int, end: Int): NameEntityRecognizeResult
}

object ProbChineseNameRecognizer {
  val chNameDict: ChNameDictionary = new ChNameDictionary()
  chNameDict.loadNameDict(MPSegmentConfiguration().getChNameDict())
}


class ProbChineseNameRecognizer(val segmentResult: SegmentResult) extends NameEntityRecognizer {

  private val factor1 = 1.1180000000000001D
  private val factor2 = 0.65000000000000002D
  private val factor3 = 1.6299999999999999D
  private val chNameDict = ProbChineseNameRecognizer.chNameDict

  def recognizeNameWordBetween(begin: Int, end: Int): NameEntityRecognizeResult = {
    new NameEntityRecognizeResult(recognizeNameWord(begin, end), chNameDict.isXing(segmentResult.getWord(begin)), false)
  }

  def recognizeNameWord(begin: Int, end: Int): Int = {
    val gap = (end - begin) + 1
    var numOfNameWordItem = -1
    if (segmentResult.getWord(begin).length() > 2 || segmentResult.getWord(begin + 1).length() > 2) {
      return numOfNameWordItem
    }
    if (segmentResult.getWord(begin + 1).length() == 1) {
      if (gap >= 3) {
        var d1 = chNameDict.computeLgLP2(segmentResult.getWord(begin), segmentResult.getWord(begin + 1))
        if (d1 <= 0.0D && segmentResult.getWord(begin).length() == 2) {
          return getNumNR(begin)
        }
        var d4 = chNameDict.computeLgLP3(segmentResult.getWord(begin), segmentResult.getWord(begin + 1), segmentResult.getWord(begin + 2))
        if (segmentResult.getWord(begin + 2).length() > 1) {
          d4 = 0.0D
        }
        if (d1 > 0.95999999999999996D) {
          d1 *= getRightBoundaryWordLP(segmentResult.getWord(begin + 2))
        }
        if (d4 > d1 && d4 > factor1) {
          numOfNameWordItem = 3
          if (isSpecialMingChar(begin + 2)) {
            val d5 = chNameDict.computeLgMing23(segmentResult.getWord(begin + 1), segmentResult.getWord(begin + 2))
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
        val d2 = chNameDict.computeLgLP2(segmentResult.getWord(begin), segmentResult.getWord(begin + 1))
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
      val d3 = chNameDict.computeLgLP3(segmentResult.getWord(begin), segmentResult.getWord(begin + 1).substring(0, 1), segmentResult.getWord(begin + 1).substring(1, 2))
      if (d3 > factor3) {
        numOfNameWordItem = 2
      }
    }
    return numOfNameWordItem
  }

  private def getNumNR(i1: Int): Int = {
    var byte0 = -1
    val s1 = segmentResult.getWord(i1).substring(0, 1)
    val s2 = segmentResult.getWord(i1).substring(1, 2)
    val s3 = segmentResult.getWord(i1 + 1)
    val d2 = chNameDict.computeLgLP3_2(segmentResult.getWord(i1), segmentResult.getWord(i1 + 1))
    var d1 = chNameDict.computeLgLP2(s1, s2)
    if (d1 > 0.95999999999999996D) {
      d1 *= getRightBoundaryWordLP(s3)
    }
    if (d2 > d1 && d2 > factor1) {
      byte0 = 2
      if (isSpecialMingChar(i1 + 1)) {
        var d3 = chNameDict.computeLgMing23(s2, s3)
        byte0 = -1
        if (d3 > 1.1339999999999999D || d3 > 0.90000000000000002D && d2 > 1.6000000000000001D && d2 / d1 > 2D) {
          byte0 = 2
        }
      }
    }
    return byte0
  }

  private def getRightBoundaryWordLP(s1: String): Double = {
    val d1 = 1.0D + chNameDict.getRightBoundaryWordLP(s1)
    return d1
  }

  private def isSpecialMingChar(wordIndex: Int): Boolean = {
    val word = segmentResult.getWord(wordIndex)
    return WordUtil.isSpecialMingChar(word)
  }

  private def wouldNotBeMingWithSpecialChar(index: Int): Boolean = {
    val word = segmentResult.getWord(index)
    if (word.equals("以") || word.equals("从")) {
      val d1 = chNameDict.computeLgMing23(segmentResult.getWord(index), segmentResult.getWord(index + 1))
      if (d1 < 0.92000000000000004D) {
        return true
      }
    } else if (word.equals("得") || word.equals("为") || word.equals("向") || word.equals("自")) {
      val d2 = chNameDict.computeLgMing23(segmentResult.getWord(index), segmentResult.getWord(index + 1))
      if (d2 <= 0.93000000000000005D) {
        return true
      }
    } else if (word.equals("则")) {
      val d3 = chNameDict.computeLgMing23(segmentResult.getWord(index), segmentResult.getWord(index + 1))
      if (d3 <= 0.80000000000000004D) {
        return true
      }
    } else if (word.equals("如")) {
      val d4 = chNameDict.computeLgMing23(segmentResult.getWord(index), segmentResult.getWord(index + 1))
      if (d4 <= 1.0D) {
        return true
      }
    }
    return false
  }
}
