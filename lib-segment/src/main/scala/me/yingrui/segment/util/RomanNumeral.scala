package me.yingrui.segment.util

object RomanNumeral {
  val basicSymbols = Map[String, Int]("I" -> 1, "V" -> 5, "X" -> 10, "L" -> 50, "C" -> 100, "D" -> 500, "M" -> 1000)

  def getBasicSymbol(symbol: String) = basicSymbols(symbol)

  implicit def charToString(ch: Char) = ch.toString

  def convert(romanNumeral: String): Int = {
    if (romanNumeral.isEmpty) {
      0
    } else {
      if(romanNumeral.length == 1) {
        getBasicSymbol(romanNumeral)
      } else {
        val n = getBasicSymbol(romanNumeral(0))
        val m = getBasicSymbol(romanNumeral(1))
        if (n < m)
          m - n + convert(romanNumeral.substring(2))
        else
          n + convert(romanNumeral.substring(1))
      }
    }
  }
}
