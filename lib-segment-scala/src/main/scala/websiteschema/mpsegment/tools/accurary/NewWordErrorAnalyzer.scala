package websiteschema.mpsegment.tools.accurary

import websiteschema.mpsegment.core.WordAtom
import websiteschema.mpsegment.dict.DictionaryFactory
import websiteschema.mpsegment.dict.POSUtil

import collection.mutable.Map

class NewWordErrorAnalyzer extends AbstractErrorAnalyzer {

    override def analysis(expect: WordAtom, possibleErrorWord: String) : Boolean = {
        var foundError = false
        if (possibleErrorWord.replaceAll(" ", "").equals(expect.word)) {
            if (expect.pos != POSUtil.POS_NR && expect.pos != POSUtil.POS_NS) {
                increaseOccur()
                addErrorWord(expect.word)
                foundError = true
            }
        }
        return foundError
    }

    override def postAnalysis(allWordsAndFreqInCorpus: Map[String,Int]) {
        for(wordStr <- getWords().keys) {
            val word = DictionaryFactory().getCoreDictionary().getWord(wordStr)
            if(null != word) {
                removeErrorWord(wordStr)
            }
        }
    }
}
