package me.yingrui.segment.tools.accurary

import me.yingrui.segment.core.Word
import me.yingrui.segment.dict.DictionaryFactory
import me.yingrui.segment.dict.POSUtil

import collection.mutable.Map

class NewWordErrorAnalyzer extends AbstractErrorAnalyzer {

    override def analysis(expect: Word, possibleErrorWord: String) : Boolean = {
        var foundError = false
        if (possibleErrorWord.replaceAll(" ", "").equals(expect.name)) {
            if (expect.pos != POSUtil.POS_NR && expect.pos != POSUtil.POS_NS) {
                increaseOccur()
                addErrorWord(expect.name)
                foundError = true
            }
        }
        return foundError
    }

    override def postAnalysis(allWordsAndFreqInCorpus: Map[String,Int]) {
        for(wordStr <- getWords().keys) {
            val word = DictionaryFactory().getCoreDictionary.getWord(wordStr)
            if(null != word) {
                removeErrorWord(wordStr)
            }
        }
    }
}
