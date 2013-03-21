//package websiteschema.mpsegment.tools.accurary
//
//import websiteschema.mpsegment.core.SegmentResult
//import websiteschema.mpsegment.core.SegmentWorker
//import websiteschema.mpsegment.core.WordAtom
//import websiteschema.mpsegment.tools.PFRCorpusLoader
//import websiteschema.mpsegment.util.NumberUtil
//import websiteschema.mpsegment.util.StringUtil
//
//import java.io.IOException
//import java.util.HashMap
//import java.util.LinkedHashMap
//import java.util.Map
//
//class SegmentAccuracy {
//
//    private var loader : PFRCorpusLoader = null
//    private var totalWords : Int = 0
//    private var correct : Int = 0
//    private var wrong : Int = 0
//    private var accuracyRate : Double = null
//
//    private HashMap[String,Int] allWordsAndFreqInCorpus = new HashMap[String,Int]()
//    private var segmentWorker : SegmentWorker = null
//    private Map[SegmentErrorType,ErrorAnalyzer] allErrorAnalyzer
//
//    public SegmentAccuracy(String testCorpus, SegmentWorker segmentWorker) throws IOException {
//        this.segmentWorker = segmentWorker
//        initialErrorAnalyzer()
//        loader = new PFRCorpusLoader(getClass().getClassLoader().getResourceAsStream(testCorpus))
//    }
//
//    def getAccuracyRate() : Double = {
//        return accuracyRate
//    }
//
//    def getWrong() : Int = {
//        return wrong
//    }
//
//    def getTotalWords() : Int = {
//        return totalWords
//    }
//
//    def getErrorAnalyzer(errorType: SegmentErrorType) : ErrorAnalyzer = {
//        return allErrorAnalyzer.get(errorType)
//    }
//
//    def checkSegmentAccuracy() {
//        var isUseContextFreq = segmentWorker.isUseContextFreqSegment()
//        segmentWorker.setUseContextFreqSegment(true)
//        try {
//            var expectResult = loader.readLine()
//            while (expectResult != null) {
//                var sentence = expectResult.toOriginalString()
//                var actualResult = segmentWorker.segment(sentence)
//
//                totalWords += expectResult.length()
//
//                compare(expectResult, actualResult)
//                expectResult = loader.readLine()
//            }
//        } catch {
//            ex.printStackTrace()
//        } finally {
//            postAnalysis()
//            segmentWorker.setUseContextFreqSegment(isUseContextFreq)
//        }
//        assert (correct > 0 && totalWords > 0)
//        accuracyRate = (Double) correct / (Double) totalWords
//    }
//
//    private def initialErrorAnalyzer() {
//        allErrorAnalyzer = new LinkedHashMap[SegmentErrorType,ErrorAnalyzer]()
//        allErrorAnalyzer.put(SegmentErrorType.NER_NR, new NerNameErrorAnalyzer())
//        allErrorAnalyzer.put(SegmentErrorType.NER_NS, new NerPlaceErrorAnalyzer())
//        allErrorAnalyzer.put(SegmentErrorType.UnknownWord, new NewWordErrorAnalyzer())
//        allErrorAnalyzer.put(SegmentErrorType.ContainDisambiguate, new ContainErrorAnalyzer())
//        allErrorAnalyzer.put(SegmentErrorType.Other, new OtherErrorAnalyzer())
//    }
//
//    private def compare(expectResult: SegmentResult, actualResult: SegmentResult) {
//        var lastMatchIndex = -1
//        for (Int i = 0; i < expectResult.length(); i++) {
//            var expectWord = expectResult.getWordAtom(i)
//            recordWordFreqInCorpus(expectWord)
//            var indexInOriginalString = expectResult.getWordIndexInOriginalString(i)
//            var match = lookupMatch(actualResult, expectWord, lastMatchIndex + 1, indexInOriginalString)
//            if (match >= 0) {
//                lastMatchIndex = match
//                correct++
//            } else {
//                wrong++
//            }
//        }
//    }
//
//    private def recordWordFreqInCorpus(word: WordAtom) {
//        var freq = allWordsAndFreqInCorpus.containsKey(word.word) ? allWordsAndFreqInCorpus.get(word.word) + 1 : 1
//        allWordsAndFreqInCorpus.put(word.word, freq)
//    }
//
//    private def lookupMatch(actualResult: SegmentResult, expectWord: WordAtom, start: Int, indexInOriginalString: Int) : Int = {
//        for (Int i = start; i < actualResult.length(); i++) {
//            var actualWord = actualResult.getWord(i)
//            if (isSameWord(expectWord.word, actualWord)) {
//                if (actualResult.getWordIndexInOriginalString(i) == indexInOriginalString) {
//                    return i
//                }
//            }
//        }
//        analyzeErrorReason(actualResult, expectWord, start, indexInOriginalString)
//        return -1
//    }
//
//    private def analyzeErrorReason(actualResult: SegmentResult, expect: WordAtom, start: Int, from: Int) {
//        var possibleErrorWord = lookupErrorWord(actualResult, expect, start, from)
//        analyzeReason(expect, possibleErrorWord)
//    }
//
//    private def analyzeReason(expect: WordAtom, possibleErrorWord: String) {
//        for(SegmentErrorType errorType : allErrorAnalyzer.keySet()) {
//            var analyzer = allErrorAnalyzer.get(errorType)
//            var isErrorWord = analyzer.analysis(expect, possibleErrorWord)
//            if(isErrorWord) {
//                break
//            }
//        }
//    }
//
//    private def lookupErrorWord(actualResult: SegmentResult, expect: WordAtom, start: Int, from: Int) : String = {
//        var to = from + expect.length()
//        var stringBuilder = new StringBuilder()
//        for (Int i = start; i < actualResult.length(); i++) {
//            var indexInOriginalString = actualResult.getWordIndexInOriginalString(i)
//            if (indexInOriginalString >= from && indexInOriginalString < to) {
//                stringBuilder.append(actualResult.getWord(i)).append(" ")
//            }
//        }
//
//        return stringBuilder.toString().trim()
//    }
//
//    private def isSameWord(expect: String, actual: String) : Boolean = {
//        var expectWord = StringUtil.doUpperCaseAndHalfShape(expect)
//        if (expectWord.equalsIgnoreCase(actual)) {
//            return true
//        }
//        if (Character.isDigit(actual.charAt(0))) {
//            var number = NumberUtil.chineseToEnglishNumberStr(expect)
//            if (actual.equals(number)) {
//                return true
//            }
//        }
//        return false
//    }
//
//    private def postAnalysis() {
//        for (errorType <- allErrorAnalyzer.keySet()) {
//            getErrorAnalyzer(errorType).postAnalysis(allWordsAndFreqInCorpus)
//        }
//    }
//}
//
