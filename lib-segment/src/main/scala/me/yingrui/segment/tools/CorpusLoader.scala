package me.yingrui.segment.tools

import java.io.{BufferedReader, _}

import me.yingrui.segment.concept.Concept
import me.yingrui.segment.core.SegmentResult
import me.yingrui.segment.dict.{DictionaryFactory, IDictionary, POSUtil}

import scala.collection.mutable._

class CorpusLoader {

  private var reader: BufferedReader = null
  private var dictionary: IDictionary = null
  private var eliminatedDomainTypes: Set[Int] = null

  def eliminateDomainType(domainType: Int) {
    eliminatedDomainTypes.add(domainType)
  }

  def load(fn: (SegmentResult) => Unit): Unit = {
    var sentence = readLine()
    while (sentence != null) {
      fn(sentence)
      sentence = readLine()
    }
  }

  def readLine(): SegmentResult = {
    val line = reader.readLine()
    if (line == null || "".equals(line.trim()))
      return null

    return buildSegmentResult(line)
  }

  def buildSegmentResult(line: String): SegmentResult = {
    val elements = line.split("\\s+")

    if (elements(0).matches("\\d{8}-\\d{2}-\\d{3}-\\d{3}/m")) {
      toSegmentResult(elements.slice(1, elements.length))
    } else {
      toSegmentResult(elements)
    }
  }

  private def toSegmentResult(elements: Array[String]): SegmentResult = {
    val words = ListBuffer[String]()
    val wordStartAts = new Array[Int](elements.length)
    val wordEndAts = new Array[Int](elements.length)
    val posArray = new Array[Int](elements.length)
    val domainTypes = new Array[Int](elements.length)
    val concepts = new Array[String](elements.length)

    var firstIndex = -1
    for (i <- 0 until elements.length) {
      var ele = elements(i)
      domainTypes(i) = 0
      if (ele.startsWith("[")) {
        firstIndex = i
        ele = ele.substring(1)
      }
      if (ele.contains("]")) {
        val conceptInfo = ele.split("]")
        ele = conceptInfo(0)
        val domainType = POSUtil.getPOSIndex(conceptInfo(1).toUpperCase())
        setDomainType(domainTypes, firstIndex, i, domainType)
        firstIndex = -1
      }
      val indexOfSlash = ele.lastIndexOf("/")
      val info = if (indexOfSlash > 0) Array(ele.substring(0, indexOfSlash), ele.substring(indexOfSlash + 1)) else Array(ele)
      val wordStr = info(0)
      if (info.length > 1) {
        val posStr = info(1).toUpperCase()
        if (posStr.equalsIgnoreCase(POSUtil.getPOSString(POSUtil.POS_NR))) {
          setDomainType(domainTypes, i, POSUtil.POS_NR)
        }
        concepts(i) = getConcept(wordStr, posStr)
        posArray(i) = POSUtil.getPOSIndex(posStr)
      }
      wordStartAts(i) = words.foldLeft(0) { (sum, word) => sum + word.length }
      words += (wordStr)
      wordEndAts(i) = words.foldLeft(0) { (sum, word) => sum + word.length }
    }

    val result = new SegmentResult(words.size)
    result.setWords(words.toArray)
    result.setWordStartAts(wordStartAts)
    result.setWordEndAts(wordEndAts)
    result.setPOSArray(posArray)
    result.setDomainTypes(domainTypes)
    result.setConcepts(concepts)
    return result
  }

  private def setDomainType(domainTypes: Array[Int], index: Int, domainType: Int) {
    setDomainType(domainTypes, index, index, domainType)
  }

  private def setDomainType(domainTypes: Array[Int], startIndex: Int, endIndex: Int, domainType: Int) {
    if (!eliminatedDomainTypes.contains(domainType)) {
      for (j <- startIndex to endIndex) {
        domainTypes(j) = domainType
      }
    }
  }

  private def getConcept(wordStr: String, posStr: String): String = {
    val word = dictionary.lookupWord(wordStr)
    if (null != word) {
      for (concept <- word.getConcepts()) {
        if (concept.getName().startsWith(posStr.substring(0, 1).toLowerCase())) {
          return concept.getName()
        } else if (POSUtil.getPOSIndex(posStr) == POSUtil.POS_J && concept.getName().startsWith("n")) {
          return concept.getName()
        }
      }
    }
    return Concept.UNKNOWN.getName()
  }
}

object CorpusLoader {
  def apply(inputStream: InputStream) = createCorpusLoader(inputStream, "utf-8")

  def apply(inputStream: InputStream, encoding: String) = createCorpusLoader(inputStream, encoding)

  def createCorpusLoader(inputStream: InputStream, encoding: String): CorpusLoader = {
    val loader = createCorpusLoader
    loader.reader = new BufferedReader(new InputStreamReader(inputStream, encoding))
    loader
  }

  def apply() = createCorpusLoader

  def createCorpusLoader: CorpusLoader = {
    val loader = new CorpusLoader()
    loader.dictionary = DictionaryFactory().getCoreDictionary
    if (loader.dictionary == null) {
      DictionaryFactory().loadDictionary()
      loader.dictionary = DictionaryFactory().getCoreDictionary
    }
    loader.eliminatedDomainTypes = HashSet[Int]()
    loader
  }

  def convertToSegmentResult(line: String) = {
    val stream = convertToInputStream(line)
    val segmentResult = CorpusLoader(stream).readLine()
    stream.close()
    segmentResult
  }

  private def convertToInputStream(line: String): InputStream = new ByteArrayInputStream(line.getBytes("utf-8"))
}
