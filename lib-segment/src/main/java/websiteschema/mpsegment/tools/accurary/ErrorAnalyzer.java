package websiteschema.mpsegment.tools.accurary;

import websiteschema.mpsegment.core.WordAtom;

import java.util.Map;
import java.util.Set;

public interface ErrorAnalyzer {

    public int getErrorOccurTimes();

    public Set<String> getWords();

    public boolean analysis(WordAtom expect, String possibleErrorWord);

    public void postAnalysis(Map<String, Integer> allWordsAndFreqInCorpus);
}
