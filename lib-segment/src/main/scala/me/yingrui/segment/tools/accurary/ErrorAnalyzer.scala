package me.yingrui.segment.tools.accurary

import me.yingrui.segment.core.Word

import collection.mutable.Map
import collection.mutable.ListMap

trait ErrorAnalyzer {

    def getErrorOccurTimes() : Int

    def getWords(): ListMap[String,Int]

    def analysis(expect: Word, possibleErrorWord: String) : Boolean

    def postAnalysis(allWordsAndFreqInCorpus: Map[String,Int])
}
