package websiteschema.mpsegment.tools

import websiteschema.mpsegment.concept.Concept
import websiteschema.mpsegment.concept.ConceptRepository
import websiteschema.mpsegment.dict.IWord
import websiteschema.mpsegment.dict.POS
import websiteschema.mpsegment.dict.POSArray
import websiteschema.mpsegment.dict.WordImpl
import collection.mutable
import scala.util.parsing.json.JSON

class StringWordConverter {

  private var conceptRepository: ConceptRepository = null
  private val domainTypeKey = "domainType"
  private val posTableKey = "POSTable"
  private val conceptsKey = "concepts"

  def setConceptRepository(conceptRepository: ConceptRepository) {
    this.conceptRepository = conceptRepository
  }

  def convert(str: String): IWord = {
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
