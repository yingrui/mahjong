package websiteschema.mpsegment.tools

import websiteschema.mpsegment.concept.Concept
import websiteschema.mpsegment.concept.ConceptRepository
import websiteschema.mpsegment.dict.IWord
import websiteschema.mpsegment.dict.POS
import websiteschema.mpsegment.dict.POSArray
import websiteschema.mpsegment.dict.WordImpl
import collection.mutable

class StringWordConverter {

  private var conceptRepository: ConceptRepository = null
  private val patternWord = "\"(.*)\"\\s*=\\s*(\\{.*\\})".r
  private val patternPair = "([\\w\\d]+)\\s*:\\s*((\\{[^\\}]+?\\})|([\\w\\d]+)|(\\[[^\\]]+?\\]))\\s*(,|$)".r
  private val domainTypeKey = "domainType"
  private val posTableKey = "POSTable"
  private val conceptsKey = "concepts"

  def setConceptRepository(conceptRepository: ConceptRepository) {
    this.conceptRepository = conceptRepository;
  }

  def convert(str: String): IWord = {
    patternWord.findFirstMatchIn(str) match {
      case Some(m) => {
        var wordName = m.group(1)
        var properties = convertToMap(m.group(2))
        try {
          wordName = new String(java.net.URLDecoder.decode(wordName, "utf-8"));
        } catch {
          case e: Exception =>
            e.printStackTrace();
        }

        val word = new WordImpl(wordName)
        setDomainType(properties.getOrElse(domainTypeKey, null), word);
        setPOSTable(properties.getOrElse(posTableKey, null), word);
        setConcepts(properties.getOrElse(conceptsKey, null), word);
        properties = null;
        return word;
      }
      case _ => return null
    }
  }

  private def setConcepts(conceptsString: String, word: WordImpl) {
    val conceptStrList: List[String] = convertToList(conceptsString)
    if (null != conceptStrList) {
      val concepts = new Array[Concept](conceptStrList.size)
      for (i <- 0 until conceptStrList.size) {
        val conceptName = conceptStrList(i)
        try {
          val concept = conceptRepository.getConceptByName(conceptName)
          concepts(i) = concept;
        } catch {
          case _: Exception =>
            println(conceptName);
        }
      }
      word.setConcepts(concepts);
    }
  }

  private def setPOSTable(posTableString: String, word: WordImpl) {
    if (null != posTableString) {
      var posTable = convertToMap(posTableString)
      val posArray = new POSArray()
      for (pos <- posTable.keys) {
        posArray.add(POS(pos, posTable(pos).toInt));
      }
      posArray.buildPOSArray();
      word.setPosArray(posArray);
      posTable = null;
    }
  }

  private def setDomainType(domainType: String, word: WordImpl) {
    if (null != domainType) {
      word.setDomainType(domainType.toInt)
    }
  }

  private def convertToMap(str: String): mutable.LinkedHashMap[String, String] = {
    if (str.matches("\\{.*\\}")) {
      val content = str.substring(1, str.length() - 1)
      val map = mutable.LinkedHashMap[String, String]()
      for (pair <- patternPair.findAllMatchIn(content)) {
        map += (pair.group(1) -> pair.group(2))
      }
      return map;
    }
    return null;
  }

  private def convertToList(str: String): List[String] = {
    if (null != str && str.matches("\\[.*\\]")) {
      val content = str.substring(1, str.length() - 1)
      return content.split(",").toList
    }
    return null;
  }

}
