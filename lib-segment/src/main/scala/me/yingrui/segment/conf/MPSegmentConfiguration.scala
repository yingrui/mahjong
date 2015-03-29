package me.yingrui.segment.conf

import io.Source
import collection.Map
import java.net.URL

class MPSegmentConfiguration {

  def setHomePath(path: String) {
    val i1 = path.length()
    if (i1 >= 1) {
      if (path.charAt(i1 - 1) == '/' || path.charAt(i1 - 1) == '\\') {
        homePath = path
      } else {
        homePath = (new StringBuilder(String.valueOf(path))).append("/").toString()
      }
    }
  }

  def is(property: String) = if (properties.contains(property)) properties(property).toBoolean else false
  def get(property: String) = if (properties.contains(property)) properties(property) else ""

  def getDefaultFileEncoding(): String = {
    return defaultFileEncoding
  }

  def getNameRecognizer = properties.getOrElse("name.recognizer", "HmmNameFilter")

  def getPOSMatrix(): String = {
    return new StringBuilder(getHomePath()).
      append(properties.getOrElse("resource.pos", "pos.dat")).toString()
  }

  def getChNameDict(): String = {
    return new StringBuilder(getHomePath()).
      append(properties.getOrElse("resource.chinesename", "ChName.dict")).toString()
  }

  def isLoadDomainDictionary(): Boolean = {
    return loaddomaindictionary
  }

  def isLoadUserDictionary(): Boolean = {
    return loaduserdictionary
  }

  def isLoadEnglishDictionary(): Boolean = {
    return loadEnglishDictionary
  }

  def isUseContextFreqSegment(): Boolean = {
    return useContextFreqSegment
  }

  def getUserDictionaryFile(): String = {
    return getHomePath() + properties.getOrElse("user.dictionaryfile", "userdict.txt")
  }

  def getEnglishDictionaryFile(): String = {
    return getHomePath() + properties.getOrElse("english.dictionaryfile", "dict-en.txt")
  }

  def isSupportQuerySyntax(): Boolean = {
    return querysyntax
  }

  def getStopPosList(): String = {
    return properties.getOrElse("filter.wordbypos", "")
  }

  def isSegmentMin(): Boolean = {
    return segment_min
  }

  def getGlueChar(): String = {
    return properties.getOrElse("glue.queryoperater", "*?~/Array[_]:")
  }

  def getMaxQueryLength(): Int = {
    return properties.getOrElse("maximum.querylength", "512").toInt
  }

  def isChineseNameIdentify(): Boolean = {
    return chinesenameidentify
  }

  def isXingMingSeparate(): Boolean = {
    return xingmingseparate
  }

  def isHalfShapeAll(): Boolean = {
    return halfshapeall
  }

  def isUpperCaseAll(): Boolean = {
    return uppercaseall
  }

  def getMaxWordLength(): Int = {
    return properties.getOrElse("maximum.wordlength", "8").toInt
  }

  def getDomainDictLoader(): String = {
    return properties.getOrElse("dictionary.loaders", "")
  }

  def isWithPinyin(): Boolean = {
    return withPinyin
  }

  def isRecognizePOS() = recognizePOS

  def getPinyinModel(): String = {
    return properties.getOrElse("pinyin.model", "wtp.m")
  }

  private def initialize() = {
    stopwordfilter = properties.getOrElse("filter.stopword", "false").toBoolean
    querysyntax = properties.getOrElse("support.querysyntax", "false").toBoolean
    loaduserdictionary = properties.getOrElse("load.userdictionary", "true").toBoolean
    loaddomaindictionary = properties.getOrElse("load.domaindictionary", "true").toBoolean
    loadEnglishDictionary = properties.getOrElse("load.englishdictionary", "true").toBoolean
    segment_min = properties.getOrElse("minimize.word", "false").toBoolean
    chinesenameidentify = properties.getOrElse("recognize.chinesename", "true").toBoolean
    recognizePOS = properties.getOrElse("recognize.partOfSpeech", "true").toBoolean
    xingmingseparate = properties.getOrElse("separate.xingming", "false").toBoolean
    halfshapeall = properties.getOrElse("convert.tohalfshape", "false").toBoolean
    uppercaseall = properties.getOrElse("convert.touppercase", "false").toBoolean
    withPinyin = properties.getOrElse("recognize.pinyin", "false").toBoolean
    useContextFreqSegment = properties.getOrElse("segment.context", "false").toBoolean
    this
  }

  private def getHomePath(): String = {
    return homePath
  }

  private def load() {
    val resource: URL = getClass.getClassLoader.getResource("maxprob.cfg")
    if (null != resource) {
      val configFile = resource.toURI
      val source = Source.fromFile(configFile)
      val re = "^\\s*([\\w\\.]+)\\s*=\\s*([^\\s]+)\\s*$".r
      for (line <- source.getLines; m <- re.findFirstMatchIn(line)) {
        properties += (m.group(1) -> m.group(2))
      }
    }
  }

  private var properties: collection.mutable.Map[String, String] = null
  private var homePath = ""
  private var defaultFileEncoding = "gbk"
  private var withPinyin = false
  private var loaddomaindictionary = false
  private var loaduserdictionary = false
  private var recognizePOS = true
  private var loadEnglishDictionary = false
  private var useContextFreqSegment = false
  private var segment_min = false
  private var querysyntax = false
  //TODO: add test case for stop word filter.
  private var stopwordfilter = false
  private var chinesenameidentify = true
  private var xingmingseparate = true
  private var halfshapeall = false
  private var uppercaseall = false
}

object MPSegmentConfiguration {

  private val defaultProperties = collection.mutable.Map[String, String](
    "filter.stopword" -> "true",
    "filter.wordbypos" -> "r,a,z,d,p,c,u,y,e,o,w",
    "load.stopwordfile" -> "stopword.txt",
    "recognize.pinyin" -> "false",
    "pinyin.model" -> "me/yingrui/segment/wtp.m",
    "separate.xingming" -> "false",
    "minimize.word" -> "true",
    "maximum.wordlength" -> "25",
    "support.querysyntax" -> "false",
    "maximum.querylength" -> "512",
    "glue.queryoperater" -> "*?~/_[]:",
    "load.userdictionary" -> "true",
    "user.dictionaryfile" -> "userdict.txt",
    "load.domaindictionary" -> "false",
    "convert.tohalfshape" -> "false",
    "convert.touppercase" -> "false")


  private val instance = new MPSegmentConfiguration()
  instance.properties = collection.mutable.Map[String, String]() ++= defaultProperties
  instance.load()
  instance.initialize()

  def SectionSize = 1024

  val LOG_CORPUS = Math.log(8000000) * 100D

  def apply() = instance

  def apply(config: Map[String, String]) = {
    val conf = new MPSegmentConfiguration()
    conf.properties = collection.mutable.Map[String, String]() ++ instance.properties ++ config
    conf.initialize
  }
}