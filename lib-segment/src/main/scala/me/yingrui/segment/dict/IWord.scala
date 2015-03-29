package me.yingrui.segment.dict

import me.yingrui.segment.concept.Concept

trait IWord {

  def getLog2Freq(): Int

  def getDomainType(): Int

  def getOccuredCount(s: String): Int

  def getOccuredSum(): Int

  def getPOSArray(): POSArray

  def getWordPOSTable(): Array[Array[Int]]

  def getWordLength(): Int

  def getWordName(): String

  def incOccuredCount(s: String)

  def setDomainType(i: Int)

  def setOccuredCount(s: String, i: Int)

  def setOccuredSum(i: Int)

  def getWordMaxPOS(): Int

  def getConcepts(): Array[Concept]
}