package websiteschema.mpsegment.dict.domain;

import org.apache.log4j.Logger;
import websiteschema.mpsegment.dict.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DomainDictionary implements IDictionary {

    public DomainDictionary() {
        wordNameIndexHashMap = new HashMap<String, Integer>();
        arrayWordItem = new ArrayList<DomainWordItem>();
        synonymIndexHashMap = new HashMap<Integer, Integer>();
        synonymHashMap = new HashMap<Integer, List<Integer>>();
        hashDictionary = new HashDictionary<DomainWordItem>();
        hashDictionary.setHeadLength(2);
    }

    private DomainWordItem lookupWord(String wordName) {
        return hashDictionary.lookupWord(wordName);
    }

    private DomainWordItem getWord(int wordIndex) {
        return wordIndex >= 0 ? this.arrayWordItem.get(wordIndex) : null;
    }

    private int getWordIndex(String wordName) {
        return wordNameIndexHashMap.containsKey(wordName)
                ? wordNameIndexHashMap.get(wordName) : -1;
    }

    private int addWord(String wordName, String pos, int freq, int domainType) throws DictionaryException {
        DomainWordItem word = new DomainWordItem(wordName, domainType);
        word.setOccuredCount(pos, freq);
        int index = arrayWordItem.size();
        wordNameIndexHashMap.put(wordName, index);
        arrayWordItem.add(word);
        hashDictionary.addWord(word);
        if (wordName.length() > maxWordLength) {
            maxWordLength = wordName.length();
        }
        return index;
    }

    public void pushWord(String wordName, String synonym, String pos, int freq, int domainType) throws DictionaryException {
        if (wordName == null || wordName.trim().equals("") || pos == null || pos.trim().equals("") || wordName.trim().length() < 2) {
            l.warn("Load a error word '" + wordName + "'into domain dictionary, already ignored.");
            return;
        }
        wordName = wordName.trim();
        DomainWordItem word = lookupWord(wordName);
        int index = -1;
        if (null != word) {
            word.setOccuredCount(pos, freq);
            word.setDomainType(domainType);
            index = getWordIndex(wordName);
        } else {
            index = addWord(wordName, pos, freq, domainType);
        }
        if (null != synonym && !synonym.isEmpty()) {
            int synonymIndex = getWordIndex(synonym);
            addSynonym(index, synonymIndex);
        }
    }

    private void addSynonym(int index, int synonymIndex) {
        if (index >= 0 && synonymIndex >= 0) {
            this.synonymIndexHashMap.put(index, synonymIndex);
            List<Integer> synonymSet = this.synonymHashMap.get(synonymIndex);
            if (null == synonymSet) {
                synonymSet = new ArrayList<Integer>();
                synonymHashMap.put(synonymIndex, synonymSet);
            }
            if (!synonymSet.contains(index)) {
                synonymSet.add(index);
            }
        }
    }

    public List<IWord> getSynonymSet(String wordName) {
        int index = getWordIndex(wordName);
        if (index >= 0) {
            int synonymIndex = synonymIndexHashMap.containsKey(index) ? synonymIndexHashMap.get(index) : -1;
            List<Integer> synonymSet = null;
            if (synonymIndex >= 0) {
                synonymSet = synonymHashMap.get(synonymIndex);
            } else {
                synonymSet = synonymHashMap.get(index);
            }
            if (null != synonymSet) {
                List<IWord> ret = new ArrayList<IWord>();
                DomainWordItem head = getWord(synonymIndex >= 0 ? synonymIndex : index);
                if (null != head) {
                    ret.add(head);
                }
                for (int i : synonymSet) {
                    ret.add(getWord(i));
                }
                return ret;
            }
        }
        return null;
    }

    @Override
    public IWord getWord(String wordName) {
        return hashDictionary.getWord(wordName);
    }

    public IWord getWordItem(String wordName, int minLength) {
        int l1 = maxWordLength;
        if (wordName.length() < l1) {
            l1 = wordName.length();
        }
        DomainWordItem word = null;
        for (int i2 = l1; i2 >= minLength; i2--) {
            String s2 = wordName.substring(0, i2);
            int wordIndex = getWordIndex(s2);
            if (wordIndex <= 0) {
                continue;
            }
            word = getWord(wordIndex);
            break;
        }

        return word;
    }

    @Override
    public IWord[] getWords(String wordStr) {
        return hashDictionary.getWords(wordStr);
    }

    @Override
    public Iterator<IWord> iterator() {
        return (Iterator) arrayWordItem.iterator();
    }

    private HashDictionary<DomainWordItem> hashDictionary;
    private HashMap<String, Integer> wordNameIndexHashMap;
    private List<DomainWordItem> arrayWordItem;
    private HashMap<Integer, Integer> synonymIndexHashMap;
    private HashMap<Integer, List<Integer>> synonymHashMap;
    private int maxWordLength = 0;
    private final static Logger l = Logger.getLogger("segment");
}
