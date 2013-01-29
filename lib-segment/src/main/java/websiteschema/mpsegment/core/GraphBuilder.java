package websiteschema.mpsegment.core;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.dict.DictionaryService;
import websiteschema.mpsegment.graph.IGraph;
import websiteschema.mpsegment.util.StringUtil;

import java.util.Map;

public class GraphBuilder {

    public GraphBuilder(IGraph graph, boolean useDomainDictionary, MPSegmentConfiguration config) {
        this.graph = graph;
        segmentMin = config.isSegmentMin();
        upperCaseAll = config.isUpperCaseAll();
        halfShapeAll = config.isHalfShapeAll();
        upperCaseOrHalfShapeAll = upperCaseAll || halfShapeAll;
        loadDomainDictionary = config.isLoadDomainDictionary();
        loadUserDictionary = config.isLoadUserDictionary();
        maxWordLength = config.getMaxWordLength();
        dictionaryService = new DictionaryService(useDomainDictionary, loadDomainDictionary, loadUserDictionary);
    }

    private String doUpperCaseAndHalfShape(String word) {
        if (upperCaseOrHalfShapeAll) {
            if (halfShapeAll && upperCaseAll) {
                return StringUtil.doUpperCaseAndHalfShape(word);
            }
            if (halfShapeAll) {
                return StringUtil.halfShape(word);
            }
            return StringUtil.toUpperCase(word);
        }
        return word;
    }

    public void scanContextFreq(final int startPos) {
        ContextFrequencyScanner scanner = getContextFrequencyScanner();
        scanner.startScanningAt(startPos);
        this.contextFreqMap = scanner.getContextFreqMap();
    }

    private ContextFrequencyScanner getContextFrequencyScanner() {
        ContextFrequencyScanner scanner = new ContextFrequencyScanner();
        scanner.setSentence(sentence);
        scanner.setDictionaryService(dictionaryService);
        scanner.setMaxWordLength(maxWordLength);
        return scanner;
    }

    public void setSentence(String sen) {
        sentence = sen;//doUpperCaseAndHalfShape(sen);
    }

    public void buildGraph(final String sen, final int startPos) {
        setSentence(sen);
        if (useContextFreqSegment) {
            scanContextFreq(startPos);
        }
        final int length = sentence.length();
        for (int i = 0; i < length; i++) {
            graph.addVertex();
        }

        SegmentWordScanner scanner = getSegmentWordScanner();
        scanner.startScanningAt(startPos);
    }

    private SegmentWordScanner getSegmentWordScanner() {
        SegmentWordScanner scanner = new SegmentWordScanner(segmentMin, useContextFreqSegment, graph, contextFreqMap);
        scanner.setSentence(sentence);
        scanner.setMaxWordLength(maxWordLength);
        scanner.setDictionaryService(dictionaryService);
        return scanner;
    }

    public Map<String, Integer> getContextFreqMap() {
        return contextFreqMap;
    }

    public void setUseContextFreqSegment(boolean useContextFreqSegment) {
        this.useContextFreqSegment = useContextFreqSegment;
    }

    private Map<String, Integer> contextFreqMap;
    private final IGraph graph;
    private final boolean segmentMin;
    private final boolean upperCaseAll;
    private final boolean halfShapeAll;
    private final boolean upperCaseOrHalfShapeAll;
    private final boolean loadDomainDictionary;
    private final boolean loadUserDictionary;
    private boolean useContextFreqSegment = false;
    private DictionaryService dictionaryService;
    private String sentence = "";
    private int maxWordLength;
}

