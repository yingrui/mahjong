package websiteschema.mpsegment.dict

import websiteschema.mpsegment.util.FileUtil
import websiteschema.mpsegment.util.SerializeHandler

import scala.collection.mutable.Map
import java.io.DataInputStream
import java.io.File

class ChNameDictionary {

  def isXing(familyName: String): Boolean = {
    return xingHashMap.containsKey(familyName)
  }

  def computeLgLP3(familyName: String, fisrtNameWord: String, secondNameWord: String): Double = {
    val d2 = getXingProb(familyName)
    val d3 = getMingFreq0(fisrtNameWord)
    val d4 = getMingFreq1(secondNameWord)
    return getNameWordProb(d2, d3, d4)
  }

  def computeLgLP3_2(s: String, s1: String): Double = {
    val d2 = getXingProb(s.substring(0, 1))
    val d3 = getMingFreq0(s.substring(1))
    val d4 = getMingFreq1(s1)
    return getNameWordProb(d2, d3, d4)
  }

  def computeLgLP2(s: String, s1: String): Double = {
    val d2 = getXingProb(s)
    val d3 = getMingFreq2(s1)
    return getNameWordProb(d2, d3)
  }

  def computeLgMing23(s: String, s1: String): Double = {
    var d1 = getMingFreq0(s).toDouble
    val d2 = getMingFreq1(s1)
    d1 += d2
    d1 /= 1000D
    return d1
  }

  def getRightBoundaryWordLP(s: String): Double = {
    val index = get(rightBoundaryHashMap, s)
    var d = 0.0D
    if (index > 0) {
      d = (rightBoundaryProbs(index) - 0.10000000000000001D) / 3D
    }
    return d
  }

  def outSummary() {
    println((new StringBuilder("xingTop=")).append(xingTop).toString())
    println((new StringBuilder("mingTop=")).append(mingTop).toString())
    for (i1 <- 0 until 3) {
      println((new StringBuilder("totalMingProb(i)=")).append(totalMingProb(i1)).toString())
    }
  }

  private def getXingProb(s: String): Int = {
    var index = get(xingHashMap, s)
    var prob = 0.0D
    if (index <= 0) {
      index = 0
    } else {
      prob = xingFreq(index).toDouble * (1.0D + xingProb(index))
    }
    return prob.toInt
  }

  private def get(map: java.util.Map[String, Int], key: String): Int = {
    if (map.containsKey(key)) {
      return map.get(key)
    }
    return 0
  }

  private def getMingFreq0(name: String): Int = {
    var index = get(mingHashMap, name)
    if (index <= 0) {
      index = 0
    } else {
      index = mingFreqs(index)(0)
    }
    return index
  }

  private def getMingFreq1(name: String): Int = {
    var index = get(mingHashMap, name)
    if (index <= 0) {
      index = 0
    } else {
      index = mingFreqs(index)(1)
    }
    return index
  }

  private def getMingFreq2(name: String): Int = {
    var index = get(mingHashMap, name)
    if (index <= 0) {
      index = 0
    } else {
      index = mingFreqs(index)(2)
    }
    return index
  }

  private def getNameWordProb(d2: Double, d3: Double, d4: Double): Double = {
    var d = d2 * factor * ((d3 + d4) / 1000000D)
    if (d4 <= 0.0D && d > 1.0D) {
      d *= 0.90000000000000002D
    }
    return d
  }

  private def getNameWordProb(d2: Double, d3: Double): Double = {
    val d = d2 * (d3 / 1000000D)
    return d
  }

