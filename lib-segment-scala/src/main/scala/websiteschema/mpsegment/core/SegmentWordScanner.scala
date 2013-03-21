//package websiteschema.mpsegment.core
//
//import websiteschema.mpsegment.conf.MPSegmentConfiguration
//import websiteschema.mpsegment.dict.DictionaryLookupResult
//import websiteschema.mpsegment.dict.IWord
//import websiteschema.mpsegment.dict.POSUtil
//import websiteschema.mpsegment.dict.UnknownWord
//import websiteschema.mpsegment.graph.IGraph
//
//import java.util.Map
//
//class SegmentWordScanner extends AbstractWordScanner {
//
//    private static Int BigWordLength = 4
//    private static Double logCorpus = MPSegmentConfiguration.LOG_CORPUS
//    private Map[String,Int] contextFreqMap
//    private var segmentMin : Boolean = null
//    private var useContextFreqSegment : Boolean = null
//    private var graph : IGraph = null
//
//    public SegmentWordScanner(Boolean segmentMin, Boolean useContextFreqSegment, IGraph graph, Map[String,Int] contextFreqMap) {
//        this.segmentMin = segmentMin
//        this.useContextFreqSegment = useContextFreqSegment
//        this.graph = graph
//        this.contextFreqMap = contextFreqMap
//    }
//
//    override def foundAtomWord(atomWord: String) : IWord = {
//        var singleCharWord = getDictionaryService().lookup(atomWord).firstMatchWord
//        if (singleCharWord == null) {
//            singleCharWord = initAsUnknownWord(atomWord);//Unknown Word
//        }
//        return singleCharWord
//    }
//
//    override def processFoundWordItems(begin: Int, singleCharWord: IWord, lookupResult: DictionaryLookupResult) {
//        var the1stMatchWord = lookupResult.firstMatchWord
//        var the2ndMatchWord = lookupResult.the2ndMatchWord
//        var the3rdMatchWord = lookupResult.the3rdMatchWord
//        var matchedWordCount = lookupResult.matchedWordCount
//        //将查找到的词添加到图中。
//        //为了减少图的分支，同时因为单字词在中文中往往没有太多意义。
//        //如果存在多个多字词，则不向图中添加单字词
//        var shouldAddSingleCharWord = the1stMatchWord == null || the1stMatchWord.getWordLength() == 1 || matchedWordCount < 2
//        if (shouldAddSingleCharWord) {
//            addSingleCharWordToGraph(begin, singleCharWord.getWordLength(), singleCharWord)
//        }
//        var foundOtherWords = the1stMatchWord != null && the1stMatchWord.getWordLength() > 1
//        if (foundOtherWords) {
//            addMatchedWordToGraph(begin, the2ndMatchWord)
//            addMatchedWordToGraph(begin, the3rdMatchWord)
//            if (!segmentMin || !isMatchedMoreThanOneWord(matchedWordCount) || !isFirstMatchedWordBigWord(the1stMatchWord) || isAtomWordAccordingPOS(the1stMatchWord)) {
//                addMatchedWordToGraph(begin, the1stMatchWord)
//            }
//        }
//    }
//
//    private def initAsUnknownWord(unknownWordStr: String) : IWord = {
//        return new UnknownWord(unknownWordStr)
//    }
//
//    private def isFirstMatchedWordBigWord(the1stMatchWord: IWord) : Boolean = {
//        return the1stMatchWord.getWordLength() > BigWordLength
//    }
//
//    private def isMatchedMoreThanOneWord(matchedWordCount: Int) : Boolean = {
//        return matchedWordCount > 1
//    }
//
//    private def isAtomWordAccordingPOS(the1stMatchWord: IWord) : Boolean = {
//        var wordPOSTable = the1stMatchWord.getWordPOSTable()
//        var the1stMatchedWordPOS = null != wordPOSTable ? wordPOSTable(0)(0) : -1
//        return the1stMatchedWordPOS == POSUtil.POS_I || the1stMatchedWordPOS == POSUtil.POS_L
//    }
//
//    private def addMatchedWordToGraph(begin: Int, matchedWord: IWord) {
//        if (null != matchedWord) {
//            addEdgeObject(begin + 1, begin + matchedWord.getWordLength() + 1, getWeight(matchedWord), matchedWord)
//        }
//    }
//
//    private def addSingleCharWordToGraph(begin: Int, lastMinWordLen: Int, singleCharWord: IWord) {
//        addEdgeObject(begin + 1, begin + lastMinWordLen + 1, getWeight(singleCharWord), singleCharWord)
//    }
//
//    private def addEdgeObject(head: Int, tail: Int, weight: Int, word: IWord) {
//        graph.addEdge(head, tail, weight, word)
//    }
//
//    private def getWeight(word: IWord) : Int = {
//        if (useContextFreqSegment) {
//            var wordName = word.getWordName()
//            var weight = 1
//            if (wordName.length() > 1) {
//                var contextFreq = contextFreqMap.containsKey(wordName) ? contextFreqMap.get(wordName) : 1
//                var freq = getFreqWeight(word)
//                weight = freq + getContextFreqWeight(freq, contextFreq)
//            } else {
//                weight = getFreqWeight(word)
//            }
//            if (weight <= 0) {
//                weight = 1
//            }
//            return weight
//        }
//        var weight = getFreqWeight(word)
//        return weight
//    }
//
//    private def getFreqWeight(word: IWord) : Int = {
//        var log2Freq = word.getLog2Freq()
//        if (logCorpus > log2Freq) {
//            var freqWeight = (Int) (logCorpus - word.getLog2Freq())
//            return freqWeight
//        } else {
//            return 1
//        }
//    }
//
//    private def getContextFreqWeight(freq: Int, contextFreq: Int) : Int = {
//        var weight = -(Int) ((1 - Math.exp(-0.1 * (contextFreq - 1))) * freq)
//        return weight
//    }
//}
