package websiteschema.mpsegment.core;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.graph.*;

public class MPSegment {

    public MPSegment(MPSegmentConfiguration config) {
        this.config = config;
        maxWordLength = 18;
        lastSection = false;
        lastSectionStr = "";
        useDomainDictionary = true;
        initialize();
    }

    public boolean isUseDomainDictionary() {
        return useDomainDictionary;
    }

    public void setUseDomainDictionary(boolean flag) {
        useDomainDictionary = flag;
    }

    public int getMaxWordLength() {
        return maxWordLength;
    }

    public void setMaxWordLength(int len) {
        maxWordLength = len;
    }

    public boolean isUseContextFreqSegment() {
        return useContextFreqSegment;
    }

    public void setUseContextFreqSegment(boolean useContextFreqSegment) {
        this.useContextFreqSegment = useContextFreqSegment;
    }

    public SegmentResult segmentMP(String sentence, boolean withPOS) {
        if (sentence == null || sentence.length() < 1) {
            return null;
        }
        lastSectionStr = "";
        final int totalLength = sentence.length();
        SegmentResult result;
        if (totalLength < 1023) {
            result = segment(sentence, withPOS, false);
        } else {
            lastSection = false;
            result = new SegmentResult(0);
            for (int startIndex = 0; startIndex < totalLength; ) {
                String section = getSection(sentence, startIndex);
                startIndex += section.length();
                lastSection = startIndex == totalLength;
                SegmentResult sectionResult = segment(section, withPOS, true);
                result.append(sectionResult);
                if (!lastSection && lastSectionStr.length() > 0) {
                    startIndex -= lastSectionStr.length();
                }
            }
        }
        return result;
    }

    private String getSection(String sentence, int startIndex) {
        String sectionedSentence;
        if (sentence.length() - startIndex >= 1000) {
            sectionedSentence = sentence.substring(startIndex, startIndex + 1000);
        } else {
            sectionedSentence = sentence.substring(startIndex);
        }
        return sectionedSentence;
    }

    private SegmentResult buildSegmentResult(Path path) {
        int length = path.getLength();
        String wordNames[] = new String[length];
        int domainTypes[] = new int[length];
        if (length < 1) {
            return null;
        }
        SegmentResult segmentResult = new SegmentResult(length);
        for (int index = 0; index < length; index++) {
            int edgeWeight = graph.getEdgeWeight(path.iget(index), path.iget(index + 1));
            if (edgeWeight == 0) {
                IWord word = graph.getEdgeObject(path.iget(index), path.iget(index + 1));
                wordNames[index] = word.getWordName();
                domainTypes[index] = word.getDomainType();
            } else {
                IWord word = graph.getEdgeObject(path.iget(index), path.iget(index + 1));
                wordNames[index] = word.getWordName();
                domainTypes[index] = word.getDomainType();
            }
        }

        segmentResult.setWords(wordNames);
        segmentResult.setDomainTypes(domainTypes);
        return segmentResult;
    }


    private void initialize() {
        initializeGraph();
        maxWordLength = config.isSegmentMin() ? 4 : config.getMaxWordLength();
        initializePOSTagging();
    }

    private void initializeGraph() {
        graph = new Graph();
        dijk = new DijkstraImpl();
    }

    private void initializePOSTagging() {
        posTagging = new POSRecognizer();
    }

    private void buildGraph(final String sen, final int startPos) {
        GraphBuilder builder = new GraphBuilder(graph, useDomainDictionary, config);
        builder.setUseContextFreqSegment(useContextFreqSegment);
        builder.buildGraph(sen, startPos);
    }

    private int lookupStopVertex(String sentence) {
        final int length = sentence.length();
        lastSectionStr = "";
        int endVertex = -2;
        if (!lastSection) {
            endVertex = graph.getStopVertex(length - 20, length);
            if (endVertex > 1 && endVertex > length - 20 && endVertex < length) {
                lastSectionStr = sentence.substring(endVertex - 1);
            } else {
                lastSectionStr = "";
                endVertex = length + 1;
            }
        } else {
            endVertex = length + 1;
        }
        return endVertex;
    }

    private Path getShortestPathToStopVertex(String sentence, boolean sectionSegment) {
        buildGraph(sentence, 0);
        final int sentenceLength = sentence.length();
        dijk.setGraph(graph);
        Path p;
        if (!sectionSegment) {
            p = dijk.getShortestPath(1, sentenceLength + 1);
        } else {
            int stopVertex = lookupStopVertex(sentence);
            if (stopVertex > 1) {
                p = dijk.getShortestPath(1, stopVertex);
            } else {
                p = dijk.getShortestPath(1, sentenceLength + 1);
            }
        }
        return p;
    }

    private SegmentResult segment(String sentence, boolean withPOS, boolean sectionSegment) {
        Path path = getShortestPathToStopVertex(sentence, sectionSegment);
        SegmentResult result = buildSegmentResult(path);
        if (withPOS) {
            result.setPOSArray(posTagging.findPOS(path, graph));
            setConcepts(result, path);
        }
        graph.clear();
        return result;
    }

    private void setConcepts(SegmentResult result, Path path) {
        final int length = path.getLength();
        if (length == 0) {
            return;
        }
        IWord[] words = new IWord[length];
        int[] posArray = new int[length];
        for (int index = 0; index < length; index++) {
            words[index] = graph.getEdgeObject(path.iget(index), path.iget(index + 1));
            posArray[index] = result.getPOS(index);
        }
        conceptRecognizer.reset();
        conceptRecognizer.setPosArray(posArray);
        conceptRecognizer.setWordArray(words);
        result.setConcepts(conceptRecognizer.getConcepts());
    }

    private int maxWordLength;
    private IShortestPath dijk;
    private IGraph graph;
    private IPOSRecognizer posTagging;
    private IConceptRecognizer conceptRecognizer = new SimpleConceptRecognizer();
    private boolean lastSection;
    private String lastSectionStr;
    private boolean useDomainDictionary;
    private boolean useContextFreqSegment = false;
    private MPSegmentConfiguration config;
}
