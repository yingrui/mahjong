package me.yingrui.segment.dict

import me.yingrui.segment.concept.Concept

object UnknownWord {

  def apply(wordName: String) = {
    val word = new UnknownWord()
    word.wordName = wordName
    word
  }
}

class UnknownWord extends IWord {

    var wordName: String = ""

    override def getLog2Freq() : Int = {
        return 0
    }

    override def getDomainType() : Int = {
        return 0
    }

    override def getOccuredCount(s: String) : Int = {
        if (s.equals(POSUtil.getPOSString(POSUtil.POS_UNKOWN))) {
            return 1
        }
        return 0
    }

    override def getOccuredSum() : Int = {
        return 1
    }

    override def getPOSArray() : POSArray = {
        return POSArrayFactory.getUnknownWordPOSArray()
    }

    override def getWordPOSTable() = getPOSArray().getWordPOSTable()

    override def getWordLength() : Int = {
        return wordName.length()
    }

    override def getWordName() : String = {
        return wordName
    }

    override def incOccuredCount(s: String) {
    }

    override def setDomainType(i: Int) {
    }

    override def setOccuredCount(s: String, i: Int) {
    }

    override def setOccuredSum(i: Int) {
    }

    override def getWordMaxPOS() : Int = {
        return POSUtil.POS_UNKOWN
    }

  override def getConcepts() : Array[Concept] = {
      val array = new Array[Concept](1)
      array(0) = Concept.UNKNOWN
      array
    }

}
