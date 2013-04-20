package websiteschema.mpsegment.dict

import websiteschema.mpsegment.concept.Concept

class DelegateWord(word: IWord, wordName: String) extends IWord {

  def getLog2Freq() : Int = word.getLog2Freq()

  def getDomainType() : Int = word.getDomainType()

  def getOccuredCount(s: String) : Int = word.getOccuredCount(s)

  def getOccuredSum() : Int = word.getOccuredSum()

  def getPOSArray() : POSArray = word.getPOSArray()

  def getWordPOSTable() : Array[Array[Int]] = word.getWordPOSTable()

  def getWordLength() : Int = wordName.length()

  def getWordName() : String = wordName

  def incOccuredCount(s: String) = word.incOccuredCount(s)

  def setDomainType(i: Int) {word.setDomainType(i)}

  def setOccuredCount(s: String, i: Int) {word.setOccuredCount(s,i)}

  def setOccuredSum(i: Int) {word.setOccuredSum(i)}

  def getWordMaxPOS() : Int = word.getWordMaxPOS()

  def toDBFString() : String = word.toDBFString()

  def getConcepts() : Array[Concept] = word.getConcepts()

}
