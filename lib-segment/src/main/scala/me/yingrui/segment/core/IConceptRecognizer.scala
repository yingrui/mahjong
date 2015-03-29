package me.yingrui.segment.core

import me.yingrui.segment.dict.IWord

trait IConceptRecognizer {

    def setWordArray(wordArray: Array[IWord])

    def setPosArray(posArray: Array[Int])

    def getConcepts() : Array[String]

    def reset()
}
