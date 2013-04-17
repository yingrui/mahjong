package websiteschema.mpsegment.dict

import websiteschema.mpsegment.concept.Concept

class DomainWordItem extends IWord with Comparable[Object] {

  var wordName = ""
  var domainType = 0

  override def setOccuredCount(pos: String, freq: Int) {
    posArray.setPOSCount(pos, freq)
  }

  override def setOccuredSum(sum: Int) {
    val factor = sum.toDouble / getOccuredSum().toDouble
    val posTable = posArray.getWordPOSTable()
    for (i <- 0 until posTable.length) {
      val freq = posTable(i)(1)
      posTable(i)(1) = (freq * factor).toInt
      calculateLogFreq()
    }
    calculateLogFreq()
  }

  private def calculateLogFreq() {
    log2Freq = (Math.log(getOccuredSum() + 1L) * 100D).toInt
  }

  override def getPOSArray(): POSArray = {
    return posArray
  }

  override def getWordPOSTable(): Array[Array[Int]] = {
    return getPOSArray().getWordPOSTable()
  }

  override def getWordMaxPOS(): Int = {
    return posArray.getWordMaxPOS()
  }

  override def getWordName(): String = {
    return wordName
  }

  override def getWordLength(): Int = {
    return wordName.length()
  }

  override def setDomainType(domainType: Int) {
    this.domainType = domainType
  }

  override def getDomainType(): Int = {
    return domainType
  }

  override def getLog2Freq(): Int = {
    if (log2Freq == 0) {
      log2Freq = (Math.log(getOccuredSum() + 1L) * 100D).toInt
    }
    return log2Freq
  }

  override def getOccuredSum(): Int = {
    return posArray.getOccurredSum()
  }

  override def getOccuredCount(s: String): Int = {
    return posArray.getOccurredCount(s)
  }

  override def incOccuredCount(s: String) {
    posArray.incPOSCount(s)
  }

  override def compareTo(obj: Object): Int = {
    if (obj != null && (obj.isInstanceOf[String])) {
      return wordName.compareTo(obj.asInstanceOf[String])
    }
    if (obj != null && (obj.isInstanceOf[DomainWordItem])) {
      return wordName.compareTo(obj.asInstanceOf[DomainWordItem].getWordName())
    } else {
      return 1
    }
  }

  def equals(obj: DomainWordItem): Boolean = {
    if (obj != null ) {
      return wordName.equals(obj.wordName)
    } else {
      return false
    }
  }

  override def toString(): String = {
    val stringBuilder = new StringBuilder()
    stringBuilder.append((new StringBuilder(String.valueOf(getWordName()))).append("\n").toString())
    stringBuilder.append(getPOSArray().toString())
    return stringBuilder.toString()
  }

  override def toDBFString(): String = {
    val stringBuilder = new StringBuilder()
    stringBuilder.append(getPOSArray().toDBFString(getWordName()))
    return stringBuilder.toString()
  }

  override def getConcepts(): Array[Concept] = {
    val array = new Array[Concept](1)
    array(0) = Concept.UNKNOWN
    array
  }

  private var log2Freq: Int = 0
  private val posArray = new POSArray()
}

object DomainWordItem {
  def apply(wordName: String, domainType: Int) = {
    val word = new DomainWordItem()
    word.wordName = wordName
    word.domainType = domainType
    word
  }
}
