package websiteschema.mpsegment.dict

import domain.DomainDictFactory
import websiteschema.mpsegment.concept.ConceptRepository
import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.tools.StringWordConverter

import io.Source
import websiteschema.mpsegment.core.MPSegment

object DictionaryFactory {
  val instance = new DictionaryFactory()

  def apply() = instance
}

class DictionaryFactory {

    private val config = MPSegmentConfiguration()
    private var coreDict : HashDictionary = null
    private var domainFactory : DomainDictFactory = null
    private val isLoadDomainDictionary : Boolean = config.isLoadDomainDictionary()
    private val isLoadUserDictionary : Boolean = config.isLoadUserDictionary()

    def getCoreDictionary() : HashDictionary = {
        return coreDict
    }

    def loadDictionary() {
        try {
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
            System.err.println(e)
        }
    }

    private def loadWordStr(dictResource: String) : List[String] = {
        coreDict = new HashDictionary()
        val inputStream = getClass.getClassLoader.getResource(dictResource).toURI
        val source = Source.fromFile(inputStream, "utf-8")
        return source.getLines.toList
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
              case e:Throwable =>
                System.err.println(e)
            }
            userDictionaryLoader.clear()
            l1 = System.currentTimeMillis() - l1
            println((new StringBuilder()).append("loading user dictionary time used(ms): ").append(l1).toString())
        }
    }
}