  def toText(): String = {
    val lineSeparator = System.getProperty("line.separator")
    var space = " "
    val sb = new StringBuilder()
    sb.append("//ChName.dict").append(lineSeparator)
    sb.append("[xingHashMap] //").append(xingHashMap.size).append(lineSeparator)
    val xingHashMapKeys = new java.util.ArrayList[String](xingHashMap.keySet())
    var i = 0
    while (i < xingHashMapKeys.size()) {
      val key = xingHashMapKeys.get(i)
      sb.append(key).append(space).append(xingHashMap.get(key)).append(space)
      i += 1
    }
    sb.append(lineSeparator)
    sb.append("[xingFreq] //").append(xingFreq.length).append(lineSeparator)
    for (i <- 0 until xingFreq.length) {
      sb.append(xingFreq(i)).append(space)
    }

    sb.append(lineSeparator)
    sb.append("[mingHashMap] //").append(mingHashMap.size).append(lineSeparator)

    val mingHashMapKeys = new java.util.ArrayList[String](mingHashMap.keySet())
    i = 0
    while (i < mingHashMapKeys.size()) {
      val key = mingHashMapKeys.get(i)
      sb.append(key).append(space).append(mingHashMap.get(key)).append(space)
      i += 1
    }
    sb.append(lineSeparator)
    sb.append("[mingFreqs] //").append(mingFreqs.length).append(lineSeparator)
    for (i <- 0 until mingFreqs.length) {
      for (j <- 0 until 3) {
        sb.append(mingFreqs(i)(j)).append(space)
      }
      sb.append("\t")
    }

    sb.append(lineSeparator)
    sb.append("[totalMingProb] //").append(totalMingProb.length).append(lineSeparator)
    for (i <- 0 until totalMingProb.length) {
      sb.append(totalMingProb(i)).append(space)
    }

    sb.append(lineSeparator)
    sb.append("[fuXing] //").append(fuXing.size).append(lineSeparator)
    val fuXingKeys = new java.util.ArrayList[String](fuXing.keySet())
    i = 0
    while (i < fuXingKeys.size()) {
      val key = fuXingKeys.get(i)
      sb.append(key).append(space).append(fuXing.get(key)).append(space)
      i += 1
    }
    sb.append(lineSeparator)
    sb.append("[xingProb] //").append(xingProb.length).append(lineSeparator)
    for (i <- 0 until xingProb.length) {
      sb.append(xingProb(i)).append(space)
    }
    sb.append(lineSeparator)
    sb.append("[rightBoundaryHashMap] //").append(rightBoundaryHashMap.size).append(lineSeparator)
    val rightBoundaryHashMapKeys = new java.util.ArrayList[String](rightBoundaryHashMap.keySet())
    i = 0
    while (i < rightBoundaryHashMapKeys.size()) {
      val key = rightBoundaryHashMapKeys.get(i)
      sb.append(key).append(space).append(rightBoundaryHashMap.get(key)).append(space)
      i += 1
    }
    sb.append(lineSeparator)
    sb.append("[rightBoundaryProbs] //").append(rightBoundaryProbs.length).append(lineSeparator)
    for (i <- 0 until rightBoundaryProbs.length) {
      sb.append(rightBoundaryProbs(i)).append(space)
    }
    sb.append(lineSeparator)
    return sb.toString()
  }

  def saveNameDict(dictFile: String) {
    try {
      val writeHandler = SerializeHandler(new File(dictFile), SerializeHandler.MODE_WRITE_ONLY)
      writeHandler.serializeMapStringInt(xingHashMap)
      writeHandler.serializeArrayInt(xingFreq)
      writeHandler.serializeMapStringInt(mingHashMap)
      writeHandler.serialize2DArrayInt(mingFreqs)
      writeHandler.serializeArrayDouble(totalMingProb)
      writeHandler.serializeMapStringInt(fuXing)
      writeHandler.serializeArrayDouble(xingProb)
      writeHandler.serializeMapStringInt(rightBoundaryHashMap)
      writeHandler.serializeArrayDouble(rightBoundaryProbs)
      writeHandler.close()
    } catch {
      case exception: Throwable =>
        System.err.println((new StringBuilder("Error: saveNameDict.save(")).append(dictFile).append(") ").append(exception.getMessage()).toString())
    }
  }

  def loadNameDict(dictFile: String) {
    try {
      var objectinputstream: DataInputStream = null
      var f = new File(dictFile)
      var readHandler: SerializeHandler = null
      if (f.exists()) {
        readHandler = SerializeHandler(f, SerializeHandler.MODE_READ_ONLY)
      } else {
        objectinputstream = new DataInputStream(
          FileUtil.getResourceAsStream(dictFile))
        readHandler = SerializeHandler(objectinputstream)
      }
      xingHashMap = readHandler.deserializeMapStringInt()
      xingFreq = readHandler.deserializeArrayInt()
      mingHashMap = readHandler.deserializeMapStringInt()
      mingFreqs = readHandler.deserialize2DArrayInt()
      totalMingProb = readHandler.deserializeArrayDouble()
      fuXing = readHandler.deserializeMapStringInt()
      xingProb = readHandler.deserializeArrayDouble()
      rightBoundaryHashMap = readHandler.deserializeMapStringInt()
      rightBoundaryProbs = readHandler.deserializeArrayDouble()
      objectinputstream.close()
    } catch {
      case exception: Throwable =>
        System.err.println((new StringBuilder()).append(dictFile).append("没找到！").append(exception.getMessage()).toString())
    }
  }

  private val factor: Double = 0.88400000000000001D
  private var xingHashMap: java.util.Map[String, Int] = null
  private var xingFreq: Array[Int] = null
  private var xingProb: Array[Double] = null
  private var mingHashMap: java.util.Map[String, Int] = null
  private var mingFreqs: Array[Array[Int]] = null
  private var totalMingProb: Array[Double] = null
  private val xingTop: Int = 1
  private val mingTop: Int = 1
  private var fuXing: java.util.Map[String, Int] = null

  def getFuXing() = fuXing

  private var rightBoundaryHashMap: java.util.Map[String, Int] = null
  private var rightBoundaryProbs: Array[Double] = null
}
