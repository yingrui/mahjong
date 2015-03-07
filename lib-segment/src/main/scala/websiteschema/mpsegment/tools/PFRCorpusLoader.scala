package websiteschema.mpsegment.tools

import websiteschema.mpsegment.concept.Concept
import websiteschema.mpsegment.core.SegmentResult
import websiteschema.mpsegment.dict.DictionaryFactory
import websiteschema.mpsegment.dict.HashDictionary
import websiteschema.mpsegment.dict.POSUtil

import java.io.{BufferedReader, _}
import collection.mutable._

class PFRCorpusLoader {

  private var reader: BufferedReader = null
  private var dictionary: HashDictionary = null
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

    val words = ListBuffer[String]()
    val wordStartAts = new Array[Int](elements.length - 1)
    val wordEndAts = new Array[Int](elements.length - 1)
    val posArray = new Array[Int](elements.length - 1)
    val domainTypes = new Array[Int](elements.length - 1)
    val concepts = new Array[String](elements.length - 1)

    var firstIndex = -1
    for (i <- 0 until (elements.length - 1)) {
      var ele = elements(i + 1)
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
      val info = ele.split("/")
      val wordStr = info(0)
      val posStr = info(1).toUpperCase()
      if (posStr.equalsIgnoreCase(POSUtil.getPOSString(POSUtil.POS_NR))) {
        setDomainType(domainTypes, i, POSUtil.POS_NR)
      }
      concepts(i) = getConcept(wordStr, posStr)
      wordStartAts(i) = words.foldLeft(0){(sum, word) => sum + word.length}
      words += (wordStr)
      wordEndAts(i) = words.foldLeft(0){(sum, word) => sum + word.length}
      posArray(i) = POSUtil.getPOSIndex(posStr)
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

  def getConcept(wordStr: String, posStr: String): String = {
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

object PFRCorpusLoader {
  def apply(inputStream: InputStream) = createCorpusLoader(inputStream, "utf-8")

  def apply(inputStream: InputStream, encoding: String) = createCorpusLoader(inputStream, encoding)

  def createCorpusLoader(inputStream: InputStream, encoding: String): PFRCorpusLoader = {
    val loader = createCorpusLoader
    loader.reader = new BufferedReader(new InputStreamReader(inputStream, encoding))
    loader
  }

  def apply() = createCorpusLoader

  def createCorpusLoader: PFRCorpusLoader = {
    val loader = new PFRCorpusLoader()
    loader.dictionary = DictionaryFactory().getCoreDictionary()
    if (loader.dictionary == null) {
      DictionaryFactory().loadDictionary()
      loader.dictionary = DictionaryFactory().getCoreDictionary()
    }
    loader.eliminatedDomainTypes = HashSet[Int]()
    loader
  }
}
