package websiteschema.mpsegment.tools.accurary;

import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.core.SegmentWorker;
import websiteschema.mpsegment.core.WordAtom;
import websiteschema.mpsegment.tools.PFRCorpusLoader;
import websiteschema.mpsegment.util.NumberUtil;
import websiteschema.mpsegment.util.StringUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SegmentAccuracy {

    private PFRCorpusLoader loader;
    private int totalWords = 0;
    private int correct = 0;
    private int wrong = 0;
    private double accuracyRate;

    private HashMap<String, Integer> allWordsAndFreqInCorpus = new HashMap<String, Integer>();
    private SegmentWorker segmentWorker;
    private Map<SegmentErrorType, ErrorAnalyzer> allErrorAnalyzer;

    public SegmentAccuracy(String testCorpus, SegmentWorker segmentWorker) throws IOException {
        this.segmentWorker = segmentWorker;
        initialErrorAnalyzer();
        loader = new PFRCorpusLoader(getClass().getClassLoader().getResourceAsStream(testCorpus));
    }

    public double getAccuracyRate() {
        return accuracyRate;
    }

    public int getWrong() {
        return wrong;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public ErrorAnalyzer getErrorAnalyzer(SegmentErrorType errorType) {
        return allErrorAnalyzer.get(errorType);
    }

    public void checkSegmentAccuracy() {
        boolean isUseContextFreq = segmentWorker.isUseContextFreqSegment();
        segmentWorker.setUseContextFreqSegment(true);
        try {
            SegmentResult expectResult = loader.readLine();
            while (expectResult != null) {
                String sentence = expectResult.toOriginalString();
                SegmentResult actualResult = segmentWorker.segment(sentence);

                totalWords += expectResult.length();

                compare(expectResult, actualResult);
                expectResult = loader.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            postAnalysis();
            segmentWorker.setUseContextFreqSegment(isUseContextFreq);
        }
        assert (correct > 0 && totalWords > 0);
        accuracyRate = (double) correct / (double) totalWords;
    }

    private void initialErrorAnalyzer() {
        allErrorAnalyzer = new LinkedHashMap<SegmentErrorType, ErrorAnalyzer>();
        allErrorAnalyzer.put(SegmentErrorType.NER_NR, new NerNameErrorAnalyzer());
        allErrorAnalyzer.put(SegmentErrorType.NER_NS, new NerPlaceErrorAnalyzer());
        allErrorAnalyzer.put(SegmentErrorType.UnknownWord, new NewWordErrorAnalyzer());
        allErrorAnalyzer.put(SegmentErrorType.ContainDisambiguate, new ContainErrorAnalyzer());
        allErrorAnalyzer.put(SegmentErrorType.Other, new OtherErrorAnalyzer());
    }

    private void compare(SegmentResult expectResult, SegmentResult actualResult) {
        int lastMatchIndex = -1;
        for (int i = 0; i < expectResult.length(); i++) {
            WordAtom expectWord = expectResult.getWordAtom(i);
            recordWordFreqInCorpus(expectWord);
            int indexInOriginalString = expectResult.getWordIndexInOriginalString(i);
            int match = lookupMatch(actualResult, expectWord, lastMatchIndex + 1, indexInOriginalString);
            if (match >= 0) {
                lastMatchIndex = match;
                correct++;
            } else {
                wrong++;
            }
        }
    }

    private void recordWordFreqInCorpus(WordAtom word) {
        int freq = allWordsAndFreqInCorpus.containsKey(word.word) ? allWordsAndFreqInCorpus.get(word.word) + 1 : 1;
        allWordsAndFreqInCorpus.put(word.word, freq);
    }

    private int lookupMatch(SegmentResult actualResult, WordAtom expectWord, int start, final int indexInOriginalString) {
        for (int i = start; i < actualResult.length(); i++) {
            String actualWord = actualResult.getWord(i);
            if (isSameWord(expectWord.word, actualWord)) {
                if (actualResult.getWordIndexInOriginalString(i) == indexInOriginalString) {
                    return i;
                }
            }
        }
        analyzeErrorReason(actualResult, expectWord, start, indexInOriginalString);
        return -1;
    }

    private void analyzeErrorReason(SegmentResult actualResult, WordAtom expect, int start, int from) {
        String possibleErrorWord = lookupErrorWord(actualResult, expect, start, from);
        analyzeReason(expect, possibleErrorWord);
    }

    private void analyzeReason(WordAtom expect, String possibleErrorWord) {
        for(SegmentErrorType errorType : allErrorAnalyzer.keySet()) {
            ErrorAnalyzer analyzer = allErrorAnalyzer.get(errorType);
            boolean isErrorWord = analyzer.analysis(expect, possibleErrorWord);
            if(isErrorWord) {
                break;
            }
        }
    }

    private String lookupErrorWord(SegmentResult actualResult, WordAtom expect, int start, int from) {
        int to = from + expect.length();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = start; i < actualResult.length(); i++) {
            int indexInOriginalString = actualResult.getWordIndexInOriginalString(i);
            if (indexInOriginalString >= from && indexInOriginalString < to) {
                stringBuilder.append(actualResult.getWord(i)).append(" ");
            }
        }

        return stringBuilder.toString().trim();
    }

    private boolean isSameWord(String expect, String actual) {
        String expectWord = StringUtil.doUpperCaseAndHalfShape(expect);
        if (expectWord.equalsIgnoreCase(actual)) {
            return true;
        }
        if (Character.isDigit(actual.charAt(0))) {
            String number = NumberUtil.chineseToEnglishNumberStr(expect);
            if (actual.equals(number)) {
                return true;
            }
        }
        return false;
    }

    private void postAnalysis() {
        for (SegmentErrorType errorType : allErrorAnalyzer.keySet()) {
            getErrorAnalyzer(errorType).postAnalysis(allWordsAndFreqInCorpus);
        }
    }
}

