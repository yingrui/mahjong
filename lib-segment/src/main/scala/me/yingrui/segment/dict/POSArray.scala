package me.yingrui.segment.dict

import collection.mutable.LinkedHashMap

class POSArray {

  var arrayPOSAndFreq: Array[Array[Int]] = null
  // TODO: remove posTable, use array of Int only.
  var posTable = LinkedHashMap[String, POS]()

  def setPOSCount(partOfSpeech: String, freq: Int) {
    if (partOfSpeech == null || partOfSpeech.trim().equals("")) {
      return
    }
    var pos = posTable.getOrElse(partOfSpeech.trim(), null)
    if (pos != null) {
      pos.setCount(freq)
    } else {
      pos = POS(partOfSpeech.trim(), freq)
    }
    posTable += (partOfSpeech.trim() -> pos)
  }

  def setPOSCount(count: Int) {
    var discount = getOccurredSum().toDouble
    if (discount > 0.0D) {
      discount = count.toDouble / discount
    }
    for (name <- posTable.keys) {
      val pos = posTable(name)
      val d1 = pos.getCount().toDouble * discount
      setPOSCount(name, d1.toInt)
    }
  }

  def add(posArray: POSArray) {
    if (posArray == null && arrayPOSAndFreq == null) {
      return
    }

    if (posArray == null) {
      posTable = LinkedHashMap[String, POS]()
      for (i <- 0 until arrayPOSAndFreq.length) {
        val posString = POSUtil.getPOSString(arrayPOSAndFreq(i)(0))
        posTable += (posString -> POS(posString, arrayPOSAndFreq(i)(1)))
      }
    }

    val arrayPosTable = posArray.getWordPOSTable()
    for (i <- 0 until arrayPosTable.length) {
      val posString = POSUtil.getPOSString(arrayPosTable(i)(0))
      add(POS(posString, arrayPosTable(i)(1)))
    }
  }

  def add(pos: POS) {
    if (pos == null) {
      return
    }
    var a2 = posTable.getOrElse(pos.getName(), null)
    if (a2 != null) {
      a2.setCount(a2.getCount() + pos.getCount())
    } else {
      a2 = POS(pos.getName(), pos.getCount())
    }
    posTable += (a2.getName() -> a2)
  }

  def incPOSCount(s: String) {
    if (s == null || s.trim().equals("")) {
      return
    }
    var a1 = posTable.getOrElse(s.trim(), null)
    if (a1 != null) {
      a1.setCount(a1.getCount() + 1)
    } else {
      a1 = POS(s.trim(), 1)
    }
    posTable += (s.trim() -> a1)
  }

  def getSize(): Int = {
    return posTable.size
  }

  def getWordPOSTable(): Array[Array[Int]] = {
    if (null == arrayPOSAndFreq) {
      arrayPOSAndFreq = buildPOSArray()
    }
    return arrayPOSAndFreq
  }

  def buildPOSArray(): Array[Array[Int]] = {
    val arrayPOS = new Array[POS](getSize())
    var i = 0
    for (name <- posTable.keys) {
      arrayPOS(i) = posTable(name)
      i += 1
    }
    val posAndFreq = new Array[Array[Int]](arrayPOS.length)
    for (j <- 0 until arrayPOS.length) {
      posAndFreq(j) = new Array[Int](2)
      posAndFreq(j)(0) = POSUtil.getPOSIndex(arrayPOS(j).getName())
      posAndFreq(j)(1) = arrayPOS(j).getCount()
    }
    posTable.clear()
    posTable = null
    this.arrayPOSAndFreq = posAndFreq
    return posAndFreq
  }

  def getWordPOSTable(arrayPOSAndFreq: Array[Array[Int]]): Int = {
    var i = 0
    var j = 0
    for (name <- posTable.keys) {
      val pos = posTable(name)
      if (j < arrayPOSAndFreq.length) {
        arrayPOSAndFreq(j)(0) = POSUtil.getPOSIndex(pos.getName())
        arrayPOSAndFreq(j)(1) = pos.getCount()
      }
      j += 1
    }

    i = j
    for (k <- i until arrayPOSAndFreq.length) {
      arrayPOSAndFreq(k)(0) = 0
      arrayPOSAndFreq(k)(1) = 0
    }

    return i
  }

  def getWordMaxPOS(): Int = {
    var posIndex: Int = 1
    var count = 0
    for (name <- posTable.keys) {
      val pos = posTable(name)
      if (pos.getCount() > count) {
        posIndex = POSUtil.getPOSIndex(pos.getName())
        count = pos.getCount()
      }
    }

    posIndex
  }

  def getOccurredCount(s: String): Int = {
    if (s == null || s.trim().equals("")) {
      return 0
    }
    val pos = posTable.getOrElse(s.trim(), null)
    if (pos == null) {
      return 0
    } else {
      return pos.getCount()
    }
  }

  def getOccurredSum(): Int = {
    var sum = 0
    val posAndFreq = getWordPOSTable()
    for (i <- 0 until posAndFreq.length) {
      sum += posAndFreq(i)(1)
    }

    return sum
  }

  override def toString(): String = {
    val stringBuilder = new StringBuilder()
    for (i <- 0 until arrayPOSAndFreq.length) {
      val pos = POS(POSUtil.getPOSString(arrayPOSAndFreq(i)(0)), arrayPOSAndFreq(i)(1))
      stringBuilder.append((new StringBuilder(String.valueOf(pos.toString()))).append("\n").toString())
    }

    return stringBuilder.toString()
  }

  def toDBFString(s: String): String = {
    val stringBuilder = new StringBuilder()
    for (i <- 0 until arrayPOSAndFreq.length) {
      val pos = POS(POSUtil.getPOSString(arrayPOSAndFreq(i)(0)), arrayPOSAndFreq(i)(1))
      stringBuilder.append((new StringBuilder(String.valueOf(s))).append(pos.toDBFString()).append("\n").toString())
    }

    return stringBuilder.toString()
  }

}
