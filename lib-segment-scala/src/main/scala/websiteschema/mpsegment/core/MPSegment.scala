//package websiteschema.mpsegment.core;
//
//import websiteschema.mpsegment.conf.MPSegmentConfiguration;
//import websiteschema.mpsegment.dict.IWord;
//import websiteschema.mpsegment.graph.*;
//import websiteschema.mpsegment.pinyin.WordToPinyinClassfierFactory;
//
//class MPSegment {
//
//    public MPSegment(MPSegmentConfiguration config) {
//        this.config = config;
//        lastSection = false;
//        lastSectionStr = "";
//        useDomainDictionary = true;
//        initialize();
//    }
//
//    def isUseDomainDictionary() : Boolean = {
//        return useDomainDictionary;
//    }
//
//    def setUseDomainDictionary(flag: Boolean) {
//        useDomainDictionary = flag;
//    }
//
//    def isUseContextFreqSegment() : Boolean = {
//        return useContextFreqSegment;
//    }
//
//    def setUseContextFreqSegment(useContextFreqSegment: Boolean) {
//        this.useContextFreqSegment = useContextFreqSegment;
//    }
//
//    private def initialize() {
//        initializeGraph();
//        withPinyin = config.isWithPinyin();
//        initializePOSTagging();
//    }
//
//    private def initializeGraph() {
//        graph = new Graph();
////        dijk = new BigramDijkstra(WordBigram.getInstance("word-bigram.dat"));
////        dijk = new BigramDijkstra(WordBigram.getInstance("google-bigram.dat"));
//        dijk = new DijkstraImpl();
//    }
//
//    private def initializePOSTagging() {
//        posTagging = new POSRecognizer();
//    }
//
//
//    def segmentMP(sentence: String, withPOS: Boolean) : SegmentResult = {
//        if (sentence == null || sentence.length() < 1) {
//            return null;
//        }
//        lastSectionStr = "";
//        Int totalLength = sentence.length();
//        SegmentResult result;
//        if (totalLength < 1023) {
//            result = segment(sentence, withPOS, false);
//        } else {
//            lastSection = false;
//            result = new SegmentResult(0);
//            for (Int startIndex = 0; startIndex < totalLength; ) {
//                String section = getSection(sentence, startIndex);
//                startIndex += section.length();
//                lastSection = startIndex == totalLength;
//                SegmentResult sectionResult = segment(section, withPOS, true);
//                result.append(sectionResult);
//                if (!lastSection && lastSectionStr.length() > 0) {
//                    startIndex -= lastSectionStr.length();
//                }
//            }
//        }
//        return result;
//    }
//
//    private def buildGraph(sen: String, startPos: Int) {
//        GraphBuilder builder = new GraphBuilder(graph, useDomainDictionary, config);
//        builder.setUseContextFreqSegment(useContextFreqSegment);
//        builder.buildGraph(sen, startPos);
//    }
//
//    private def buildSegmentResult(path: Path) : SegmentResult = {
//        Int length = path.getLength();
//        Array[String] wordNames = new Array[String](length);
//        Array[Int] domainTypes = new Array[Int](length);
//        if (length < 1) {
//            return null;
//        }
//        SegmentResult segmentResult = new SegmentResult(length);
//        for (Int index = 0; index < length; index++) {
//            Int edgeWeight = graph.getEdgeWeight(path.iget(index), path.iget(index + 1));
//            if (edgeWeight == 0) {
//                IWord word = graph.getEdgeObject(path.iget(index), path.iget(index + 1));
//                wordNames(index) = word.getWordName();
//                domainTypes(index) = word.getDomainType();
//            } else {
//                IWord word = graph.getEdgeObject(path.iget(index), path.iget(index + 1));
//                wordNames(index) = word.getWordName();
//                domainTypes(index) = word.getDomainType();
//            }
//        }
//
//        segmentResult.setWords(wordNames);
//        segmentResult.setDomainTypes(domainTypes);
//        return segmentResult;
//    }
//
//    private def getSection(sentence: String, startIndex: Int) : String = {
//        String sectionedSentence;
//        if (sentence.length() - startIndex >= 1000) {
//            sectionedSentence = sentence.substring(startIndex, startIndex + 1000);
//        } else {
//            sectionedSentence = sentence.substring(startIndex);
//        }
//        return sectionedSentence;
//    }
//
//    private def lookupStopVertex(sentence: String) : Int = {
//        Int length = sentence.length();
//        lastSectionStr = "";
//        Int endVertex = -2;
//        if (!lastSection) {
//            endVertex = graph.getStopVertex(length - 20, length);
//            if (endVertex > 1 && endVertex > length - 20 && endVertex < length) {
//                lastSectionStr = sentence.substring(endVertex - 1);
//            } else {
//                lastSectionStr = "";
//                endVertex = length + 1;
//            }
//        } else {
//            endVertex = length + 1;
//        }
//        return endVertex;
//    }
//
//    private def getShortestPathToStopVertex(sentence: String, sectionSegment: Boolean) : Path = {
//        buildGraph(sentence, 0);
//        Int sentenceLength = sentence.length();
//        dijk.setGraph(graph);
//        Path p;
//        if (!sectionSegment) {
//            p = dijk.getShortestPath(1, sentenceLength + 1);
//        } else {
//            Int stopVertex = lookupStopVertex(sentence);
//            if (stopVertex > 1) {
//                p = dijk.getShortestPath(1, stopVertex);
//            } else {
//                p = dijk.getShortestPath(1, sentenceLength + 1);
//            }
//        }
//        return p;
//    }
//
//    private def segment(sentence: String, withPOS: Boolean, sectionSegment: Boolean) : SegmentResult = {
//        Path path = getShortestPathToStopVertex(sentence, sectionSegment);
//        SegmentResult result = buildSegmentResult(path);
//        if (withPinyin) {
//            WordToPinyinClassfierFactory.getInstance().getClassifier().classify(result);
//        }
//        if (withPOS) {
//            result.setPOSArray(posTagging.findPOS(path, graph));
//            setConcepts(result, path);
//        }
//        graph.clear();
//        return result;
//    }
//
//    private def setConcepts(result: SegmentResult, path: Path) {
//        Int length = path.getLength();
//        if (length == 0) {
//            return;
//        }
//        Array[IWord] words = new Array[IWord](length);
//        Array[Int] posArray = new Array[Int](length);
//        for (Int index = 0; index < length; index++) {
//            words(index) = graph.getEdgeObject(path.iget(index), path.iget(index + 1));
//            posArray(index) = result.getPOS(index);
//        }
//        conceptRecognizer.reset();
//        conceptRecognizer.setPosArray(posArray);
//        conceptRecognizer.setWordArray(words);
//        result.setConcepts(conceptRecognizer.getConcepts());
//    }
//
//    private var dijk : IShortestPath = null
//    private var graph : IGraph = null
//    private var posTagging : IPOSRecognizer = null
//    private var withPinyin : Boolean = null
//    private var conceptRecognizer : IConceptRecognizer = new SimpleConceptRecognizer();
//    private var lastSection : Boolean = null
//    private var lastSectionStr : String = null
//    private var useDomainDictionary : Boolean = null
//    private var useContextFreqSegment : Boolean = false;
//    private var config : MPSegmentConfiguration = null
//}
