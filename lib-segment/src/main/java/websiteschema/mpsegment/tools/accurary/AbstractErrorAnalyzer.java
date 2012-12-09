package websiteschema.mpsegment.tools.accurary;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

abstract class AbstractErrorAnalyzer implements ErrorAnalyzer {

    private int occurTimes = 0;
    private Map<String, Integer> wordsWithError = new ConcurrentHashMap<String, Integer>();

    @Override
    public int getErrorOccurTimes() {
        return occurTimes;
    }

    @Override
    public Map<String, Integer> getWords() {
        return wordsWithError;
    }

    @Override
    public void postAnalysis(Map<String, Integer> allWordsAndFreqInCorpus) {
    }

    protected void increaseOccur() {
        occurTimes++;
    }

    protected void addErrorWord(String word) {
        int occurs = wordsWithError.containsKey(word) ? wordsWithError.get(word) + 1 : 1;
        wordsWithError.put(word, occurs);
    }

    protected void removeErrorWord(String word) {
        wordsWithError.remove(word);
    }
}
