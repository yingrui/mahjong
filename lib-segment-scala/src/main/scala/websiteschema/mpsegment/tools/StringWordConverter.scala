package websiteschema.mpsegment.tools

import websiteschema.mpsegment.concept.Concept
import websiteschema.mpsegment.concept.ConceptRepository
import websiteschema.mpsegment.dict.IWord
import websiteschema.mpsegment.dict.POS
import websiteschema.mpsegment.dict.POSArray
import websiteschema.mpsegment.dict.WordImpl
import scala.util.parsing.json.JSON

class StringWordConverter {

  private var conceptRepository: ConceptRepository = null
  private val domainTypeKey = "domainType"
  private val posTableKey = "POSTable"
  private val conceptsKey = "concepts"

  def setConceptRepository(conceptRepository: ConceptRepository) {
    this.conceptRepository = conceptRepository
  }

  def convertJSON(str: String): IWord = {
    JSON.parseFull(str) match {
      case Some(map: Map[String, Any]) => {
        val word = new WordImpl(map("word").asInstanceOf[String])
        word.setDomainType(map(domainTypeKey).asInstanceOf[Double].toInt)
        updatePOSTable(map(posTableKey).asInstanceOf[Map[String, Double]], word)
        val concepts = map.getOrElse(conceptsKey, List[String]()).asInstanceOf[List[String]]
        updateConcepts(concepts, word)
        word
      }
      case _ => null
    }
  }

  def convert(wordStr: String): IWord = {
    val wordNamePattern = """"word"\s*:\s*"(.+?)"\s*,""".r
    wordNamePattern findFirstIn wordStr match {
      case Some(wordNamePattern(wordName)) => {
        val word = new WordImpl(escape(wordName))

        parseDomainType(wordStr, word)
        parsePOS(wordStr, word)
        parseConcepts(wordStr, word)

        word
      }
      case None => throw new RuntimeException(s"Cannot convert to word: $wordStr")
    }
  }

  def escape(word:String) = word.
    replaceAll("""\\r""", "\r").
    replaceAll("""\\n""", "\n").
    replaceAll("""\\/""", "/").
    replaceAll("""\\"""", "\"").
    replace("""\\""", "\\")

  def parseConcepts(wordStr: String, word: WordImpl) {
    val conceptsPattern = """"concepts"\s*:\s*\[(("([^"]+)",?)+)\]\s*,?""".r
    conceptsPattern findFirstMatchIn wordStr match {
      case Some(m) => {
        val concepts = m.group(1)
        val conceptPattern = """"([^"]+)\",?""".r
        val conceptArray = conceptPattern.findAllMatchIn(concepts).
          map(m =>conceptRepository.getConceptByName(m.group(1))).toArray
        word.setConcepts(conceptArray)
        }
      case None => word.setConcepts(Array())
    }
  }

  def parseDomainType(wordStr: String, word: WordImpl) {
    val domainTypePattern = """"domainType"\s*:\s*(\d+)\s*,?""".r
    domainTypePattern findFirstIn wordStr match {
      case Some(domainTypePattern(domainType)) => word.setDomainType(domainType.toInt)
      case None => word.setDomainType(0)
    }
  }

  def parsePOS(wordStr: String, word: WordImpl) {
    val posTablePattern = """"POSTable"\s*:\s*\{(("(\w+)"\s*:\s*(\d+)\s*,?\s*)+)\}\s*,?""".r
    posTablePattern findFirstMatchIn wordStr match {
      case Some(m) => {
        val posTable = m.group(1)
        val posArray = new POSArray()

        val posAndFreq = """"(\w+)"\s*:\s*(\d+)""".r
        posAndFreq.findAllMatchIn(posTable)
          .foreach(m => posArray.add(POS(m.group(1), m.group(2).toInt)))
        posArray.buildPOSArray()
        word.setPosArray(posArray)
      }
      case None =>
    }
  }

  def updateConcepts(conceptList: List[String], word: WordImpl) {
    val concepts = new Array[Concept](conceptList.size)
    for (i <- 0 until conceptList.size) {
      val conceptName = conceptList(i)
      try {
        val concept = conceptRepository.getConceptByName(conceptName)
        concepts(i) = concept
      } catch {
        case _: Exception =>
          println(conceptName)
      }
    }
    word.setConcepts(concepts)
  }

  def updatePOSTable(posTable: collection.immutable.Map[String, Double], word: WordImpl) {
    val posArray = new POSArray()
    for (pos <- posTable.keys) {
      posArray.add(POS(pos, posTable(pos).toInt))
    }
    posArray.buildPOSArray()
    word.setPosArray(posArray)
  }

}
