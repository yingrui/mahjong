package websiteschema.mpsegment.dict

import domain.DomainDictFactory
import websiteschema.mpsegment.concept.ConceptRepository
import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.tools.StringWordConverter

import io.Source
import websiteschema.mpsegment.core.MPSegment
import websiteschema.mpsegment.util.FileUtil
import scala.util.parsing.json.{JSON, JSONObject}
import java.io.InputStream

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
    var timestamp = System.currentTimeMillis()
    try {
      coreDict = new HashDictionary()
      val inputStream = getClass.getClassLoader.getResourceAsStream("websiteschema/mpsegment/dict.txt")

      loadDictionary(inputStream, coreDict)
    } catch {
      case e: Throwable =>
        e.printStackTrace()
    } finally {
      timestamp = System.currentTimeMillis() - timestamp
      println(s"loading dictionary time used(ms): $timestamp")
    }

  }

  def loadDictionary(inputStream: InputStream, dictionary: IDictionary) {
    val converter = new StringWordConverter()
    converter.setConceptRepository(ConceptRepository())

    loadWords(inputStream) {
      wordString: String => {
        val word = converter.convert(wordString)
        dictionary addWord word
      }
    }
  }

  private def loadWords(inputStream: InputStream)(convert : (String) => Unit) {
    val source = Source.fromInputStream(inputStream, "utf-8")

    source.getLines().foreach(wordString => {
       convert(wordString.replaceAll("(^\\[)|(,$)|(\\]$)", ""))
    })

    source.close()
  }

  def loadEnglishDictionary() {
    if (isLoadEnglishDictionary) {
      var timestamp = System.currentTimeMillis()
      englishDict = new TrieDictionary()
      val inputStream = FileUtil.getResourceAsStream(config.getEnglishDictionaryFile)
      try {
        loadDictionary(inputStream, englishDict)
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
