package websiteschema.mpsegment.tools.accurary;

import websiteschema.mpsegment.core.WordAtom;

import java.util.Map;

public interface ErrorAnalyzer {

    public int getErrorOccurTimes();

    public Map<String, Integer> getWords();

    public boolean analysis(WordAtom expect, String possibleErrorWord);

    public void postAnalysis(Map<String, Integer> allWordsAndFreqInCorpus);
}
