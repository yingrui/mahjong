/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.core;

import org.apache.log4j.Logger;
import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.dict.*;
import websiteschema.mpsegment.dict.domain.DomainDictFactory;
import websiteschema.mpsegment.dict.domain.DomainDictionary;
import websiteschema.mpsegment.graph.IGraph;
import websiteschema.mpsegment.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ray
 */
public class GraphBuilder {

    private static Logger logger = Logger.getLogger(GraphBuilder.class);
    private final IGraph graph;
    private final double logCorpus = MPSegmentConfiguration.LOG_CORPUS;
    private final HashDictionary hashDictionary = DictionaryFactory.getInstance().getCoreDictionary();
    private final boolean useDomainDictionary;
    private Map<String, Integer> contextFreqMap = new HashMap<String, Integer>();
    private boolean useContextFreqSegment = false;

    public GraphBuilder(IGraph graph, boolean useDomainDictionary) {
        this.graph = graph;
        this.useDomainDictionary = useDomainDictionary;
    }

    private String doUpperCaseAndHalfShape(String word) {
        if (isUpperCaseOrHalfShapeAll) {
            if (isHalfShapeAll && isUpperCaseAll) {
                return StringUtil.doUpperCaseAndHalfShape(word);
            }
            if (isHalfShapeAll) {
                return StringUtil.halfShape(word);
            }
            return StringUtil.toUpperCase(word);
        }
        return word;
    }

    private int scanEnglishWordAndShorten(int begin) {
        final int sentenceLength = sentence.length();
        int index = begin;
        if (StringUtil.isCharAlphabeticalOrDigital(sentence.charAt(index))) {
            while (index < sentenceLength && StringUtil.isCharAlphabeticalOrDigital(sentence.charAt(index))) {
                index++;
            }
            index--;
        }
        return index;
    }

    private int scanTheMinWordLength(int begin) {
        int index = scanEnglishWordAndShorten(begin);
        int minWordLen = (index - begin) + 1;
        return minWordLen;
    }

    private String getCandidateSentence(int begin) {
        String candidateWord = "";
        final int slen = sentence.length();
        final int rest = slen - begin;
        if (maxWordLength <= rest) {
            int end = (begin + maxWordLength + lastMinWordLen) - 1;
            end = end < slen
                    ? end : slen;
            candidateWord = sentence.substring(begin, end);
        } else {
            candidateWord = sentence.substring(begin);
        }
        return candidateWord;
    }

