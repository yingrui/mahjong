package websiteschema.mpsegment.core;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.dict.DictionaryLookupResult;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.POSUtil;
import websiteschema.mpsegment.dict.UnknownWord;
import websiteschema.mpsegment.graph.IGraph;

import java.util.Map;

public class SegmentWordScanner extends AbstractWordScanner {

    private final static int BigWordLength = 4;
    private final static double logCorpus = MPSegmentConfiguration.LOG_CORPUS;
    private final Map<String, Integer> contextFreqMap;
    private boolean segmentMin;
    private boolean useContextFreqSegment;
    private IGraph graph;

    public SegmentWordScanner(boolean segmentMin, boolean useContextFreqSegment, IGraph graph, Map<String, Integer> contextFreqMap) {
        this.segmentMin = segmentMin;
        this.useContextFreqSegment = useContextFreqSegment;
        this.graph = graph;
        this.contextFreqMap = contextFreqMap;
    }

    @Override
    public IWord foundAtomWord(String atomWord) {
        IWord singleCharWord = getDictionaryService().lookup(atomWord).firstMatchWord;
        if (singleCharWord == null) {
            singleCharWord = initAsUnknownWord(atomWord);//Unknown Word
        }
        return singleCharWord;
    }

    @Override
    public void processFoundWordItems(int begin, IWord singleCharWord, DictionaryLookupResult lookupResult) {
        IWord the1stMatchWord = lookupResult.firstMatchWord;
        IWord the2ndMatchWord = lookupResult.the2ndMatchWord;
        IWord the3rdMatchWord = lookupResult.the3rdMatchWord;
        int matchedWordCount = lookupResult.matchedWordCount;
        //将查找到的词添加到图中。
        //为了减少图的分支，同时因为单字词在中文中往往没有太多意义。
        //如果存在多个多字词，则不向图中添加单字词
        boolean shouldAddSingleCharWord = the1stMatchWord == null || the1stMatchWord.getWordLength() == 1 || matchedWordCount < 2;
        if (shouldAddSingleCharWord) {
            addSingleCharWordToGraph(begin, singleCharWord.getWordLength(), singleCharWord);
        }
        boolean foundOtherWords = the1stMatchWord != null && the1stMatchWord.getWordLength() > 1;
        if (foundOtherWords) {
            addMatchedWordToGraph(begin, the2ndMatchWord);
            addMatchedWordToGraph(begin, the3rdMatchWord);
            if (!segmentMin || !isMatchedMoreThanOneWord(matchedWordCount) || !isFirstMatchedWordBigWord(the1stMatchWord) || isAtomWordAccordingPOS(the1stMatchWord)) {
                addMatchedWordToGraph(begin, the1stMatchWord);
            }
        }
    }

    private IWord initAsUnknownWord(String unknownWordStr) {
        return new UnknownWord(unknownWordStr);
    }

    private boolean isFirstMatchedWordBigWord(IWord the1stMatchWord) {
        return the1stMatchWord.getWordLength() > BigWordLength;
    }

    private boolean isMatchedMoreThanOneWord(int matchedWordCount) {
        return matchedWordCount > 1;
    }

    private boolean isAtomWordAccordingPOS(IWord the1stMatchWord) {
        int[][] wordPOSTable = the1stMatchWord.getWordPOSTable();
        int the1stMatchedWordPOS = null != wordPOSTable ? wordPOSTable[0][0] : -1;
        return the1stMatchedWordPOS == POSUtil.POS_I || the1stMatchedWordPOS == POSUtil.POS_L;
    }

    private void addMatchedWordToGraph(int begin, IWord matchedWord) {
        if (null != matchedWord) {
            addEdgeObject(begin + 1, begin + matchedWord.getWordLength() + 1, getWeight(matchedWord), matchedWord);
        }
    }

    private void addSingleCharWordToGraph(int begin, int lastMinWordLen, IWord singleCharWord) {
        addEdgeObject(begin + 1, begin + lastMinWordLen + 1, getWeight(singleCharWord), singleCharWord);
    }

    private void addEdgeObject(int head, int tail, int weight, IWord word) {
        graph.addEdge(head, tail, weight, word);
    }

    private int getWeight(IWord word) {
        if (useContextFreqSegment) {
            String wordName = word.getWordName();
            int weight = 1;
            if (wordName.length() > 1) {
                int contextFreq = contextFreqMap.containsKey(wordName) ? contextFreqMap.get(wordName) : 1;
                int freq = getFreqWeight(word);
                weight = freq + getContextFreqWeight(freq, contextFreq);
            } else {
                weight = getFreqWeight(word);
            }
            if (weight <= 0) {
                weight = 1;
            }
            return weight;
        }
        int weight = getFreqWeight(word);
        return weight;
    }

    private int getFreqWeight(IWord word) {
        int log2Freq = word.getLog2Freq();
        if (logCorpus > log2Freq) {
            int freqWeight = (int) (logCorpus - word.getLog2Freq());
            return freqWeight;
        } else {
            return 1;
        }
    }

    private int getContextFreqWeight(int freq, int contextFreq) {
        int weight = -(int) ((1 - Math.exp(-0.1 * (contextFreq - 1))) * freq);
        return weight;
    }
}
