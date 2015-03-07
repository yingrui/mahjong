package websiteschema.mpsegment.util

import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.dict.POSUtil
import websiteschema.mpsegment.util.CharCheckUtil._

object WordUtil {

  def isNumerical(wordString: String): Int = {
    if (wordString.length() <= 0) {
      2
    } else {
      wordString.find(!Character.isDigit(_)) match {
        case Some(ch) => return 2
        case _ => return 1
      }
    }
  }

  def isCharaterOrDigit(ch: Char): Boolean = {
    ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9'
  }

  def isAlphaNumericWithUnderScore(wordString: String): Boolean =
    wordString.find(ch => !isCharaterOrDigit(ch) && ch != '_') match {
      case Some(c) => false
      case _ => true
    }

  def isAlphaNumericWithUnderScore_Slash_Colon(wordString: String): Boolean =
    wordString.find(ch => !isCharaterOrDigit(ch) && ch != '_' && ch != '/' && ch != ':') match {
      case Some(c) => false
      case _ => true
    }

  def isLetterOrDigitWithUnderscore(wordString: String): Boolean =
    wordString.find(ch => !Character.isLetterOrDigit(ch) && ch != '_') match {
      case Some(c) => false
      case _ => true
    }

  def isPos_P_C_U_W_UN(POS: Int, word: String): Boolean = {
    POS == POSUtil.POS_P || POS == POSUtil.POS_C || POS == POSUtil.POS_U || (POS == POSUtil.POS_W && !word.equals("·")) || (POS == POSUtil.POS_UNKOWN && !isChinese(word))
  }

  def isChinesePreposition(wordString: String): Boolean = {
    wordString.equals("向") || wordString.equals("和") || wordString.equals("丁") || wordString.equals("自") || wordString.equals("若") || wordString.equals("于") || wordString.equals("同") || wordString.equals("为") || wordString.equals("以") || wordString.equals("连") || wordString.equals("从") || wordString.equals("得") || wordString.equals("则") || wordString.equals("之")
  }

  def isLeftOrRightBraceOrColonOrSlash(ch: String): Boolean = {
    var flag = false
    if (glueChars.indexOf(ch) >= 0 && glueChar.indexOf(ch) < 0) {
      flag = true
    }
    flag
  }

  def isSpecialMingChar(ch: String): Boolean = {
    ch.equals("向") || ch.equals("自") || ch.equals("乃") || ch.equals("以") || ch.equals("从") || ch.equals("和") || ch.equals("得") || ch.equals("为") || ch.equals("则") || ch.equals("如")
  }

  val glueChars = "*?~/Array[_]:"

  val config = MPSegmentConfiguration()

  val glueChar = config.getGlueChar()
}
