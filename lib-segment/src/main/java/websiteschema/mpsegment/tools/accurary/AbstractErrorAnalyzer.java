package websiteschema.mpsegment.tools.accurary;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

abstract class AbstractErrorAnalyzer implements ErrorAnalyzer {

    private int occurTimes = 0;
    private Set<String> wordsWithError = new ConcurrentSkipListSet<String>();

    @Override
    public int getErrorOccurTimes() {
        return occurTimes;
    }

    @Override
    public Set<String> getWords() {
        return wordsWithError;
    }

    @Override
    public void postAnalysis(Map<String, Integer> allWordsAndFreqInCorpus) {
    }

    protected void increaseOccur() {
        occurTimes++;
    }

    protected void addErrorWord(String word) {
        wordsWithError.add(word);
    }

    protected void removeErrorWord(String word) {
        wordsWithError.remove(word);
    }
}
