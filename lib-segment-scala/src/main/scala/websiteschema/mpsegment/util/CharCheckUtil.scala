package websiteschema.mpsegment.util

object CharCheckUtil {

  def isChinese(c: Char): Boolean =
    Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

  def isChinese(s: String): Boolean = {
    s.find(ch => !isChinese(ch)) match {
      case None => true
      case _ => false
    }
  }

  def isSymbol(c: Char): Boolean = {
    val typo = Character.getType(c);
    return typo == 24 || typo == 25 || typo == 21 || typo == 22 || typo == 27 || typo == 23 || typo == 26 || typo == 20;
  }

  def isWhiteSpace(word: String): Boolean = word.matches("\\s+")

  def isEnglish(word: String): Boolean = word.matches("[A-z]+('|'s)?")
}
