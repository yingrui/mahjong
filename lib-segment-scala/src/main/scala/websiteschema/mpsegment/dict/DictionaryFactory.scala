package websiteschema.mpsegment.dict

import domain.DomainDictFactory
import websiteschema.mpsegment.concept.ConceptRepository
import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.tools.StringWordConverter

import io.Source
import websiteschema.mpsegment.core.MPSegment
import websiteschema.mpsegment.util.FileUtil

object DictionaryFactory {
  val instance = new DictionaryFactory()

  def apply() = instance
}

class DictionaryFactory {

  private val config = MPSegmentConfiguration()
  private var coreDict: HashDictionary = null
  private var englishDict: TrieDictionary = null
  private var domainFactory: DomainDictFactory = null
  private val isLoadDomainDictionary: Boolean = config.isLoadDomainDictionary()
  private val isLoadUserDictionary: Boolean = config.isLoadUserDictionary()
  private val isLoadEnglishDictionary: Boolean = config.isLoadEnglishDictionary()

  def getCoreDictionary(): HashDictionary = {
    return coreDict
  }

  def getEnglishDictionary: IDictionary = englishDict

  def loadDictionary() {
    try {
      coreDict = new HashDictionary()
      val list = loadWordStr("websiteschema/mpsegment/dict.txt").sorted

      val converter = new StringWordConverter()
      converter.setConceptRepository(ConceptRepository())
      for (wordStr <- list) {
        val word = converter.convert(wordStr)
        if (word != null) {
          coreDict.addWord(word)
        }
      }
    } catch {
      case e: Throwable =>
        e.printStackTrace()
    }
  }

  private def loadWordStr(dictResource: String): List[String] = {
    val inputStream = getClass.getClassLoader.getResourceAsStream(dictResource)
    val source = Source.fromInputStream(inputStream, "utf-8")
    return source.getLines.toList
  }

  def loadEnglishDictionary() {
    if (isLoadEnglishDictionary) {
      val inputStream = FileUtil.getResourceAsStream(config.getEnglishDictionaryFile)
      var timestamp = System.currentTimeMillis()
      try {
        englishDict = new TrieDictionary()
        val converter = new StringWordConverter()
        val source = Source.fromInputStream(inputStream, "utf-8")
        for (wordStr <- source.getLines()) {
          val word = converter.convert(wordStr)
          if (null != word) {
            englishDict.addWord(word)
          }
        }
      }
      catch {
        case ex: Throwable => ex.printStackTrace()
      }
      finally {
        inputStream.close()
        timestamp = System.currentTimeMillis() - timestamp
        println((new StringBuilder()).append("loading english dictionary time used(ms): ").append(timestamp).toString())
      }
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
        userDictionaryLoader.buildDisambiguationRule(new MPSegment(MPSegmentConfiguration()))
      } catch {
        case e: Throwable =>
          e.printStackTrace()
      }
      userDictionaryLoader.clear()
      l1 = System.currentTimeMillis() - l1
      println((new StringBuilder()).append("loading user dictionary time used(ms): ").append(l1).toString())
    }
  }
}
