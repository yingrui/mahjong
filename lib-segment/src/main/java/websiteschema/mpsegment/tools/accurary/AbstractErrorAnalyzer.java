package websiteschema.mpsegment.tools.accurary;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

abstract class AbstractErrorAnalyzer implements ErrorAnalyzer {

    private int occurTimes = 0;
    private Map<String, Integer> wordsWithError = new ConcurrentHashMap<String, Integer>();

    @Override
    public int getErrorOccurTimes() {
        return occurTimes;
    }

    @Override
    public Map<String,Integer> getWords() {
        return sort();
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

    private Map<String,Integer> sort() {
        List<Map.Entry<String,Integer>> entries = new ArrayList<Map.Entry<String,Integer>>(wordsWithError.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String,Integer>>() {
            @Override
            public int compare(Map.Entry<String,Integer> o1, Map.Entry<String,Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>(entries.size());
        for(Map.Entry<String, Integer> mapEntry : entries) {
            sortedMap.put(mapEntry.getKey(), mapEntry.getValue());
        }
        return sortedMap;
    }
}
