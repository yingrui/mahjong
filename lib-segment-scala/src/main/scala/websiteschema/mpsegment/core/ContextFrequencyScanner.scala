//package websiteschema.mpsegment.core
//
//import websiteschema.mpsegment.dict.DictionaryLookupResult
//import websiteschema.mpsegment.dict.IWord
//
//import java.util.HashMap
//import java.util.Map
//
//class ContextFrequencyScanner extends AbstractWordScanner {
//
//    private Map[String,Int] contextFreqMap
//
//    public ContextFrequencyScanner() {
//        contextFreqMap = new HashMap[String,Int]()
//    }
//
//    public Map[String,Int] getContextFreqMap() {
//        return contextFreqMap
//    }
//
//    override def foundAtomWord(atomWord: String) : IWord = {
//        return null
//    }
//
//    override def processFoundWordItems(begin: Int, singleCharWord: IWord, lookupResult: DictionaryLookupResult) {
//        var the1stMatchWord = lookupResult.firstMatchWord
//        var the2ndMatchWord = lookupResult.the2ndMatchWord
//        var the3rdMatchWord = lookupResult.the3rdMatchWord
//        if (the1stMatchWord != null && the1stMatchWord.getWordLength() > 1) {
//            //查找结果不为空且不是单字词
//            increaseContextFreq(the1stMatchWord.getWordName())
//            if (the2ndMatchWord != null && the2ndMatchWord.getWordLength() > 1) {
//                increaseContextFreq(the2ndMatchWord.getWordName())
//            }
//            if (the3rdMatchWord != null && the3rdMatchWord.getWordLength() > 1) {
//                increaseContextFreq(the3rdMatchWord.getWordName())
//            }
//        }
//    }
//
//    private def increaseContextFreq(word: String) {
//        if (contextFreqMap.containsKey(word)) {
//            var freq = contextFreqMap.get(word)
//            contextFreqMap.put(word, freq + 1)
//        } else {
//            contextFreqMap.put(word, 1)
//        }
//    }
//}
