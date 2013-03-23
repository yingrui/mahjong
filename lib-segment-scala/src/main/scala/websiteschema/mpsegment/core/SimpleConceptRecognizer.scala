package websiteschema.mpsegment.core

import websiteschema.mpsegment.concept.Concept
import websiteschema.mpsegment.dict.IWord
import websiteschema.mpsegment.dict.POSUtil

class SimpleConceptRecognizer extends IConceptRecognizer {
    private var words : Array[IWord] = null
    private var posArray : Array[Int] = null

    override def setWordArray(wordArray: Array[IWord]) {
        this.words = wordArray
    }

    override def setPosArray(posArray: Array[Int]) {
        this.posArray = posArray
    }

    override def getConcepts() : Array[String] = {
        assert (words != null && posArray != null && words.length == posArray.length)
        val length = words.length
        val concepts = new Array[String](length)
        for(i <- 0 until length) {
            concepts(i) = getConcept(words(i), posArray(i))
        }
        return concepts
    }

    private def getConcept(word: IWord, pos: Int) : String = {
        val concepts = word.getConcepts()

        if(pos == POSUtil.POS_NS) {
            return "n-location"
        }

        if(pos == POSUtil.POS_NR) {
            return "n-name"
        }

        if (null != concepts) {
            val primaryPOS = POSUtil.getPOSString(pos).substring(0, 1).toLowerCase()
            for (i <- 0 until concepts.length) {
                if (concepts(i).getName().startsWith(primaryPOS)) {
                    return concepts(i).getName()
                }
            }
        }
        return Concept.UNKNOWN.getName()
    }

    override def reset() {
        words = null
        posArray = null
    }


}
