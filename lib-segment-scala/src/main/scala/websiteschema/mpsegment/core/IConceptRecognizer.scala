package websiteschema.mpsegment.core

import websiteschema.mpsegment.dict.IWord

trait IConceptRecognizer {

    def setWordArray(wordArray: Array[IWord])

    def setPosArray(posArray: Array[Int])

    def getConcepts() : Array[String]

    def reset()
}
