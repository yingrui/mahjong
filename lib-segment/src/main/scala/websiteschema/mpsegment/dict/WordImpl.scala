package websiteschema.mpsegment.dict

import websiteschema.mpsegment.concept.Concept

class WordImpl(wordName: String) extends IWord with Comparable[Object] {

  private var domainType: Int = 0
  private var log2Freq: Int = 0
  private var posArray: POSArray = new POSArray
  private var concepts: Array[Concept] = null

  posArray.add(POS("UN", 200))

  override def setOccuredCount(posName: String, freq: Int) {
    val posTable = posArray.getWordPOSTable()
    posTable.find(
      array => posName.equals(POSUtil.getPOSString(array(0)))
    ) match {
      case Some(array) =>
        array(1) = freq; calculateLogFreq()
      case _ =>
    }
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

  override def getPOSArray(): POSArray = {
    return posArray
  }

  override def getWordPOSTable(): Array[Array[Int]] = {
    return getPOSArray().getWordPOSTable()
  }

  def setPosArray(posArray: POSArray) {
    this.posArray = posArray
  }

  override def getWordMaxPOS(): Int = {
    var maxOccuredPOS = POSUtil.POS_UNKOWN
    val maxOccured = 0
    val posTable = posArray.getWordPOSTable()
    for (i <- 0 until posTable.length) {
      val freq = posTable(i)(1)
      if (freq > maxOccured) {
        maxOccuredPOS = posTable(i)(0)
      }
    }

    return maxOccuredPOS
  }

  override def getWordName(): String = {
    return wordName
  }

  override def getWordLength(): Int = {
    return wordName.length()
  }

  override def setDomainType(i: Int) {
    domainType = i
  }

  override def getDomainType(): Int = {
    return domainType
  }

  override def getLog2Freq(): Int = {
    if (log2Freq == 0) {
      calculateLogFreq()
    }
    return log2Freq
  }

  private def calculateLogFreq() {
    log2Freq = (Math.log(getOccuredSum() + 1L) * 100D).toInt
  }

  override def getOccuredSum(): Int = {
    val posTable = getPOSArray().getWordPOSTable()
    var occuredSum = 0
    for (i <- 0 until posTable.length) {
      occuredSum += posTable(i)(1)
    }
    return occuredSum
  }

  override def getOccuredCount(s: String): Int = {
    val pos = POSUtil.getPOSIndex(s)
    val posTable = getPOSArray().getWordPOSTable()
    for (i <- 0 until posTable.length) {
      if (posTable(i)(0) == pos) {
        return posTable (i)(1)
      }
    }
    return 0
  }

  override def incOccuredCount(s: String) {
    setOccuredCount(s, getOccuredCount(s) + 1)
  }

  override def compareTo(obj: Object): Int = {
    if (obj != null && (obj.isInstanceOf[String])) {
      return wordName.compareTo(obj.asInstanceOf[String])
    }
    if (obj != null && (obj.isInstanceOf[WordImpl])) {
      return wordName.compareTo(obj.asInstanceOf[WordImpl].getWordName())
    } else {
      return 1
    }
  }

  def equals(obj: WordImpl): Boolean = {
    if (obj != null) {
      return wordName.equals(obj.getWordName())
    }else{
      return false
    }
  }

  override def toString(): String = {
    val stringBuilder = new StringBuilder()
    stringBuilder.append((new StringBuilder(String.valueOf(getWordName()))).append("\n").toString())
    stringBuilder.append(getPOSArray().toString())
    return stringBuilder.toString()
  }

  override def getConcepts(): Array[Concept] =
    if (null != concepts) {
      concepts
    } else {
      val array = new Array[Concept](1)
      array(0) = Concept.UNKNOWN
      array
    }


  def setConcepts(concepts: Array[Concept]) {
    this.concepts = concepts
  }

}
