package websiteschema.mpsegment.concept

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class ConceptLoader {

    var conceptTree: ConceptTree = null

    private def loadConcept(inputStream: InputStream) {
        try {
            val reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"))
            loadConcept(reader)
        } catch {
          case ex : Exception =>
            ex.printStackTrace()
        }
    }

    private def loadConcept(reader: BufferedReader) {
        conceptTree = new ConceptTree()
        var line = reader.readLine()
        while (null != line) {
            try {
                parseThenAdd(line)
            } catch {
              case ex: Exception =>
                ex.printStackTrace()
            }
            line = reader.readLine()
        }
    }

    private def parseThenAdd(conceptStr: String) {
        val elements = conceptStr.split("\\s+")
        if (null == elements || elements.length != 3)
            return
        val id = parseId(elements(0))
        val description = elements(1)
        val name = elements(2)
        val concept = new Concept(id, name)
        concept.setDescription(description)
        val parentId = parseParentId(elements(0))
        val parent = conceptTree.getConceptById(parentId)
        if (null != parent) {
            conceptTree.addConcept(concept, parent)
            return
        }

        conceptTree.addConcept(concept)
    }

    private def parseId(idStr: String) : Long = {
        return parseId(idStr, 0)
    }

    private def parseParentId(idStr: String) : Long = {
        return parseId(idStr, 1)
    }

    private def parseId(idStr: String, rank: Int) : Long = {
        val idStrings = idStr.split("\\.")
        if (idStrings.length <= rank) {
            return 0
        }

        var stringBuilder = new StringBuilder()
        for (i <- 0 until (idStrings.length - rank)) {
            var part = idStrings(i)
            if (part.length() == 1) {
                part = "0" + part
            }
            stringBuilder.append(part)
        }
        return stringBuilder.toString().toLong
    }

    def getConceptTree() : ConceptTree = {
        return conceptTree
    }
}

object ConceptLoader {

  def apply(resource: String) = {
    val loader = new ConceptLoader()
    loader.loadConcept(getClass().getClassLoader().getResourceAsStream(resource))
    loader
  }

  def apply(resource: InputStream) = {
    val loader = new ConceptLoader()
    loader.loadConcept(resource)
    loader
  }
}