    private int getWeight(IWord word) {
        if (useContextFreqSegment) {
            String wordName = word.getWordName();
            int weight = 1;
            if (wordName.length() > 1) {
                int contextFreq = contextFreqMap.containsKey(wordName) ? contextFreqMap.get(wordName) : 1;
                int freq = getFreqWeight(word);
                weight = (int) (freq + getContextFreqWeight(freq, contextFreq));
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

    private void addEdgeObject(int head, int tail, int weight, IWord word) {
        logger.trace(word.getWordName() + " weight " + weight);
        graph.addEdge(head, tail, weight, word);
    }

    public void scanContextFreq(final int startPos) {
        lastMinWordLen = 0;
        try {
            final int sentencelength = sentence.length();
            for (int begin = startPos; begin < sentencelength; begin += lastMinWordLen) {
                lastMinWordLen = scanTheMinWordLength(begin);

                //find all possible slices except single word
                final String candidateWord = getCandidateSentence(begin);
                final IWord the1stMatchWord = getItem(candidateWord, lastMinWordLen);
                if (the1stMatchWord != null && the1stMatchWord.getWordLength() > 1) {
                    //查找结果不为空且不是单字词
                    increaseContextFreq(the1stMatchWord.getWordName());
                    if (the2ndMatchWord != null && the2ndMatchWord.getWordLength() > 1) {
                        increaseContextFreq(the2ndMatchWord.getWordName());
                    }
                    if (the3rdMatchWord != null && the3rdMatchWord.getWordLength() > 1) {
                        increaseContextFreq(the3rdMatchWord.getWordName());
                    }
                }
            }
        } finally {
            lastMinWordLen = 0;
            the2ndMatchWord = null;
            the3rdMatchWord = null;
        }
    }

    private void increaseContextFreq(String word) {
        if (contextFreqMap.containsKey(word)) {
            int freq = contextFreqMap.get(word);
            contextFreqMap.put(word, freq + 1);
        } else {
            contextFreqMap.put(word, 1);
        }
    }

    public void setSentence(String sen) {
        sentence = doUpperCaseAndHalfShape(sen);
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

        for (int begin = startPos; begin < length; begin += lastMinWordLen) {
            final int minWordLen = scanTheMinWordLength(begin);
            this.lastMinWordLen = minWordLen;

            //find single char word or multi-chars alpha-numeric word
            String atomWord = sentence.substring(begin, begin + minWordLen);
            IWord singleCharWord = getItem(atomWord, minWordLen); // single char word 单字词
            if (singleCharWord == null) {
                singleCharWord = initAsUnknownWord(atomWord);//Unknown Word
            }

            //find all possible slices except single word
            final String candidateWord = getCandidateSentence(begin);
            final IWord the1stMatchWord = getItem(candidateWord, minWordLen);
            //将查找到的词添加到图中。
            //为了减少图的分支，同时因为单字词在中文中往往没有太多意义。
            //如果存在多个多字词，则不向图中添加单字词
            boolean shouldAddSingleCharWord = the1stMatchWord == null || the1stMatchWord.getWordLength() == 1 || matchedWordCount < 2;
            if (shouldAddSingleCharWord) {
                addSingleCharWordToGraph(begin, singleCharWord);
            }
            boolean foundOtherWords = the1stMatchWord != null && the1stMatchWord.getWordLength() > 1;
            if (foundOtherWords) {
                addMatchedWordToGraph(begin, the2ndMatchWord);
                addMatchedWordToGraph(begin, the3rdMatchWord);
                boolean segmentMin = MPSegmentConfiguration.getINSTANCE().isSegmentMin();
                if (!segmentMin || !isMatchedMoreThanOneWord() || !isFirstMatchedWordBigWord(the1stMatchWord) || isAtomWordAccordingPOS(the1stMatchWord)) {
                    addMatchedWordToGraph(begin, the1stMatchWord);
                }
            }
        }
    }

    private boolean isFirstMatchedWordBigWord(IWord the1stMatchWord) {
        return the1stMatchWord.getWordLength() > BigWordLength;
    }

    private boolean isMatchedMoreThanOneWord() {
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

    private void addSingleCharWordToGraph(int begin, IWord singleCharWord) {
        addEdgeObject(begin + 1, begin + lastMinWordLen + 1, getWeight(singleCharWord), singleCharWord);
    }

    private IWord get1stMatchWord(IWord[] words) {
        return null != words && words.length > 0 ? words[0] : null;
    }

    private IWord get2ndMatchWord(IWord[] words) {
        return null != words && words.length > 1 ? words[1] : null;
    }

    private IWord get3rdMatchWord(IWord[] words) {
        return null != words && words.length > 2 ? words[2] : null;
    }

    private IWord getMultiWordsInHashDictionary(String candidateWord) {
        IWord word = null;
        IWord[] words = hashDictionary.getWords(candidateWord);
        matchedWordCount = null != words ? words.length : 0;
        if (null != words) {
            word = get1stMatchWord(words);
            if (word != null) {
                the2ndMatchWord = get2ndMatchWord(words);
                the3rdMatchWord = get3rdMatchWord(words);
            }
        }
        return word;
    }

    private IWord getItem(String candidateWord, int minWordLength) {
        IWord word = null;
        if (useDomainDictionary && (loadDomainDictionary || loadUserDictionary) && candidateWord.length() > 1) {
            word = getDomainDictionary().getWordItem(candidateWord, minWordLength);
        }
        if (word == null) {
            //如果在领域词典中没有找到对应的词
            //则在核心词典中继续查找
            word = getMultiWordsInHashDictionary(candidateWord);
        } else {
            //如果在领域词典中找到对应的词
            //继续在核心词典中查找，并排除在返回结果中和领域词典里相同的词
            IWord[] words = hashDictionary.getWords(candidateWord);
            matchedWordCount = null != words ? words.length : 0;
            the2ndMatchWord = get1stMatchWord(words);
            if (the2ndMatchWord != null) {
                if (word.getWordName().length() == the2ndMatchWord.getWordName().length()) {
                    //如果用户辞典或领域辞典的长度和普通辞典相同
                    the2ndMatchWord = get2ndMatchWord(words);
                    the3rdMatchWord = get3rdMatchWord(words);
                    if (the2ndMatchWord != null) {
                        if (word.getWordName().length() == the2ndMatchWord.getWordName().length()) {
                            the2ndMatchWord = null;
                        }
                        if (the3rdMatchWord != null && word.getWordName().length() == the3rdMatchWord.getWordName().length()) {
                            the3rdMatchWord = null;
                        }
                        if (the2ndMatchWord == null) {
                            the2ndMatchWord = the3rdMatchWord;
                            the3rdMatchWord = null;
                        }
                    }
                } else {
                    //如果用户辞典或领域辞典的长度和普通辞典不相同
                    the3rdMatchWord = get2ndMatchWord(words);
                    if (the3rdMatchWord != null) {
                        if (word.getWordName().length() == the3rdMatchWord.getWordName().length()) {
                            the3rdMatchWord = get3rdMatchWord(words);
                        }
                        if (the3rdMatchWord != null && word.getWordName().length() == the3rdMatchWord.getWordName().length()) {
                            the3rdMatchWord = get3rdMatchWord(words);
                        }
                    }
                }
            }
        }
        return word;
    }

    private DomainDictionary getDomainDictionary() {
        return DomainDictFactory.getInstance().getDomainDictionary();
    }

    private IWord initAsUnknownWord(String unknownWordStr) {
        return new UnknownWord(unknownWordStr);
    }

    public Map<String, Integer> getContextFreqMap() {
        return contextFreqMap;
    }

    public boolean isUseContextFreqSegment() {
        return useContextFreqSegment;
    }

    public void setUseContextFreqSegment(boolean useContextFreqSegment) {
        this.useContextFreqSegment = useContextFreqSegment;
    }

    private final static boolean isUpperCaseAll = MPSegmentConfiguration.getINSTANCE().isUpperCaseAll();
    private final static boolean isHalfShapeAll = MPSegmentConfiguration.getINSTANCE().isHalfShapeAll();
    private final static boolean isUpperCaseOrHalfShapeAll = isUpperCaseAll || isHalfShapeAll;
    private final static int maxWordLength = MPSegmentConfiguration.getINSTANCE().getMaxWordLength();
    private final static boolean loadDomainDictionary = MPSegmentConfiguration.getINSTANCE().isLoadDomainDictionary();
    private final static boolean loadUserDictionary = MPSegmentConfiguration.getINSTANCE().isLoadUserDictionary();
    private final static int BigWordLength = 4;
    private IWord the2ndMatchWord;
    private IWord the3rdMatchWord;
    private int matchedWordCount = 0;
    int lastMinWordLen = 0;
    String sentence = "";
}
