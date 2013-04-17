package websiteschema.mpsegment.tools

import websiteschema.mpsegment.concept.Concept
import websiteschema.mpsegment.dict.IWord
import websiteschema.mpsegment.dict.POSUtil

class WordStringConverter(word: IWord) {

    private val stringBuilder = new StringBuilder()

    def convertToString() : String = {
        buildHead()
        buildBody()
        return stringBuilder.toString()
    }

    private def buildBody() {
        stringBuilder.append("{")
        stringBuilder.append(buildDomainType()).append(",")
        val posTable = buildPOSTable()
        if (!posTable.isEmpty()) {
            stringBuilder.append(posTable).append(",")
        }
        stringBuilder.append(buildConcepts())
        if (endWith(',')) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1)
        }
        stringBuilder.append("}")
    }

    private def endWith(ch: Char) : Boolean = {
        return stringBuilder.charAt(stringBuilder.length() - 1) == ch
    }

    private def buildPOSTable() : String = {
        val stringBuilder = new StringBuilder()
        val POSTable = word.getWordPOSTable()
        if (null != POSTable && POSTable.length > 0) {
            stringBuilder.append("POSTable:{")
            for (POSAndFreq <- POSTable) {
                val POS = POSUtil.getPOSString(POSAndFreq(0))
                val freq = POSAndFreq(1)
                stringBuilder.append(POS).append(":").append(freq).append(",")
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length())
            stringBuilder.append("}")
        }
        return stringBuilder.toString()
    }

    private def buildDomainType() : String = {
        return "domainType:" + word.getDomainType()
    }

    private def buildConcepts() : String = {
        return buildConceptArray(word.getConcepts())
    }

    private def buildConceptArray(concepts: Array[Concept]) : String = {
        val stringBuilder = new StringBuilder()
        if (null != concepts && concepts.length > 0 && concepts(0) != Concept.UNKNOWN) {
            stringBuilder.append("concepts:")
            stringBuilder.append("[")
            for (concept <- concepts) {
                stringBuilder.append(concept.getName()).append(",")
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1)
            stringBuilder.append("]")
        }
        return stringBuilder.toString()
    }

    private def buildHead() {
        var wordName = word.getWordName()
        wordName = wordName.replaceAll("\"", "%22")
        wordName = wordName.replaceAll("\\(", "%28")
        wordName = wordName.replaceAll("\\)", "%29")
        wordName = wordName.replaceAll("\\[", "%5B")
        wordName = wordName.replaceAll("\\]", "%5D")
        wordName = wordName.replaceAll("\\{", "%7B")
        wordName = wordName.replaceAll("\\}", "%7D")
        stringBuilder.append("\"").append(wordName).append("\" = ")
    }

}
