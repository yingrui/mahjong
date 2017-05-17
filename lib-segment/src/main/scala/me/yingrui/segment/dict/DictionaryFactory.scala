package me.yingrui.segment.dict

import java.io.InputStream

import me.yingrui.segment.concept.ConceptRepository
import me.yingrui.segment.conf.SegmentConfiguration
import me.yingrui.segment.core.MPSegment
import me.yingrui.segment.dict.domain.DomainDictFactory
import me.yingrui.segment.tools.StringWordConverter
import me.yingrui.segment.util.FileUtil

import scala.io.Source

object DictionaryFactory {
  val instance = new DictionaryFactory()

  def apply() = instance
}

class DictionaryFactory {

  private val config = SegmentConfiguration()
  private var coreDict: IDictionary = null
  private var englishDict: TrieDictionary = null
  private var domainFactory: DomainDictFactory = null
  private val isLoadDomainDictionary: Boolean = config.isLoadDomainDictionary()
  private val isLoadUserDictionary: Boolean = config.isLoadUserDictionary()
  private val isLoadEnglishDictionary: Boolean = config.isLoadEnglishDictionary()

  def getCoreDictionary: IDictionary = coreDict

  def getEnglishDictionary: IDictionary = englishDict

  def getDomainDictionary: IDictionary = domainFactory.getDomainDictionary()

  def loadDictionary(): Unit = {
    coreDict = loadDictionary(config.getCoreDictionaryFile())
  }

  def loadDictionary(dictionaryFile: String): IDictionary = {
    System.err.println(s"loading dictionary: $dictionaryFile")
    val inputStream = FileUtil.getResourceAsStream(dictionaryFile)
    val dict = new HashDictionary()
    loadDictionary(inputStream, dict)
    dict
  }

  def loadDictionary(inputStream: InputStream, dictionary: IDictionary) {
    var timestamp = System.currentTimeMillis()
    try {
      loadWords(inputStream) {
        words => words.foreach(dictionary.addWord)
      }
    } catch {
      case e: Throwable =>
        e.printStackTrace()
    } finally {
      timestamp = System.currentTimeMillis() - timestamp
      System.err.println(s"loading dictionary time used(ms): $timestamp")
    }
  }

  private def loadWords(inputStream: InputStream)(convert: (Iterator[IWord]) => Unit) {
    val source = Source.fromInputStream(inputStream, "utf-8")
    val converter = new StringWordConverter()
    converter.setConceptRepository(ConceptRepository())

    val words = source.getLines()
      .map(_.replaceAll("(^\\[)|(,$)|(\\]$)", ""))
      .map(converter.convert)
    convert(words)

    source.close()
  }

  def loadEnglishDictionary() {
    if (isLoadEnglishDictionary) {
      System.err.println(s"loading English dictionary")
      englishDict = new TrieDictionary()
      val inputStream = FileUtil.getResourceAsStream(config.getEnglishDictionaryFile)
      loadDictionary(inputStream, englishDict)
    }
  }

  def loadDomainDictionary() {
    if (isLoadDomainDictionary || isLoadUserDictionary) {
      domainFactory = DomainDictFactory()
      domainFactory.buildDictionary()
    }
  }

  def loadUserDictionary() {
    if (isLoadUserDictionary) {
      var l1 = System.currentTimeMillis()
      val userDictFile = config.getUserDictionaryFile()
      val domainDictionary = domainFactory.getDomainDictionary()
      val userDictionaryLoader = UserDictionaryLoader(domainDictionary, coreDict)
      try {
        userDictionaryLoader.loadUserDictionary(userDictFile)
        val dictionaryService = DictionaryService(coreDict, englishDict, domainDictionary)
        userDictionaryLoader.buildDisambiguationRule(new MPSegment(SegmentConfiguration(), dictionaryService))
      } catch {
        case e: Throwable =>
          e.printStackTrace()
      }
      userDictionaryLoader.clear()
      l1 = System.currentTimeMillis() - l1
      System.err.println((new StringBuilder()).append("loading user dictionary time used(ms): ").append(l1).toString())
    }
  }
}
