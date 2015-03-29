package me.yingrui.segment.core

import me.yingrui.segment.dict.DictionaryLookupResult
import me.yingrui.segment.dict.IWord
import collection.mutable.HashMap
import collection.mutable.Map

class ContextFrequencyScanner extends AbstractWordScanner {

    private val contextFreqMap = HashMap[String,Int]()

    def getContextFreqMap(): Map[String,Int] = {
        return contextFreqMap
    }

    override def foundAtomWord(atomWord: String) : IWord = {
        return null
    }

    override def processFoundWordItems(begin: Int, singleCharWord: IWord, lookupResult: DictionaryLookupResult) {
        val the1stMatchWord = lookupResult.firstMatchWord
        val the2ndMatchWord = lookupResult.the2ndMatchWord
        val the3rdMatchWord = lookupResult.the3rdMatchWord
        if (the1stMatchWord != null && the1stMatchWord.getWordLength() > 1) {
            //查找结果不为空且不是单字词
            increaseContextFreq(the1stMatchWord.getWordName())
            if (the2ndMatchWord != null && the2ndMatchWord.getWordLength() > 1) {
                increaseContextFreq(the2ndMatchWord.getWordName())
            }
            if (the3rdMatchWord != null && the3rdMatchWord.getWordLength() > 1) {
                increaseContextFreq(the3rdMatchWord.getWordName())
            }
        }
    }

    private def increaseContextFreq(word: String) {
        if (contextFreqMap.contains(word)) {
            val freq = contextFreqMap(word)
            contextFreqMap += (word -> (freq + 1))
        } else {
            contextFreqMap += (word -> 1)
        }
    }
}
