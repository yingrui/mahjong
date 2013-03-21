//package websiteschema.mpsegment.tools.accurary
//
//import websiteschema.mpsegment.core.WordAtom
//import websiteschema.mpsegment.dict.DictionaryFactory
//import websiteschema.mpsegment.dict.IWord
//import websiteschema.mpsegment.dict.POSUtil
//
//import java.util.Map
//
//class NewWordErrorAnalyzer extends AbstractErrorAnalyzer {
//
//    override def analysis(expect: WordAtom, possibleErrorWord: String) : Boolean = {
//        var foundError = false
//        if (possibleErrorWord.replaceAll(" ", "").equals(expect.word)) {
//            if (expect.pos != POSUtil.POS_NR && expect.pos != POSUtil.POS_NS) {
//                increaseOccur()
//                println(possibleErrorWord + " -> " + expect.word)
//                addErrorWord(expect.word)
//                foundError = true
//            }
//        }
//        return foundError
//    }
//
//    override def postAnalysis(allWordsAndFreqInCorpus: Map[String,Int]) {
//        for(String wordStr : getWords().keySet()) {
//            var word = DictionaryFactory.getInstance().getCoreDictionary().getWord(wordStr)
//            if(null != word) {
//                removeErrorWord(wordStr)
//            }
//        }
//    }
//}
