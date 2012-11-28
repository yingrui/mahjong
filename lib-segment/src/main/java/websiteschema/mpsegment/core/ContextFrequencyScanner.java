package websiteschema.mpsegment.core;

import websiteschema.mpsegment.dict.DictionaryLookupResult;
import websiteschema.mpsegment.dict.IWord;

import java.util.HashMap;
import java.util.Map;

public class ContextFrequencyScanner extends AbstractWordScanner {

    private final Map<String, Integer> contextFreqMap;

    public ContextFrequencyScanner() {
        contextFreqMap = new HashMap<String, Integer>();
    }

    public Map<String, Integer> getContextFreqMap() {
        return contextFreqMap;
    }

    @Override
    public IWord foundAtomWord(String atomWord) {
        return null;
    }

    @Override
    public void processFoundWordItems(int begin, IWord singleCharWord, DictionaryLookupResult lookupResult) {
        IWord the1stMatchWord = lookupResult.firstMatchWord;
        IWord the2ndMatchWord = lookupResult.the2ndMatchWord;
        IWord the3rdMatchWord = lookupResult.the3rdMatchWord;
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

    private void increaseContextFreq(String word) {
        if (contextFreqMap.containsKey(word)) {
            int freq = contextFreqMap.get(word);
            contextFreqMap.put(word, freq + 1);
        } else {
            contextFreqMap.put(word, 1);
        }
    }
}
