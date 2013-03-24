package websiteschema.mpsegment.tools.accurary

import websiteschema.mpsegment.core.WordAtom

import collection.mutable.Map
import collection.mutable.ListMap

trait ErrorAnalyzer {

    def getErrorOccurTimes() : Int

    def getWords(): ListMap[String,Int]

    def analysis(expect: WordAtom, possibleErrorWord: String) : Boolean

    def postAnalysis(allWordsAndFreqInCorpus: Map[String,Int])
}
