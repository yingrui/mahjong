//package websiteschema.mpsegment.core
//
//import websiteschema.mpsegment.conf.MPSegmentConfiguration
//import websiteschema.mpsegment.dict.DictionaryService
//import websiteschema.mpsegment.graph.IGraph
//import websiteschema.mpsegment.util.StringUtil
//
//import java.util.Map
//
//class GraphBuilder {
//
//    public GraphBuilder(IGraph graph, Boolean useDomainDictionary, MPSegmentConfiguration config) {
//        this.graph = graph
//        segmentMin = config.isSegmentMin()
//        upperCaseAll = config.isUpperCaseAll()
//        halfShapeAll = config.isHalfShapeAll()
//        upperCaseOrHalfShapeAll = upperCaseAll || halfShapeAll
//        loadDomainDictionary = config.isLoadDomainDictionary()
//        loadUserDictionary = config.isLoadUserDictionary()
//        maxWordLength = config.getMaxWordLength()
//        dictionaryService = new DictionaryService(useDomainDictionary, loadDomainDictionary, loadUserDictionary)
//    }
//
//    private def doUpperCaseAndHalfShape(word: String) : String = {
//        if (upperCaseOrHalfShapeAll) {
//            if (halfShapeAll && upperCaseAll) {
//                return StringUtil.doUpperCaseAndHalfShape(word)
//            }
//            if (halfShapeAll) {
//                return StringUtil.halfShape(word)
//            }
//            return StringUtil.toUpperCase(word)
//        }
//        return word
//    }
//
//    def scanContextFreq(startPos: Int) {
//        var scanner = getContextFrequencyScanner()
//        scanner.startScanningAt(startPos)
//        this.contextFreqMap = scanner.getContextFreqMap()
//    }
//
//    private def getContextFrequencyScanner() : ContextFrequencyScanner = {
//        var scanner = new ContextFrequencyScanner()
//        scanner.setSentence(sentence)
//        scanner.setDictionaryService(dictionaryService)
//        scanner.setMaxWordLength(maxWordLength)
//        return scanner
//    }
//
//    def setSentence(sen: String) {
//        sentence = sen;//doUpperCaseAndHalfShape(sen)
//    }
//
//    def buildGraph(sen: String, startPos: Int) {
//        setSentence(sen)
//        if (useContextFreqSegment) {
//            scanContextFreq(startPos)
//        }
//        var length = sentence.length()
//        for (Int i = 0; i < length; i++) {
//            graph.addVertex()
//        }
//
//        var scanner = getSegmentWordScanner()
//        scanner.startScanningAt(startPos)
//    }
//
//    private def getSegmentWordScanner() : SegmentWordScanner = {
//        var scanner = new SegmentWordScanner(segmentMin, useContextFreqSegment, graph, contextFreqMap)
//        scanner.setSentence(sentence)
//        scanner.setMaxWordLength(maxWordLength)
//        scanner.setDictionaryService(dictionaryService)
//        return scanner
//    }
//
//    public Map[String,Int] getContextFreqMap() {
//        return contextFreqMap
//    }
//
//    def setUseContextFreqSegment(useContextFreqSegment: Boolean) {
//        this.useContextFreqSegment = useContextFreqSegment
//    }
//
//    private Map[String,Int] contextFreqMap
//    private var graph : IGraph = null
//    private var segmentMin : Boolean = null
//    private var upperCaseAll : Boolean = null
//    private var halfShapeAll : Boolean = null
//    private var upperCaseOrHalfShapeAll : Boolean = null
//    private var loadDomainDictionary : Boolean = null
//    private var loadUserDictionary : Boolean = null
//    private var useContextFreqSegment : Boolean = false
//    private var dictionaryService : DictionaryService = null
//    private var sentence : String = ""
//    private var maxWordLength : Int = null
//}
//
