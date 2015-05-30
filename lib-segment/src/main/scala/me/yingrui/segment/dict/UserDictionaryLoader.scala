package me.yingrui.segment.dict

import java.io.File

import me.yingrui.segment.conf.MPSegmentConfiguration
import me.yingrui.segment.core._
import me.yingrui.segment.dict.domain.DomainDictionary

import scala.collection.mutable.ListBuffer
import scala.io.Source

object UserDictionaryLoader {
  def apply(domaindictionary: DomainDictionary, hashdictionary: HashDictionary) = {
    val loader = new UserDictionaryLoader()
    loader.domainDict = domaindictionary
    loader.hashDictionary = hashdictionary
    loader
  }
}

class UserDictionaryLoader {

  private val encoding = MPSegmentConfiguration().getDefaultFileEncoding()
  private var domainDict: DomainDictionary = null
  private var hashDictionary: HashDictionary = null
  private val defaultFreq = 50
  private val defaultDomainType = 250
  private val disambiguateRuleArrayList = ListBuffer[String]()
  private val maxDisambiguateIteration = 50

  private def existedInHashDict(s: String): Boolean = {
    return hashDictionary != null && hashDictionary.lookupWord(s) != null
  }

  def clear() {
    disambiguateRuleArrayList.clear()
  }

  def loadUserDictionary(file: String) {
    val encoding = "utf-8"
    try {
      val source = Source.fromInputStream(
        getClass().getClassLoader().getResourceAsStream(file), encoding)
      for (line <- source.getLines; str = line.trim()) {
        if (str.length >= 1) {
          processUserDictLine(str)
        }
      }
      source.close()
    } catch {
      case exception: Throwable =>
        println((new StringBuilder("[UserDictionary] exception:")).append(exception.getMessage()).toString())
        exception.printStackTrace()
    }
  }

  private def processUserDictLine(s: String) {
    var line = s
    var freq = defaultFreq
    var pos = "N"
    if (line.length <= 0 || line.startsWith("#") || line.startsWith("~~")) {
      return
    }
    if (line.indexOf("->") > 0) {
      addDisambiguateRule(s)
    } else if (line.indexOf("=") > 0) {
      val j = line.indexOf("=")
      val s4 = line.substring(0, j)
      val s5 = line.substring(j + 1)
      val as1 = s4.split(",")
      if (as1 != null) {
        for (k <- 0 until as1.length) {
          if (!existedInHashDict(as1(k))) {
            domainDict.pushWord(as1(k), null, "N", defaultFreq, defaultDomainType)
          }
        }

      }
    } else {
      val as = line.split(" ")
      val wordName = as(0).trim()
      if (as.length > 1) {
        as(1) = as(1).trim().toUpperCase()
        if (as(1).length() > 0) {
          pos = as(1)
        }
      }
      if (as.length > 2) {
        as(2) = as(2).trim()
        freq = strToInt(as(2), defaultFreq)
      }
      domainDict.pushWord(wordName, null, pos, freq, defaultDomainType)
    }
  }

  private def strToInt(intStr: String, default: Int): Int = {
    var j = default
    try {
      j = intStr.toInt
    } catch {
      case _: Throwable =>
    }
    return j
  }

  private def addDisambiguateRule(rule: String) {
    disambiguateRuleArrayList += (rule)
  }

  def buildDisambiguationRule(mpSegment: MPSegment) {
    if (disambiguateRuleArrayList.size > 0) {
      for (k <- 0 until disambiguateRuleArrayList.size) {
        val rule = disambiguateRuleArrayList(k)
        val i = rule.indexOf("->")
        val left = rule.substring(0, i)
        val right = rule.substring(i + 2)
        val as = right.split(" ")
        for (i1 <- 0 until as.length) {
          val s6 = as(i1).trim()
          if (getLog2Freq(s6) < 0) {
            domainDict.pushWord(s6, null, "N", defaultFreq, defaultDomainType)
          }
        }
      }
      //            domainDict.updateWordItems()
      var minTFIndex = 0
      for (l2 <- 0 until disambiguateRuleArrayList.size) {
        val line = disambiguateRuleArrayList(l2)
        val j = line.indexOf("->")
        val left = line.substring(0, j)
        val right = line.substring(j + 2)
        var leftFee = getFee(mpSegment, left); //
        val as1 = right.split(" ")
        var rightFee = getFee(as1)
        var k2 = 0;
        while (rightFee > leftFee && k2 < maxDisambiguateIteration) {
          var minTF = 100000
          for (i3 <- 0 until as1.length) {
            val word = as1(i3).trim()
            val tf = getTF(word)
            if (tf < minTF) {
              minTF = tf
              minTFIndex = i3
            }
          }
          val minTFWord = as1(minTFIndex).trim()
          setOccurredCount(minTFWord, minTF + minTF)
          rightFee = getFee(as1)
          k2 += 1
        }
        leftFee = getFee(mpSegment, left)
      }

    }
  }

  private def getFee(mpsegment: MPSegment, s: String): Int = {
    var i = 0
    val segmentResult = mpsegment.segmentMP(s, false)
    for (j <- 0 until segmentResult.length()) {
      i = (i + getLogCorpus()) - getLog2Freq(segmentResult.getWord(j))
    }
    return i
  }

  private def getFee(as: Array[String]): Int = {
    var i = 0
    for (j <- 0 until as.length) {
      i = (i + getLogCorpus()) - getLog2Freq(as(j))
    }
    return i
  }

  private def getLog2Freq(s: String): Int = {
    val word = getItem(s)
    if (word != null) {
      return word.getLog2Freq()
    } else {
      return -1
    }
  }

  private def setOccurredCount(s: String, factor: Int) {
    val word = getItem(s)
    if (word != null) {
      word.setOccuredSum(factor)
    }
  }

  def loadStopWord(s: String): List[String] = {
    val file = new File(s)
    val arrayList = ListBuffer[String]()
    if (file.isFile() && file.exists()) {
      try {
        val source = Source.fromFile(
          getClass().getClassLoader().getResource(s).toURI, encoding)
        for (line <- source.getLines; str = line.trim()) {
          if (str.length >= 1 && str.startsWith("~~")) {
            arrayList += str
          }
        }
        source.close()
      } catch {
        case ex: Throwable =>
          println((new StringBuilder("[UserDictionary] exception:")).append(ex.getMessage()).toString())
          ex.printStackTrace()
      }
    } else {
      println((new StringBuilder("[UserDictionary] ")).append(s).append(" 不存在！").toString())
    }
    return arrayList.toList
  }

  private def getItem(wordStr: String): IWord = {
    var word: IWord = null
    if (wordStr.length() > 1) {
      word = domainDict.getWord(wordStr)
    }
    if (word == null) {
      word = hashDictionary.getWord(wordStr)
    }
    return word
  }

  private def getTF(wordStr: String): Int = {
    val iworditem = getItem(wordStr)
    var tf = 0
    if (iworditem != null) {
      tf = iworditem.getOccuredSum().toInt
    }
    return tf
  }

  private def getLogCorpus(): Int = {
    return MPSegmentConfiguration.LOG_CORPUS.toInt
  }
}
