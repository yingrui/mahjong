package websiteschema.mpsegment.dict;

import java.util.*;

public class HashDictionary<Word extends IWord> implements IDictionary {

    public HashDictionary() {
        maxWordLength = 0;
        headIndexers = new ArrayList<HeadIndexer<Word>>();
    }

    public int getCapacity() {
        return headIndexersHashMap.size();
    }

    public synchronized void clear() {
        headIndexersHashMap.clear();
    }

    protected HeadIndexer<Word> lookupHeadIndexer(String head) {
        return headIndexersHashMap.get(head);
    }

    @Override
    public Word getWord(String wordStr) {
        HeadIndexer<Word> headIndexer = lookupHeadIndexer(getHead(wordStr));
        return headIndexer != null ? headIndexer.findWord(wordStr) : null;
    }

    public Word lookupWord(String wordStr) {
        HeadIndexer<Word> headIndexer = lookupHeadIndexer(getHead(wordStr));
        return headIndexer != null ? headIndexer.get(wordStr) : null;
    }

    @Override
    public Word[] getWords(String sentenceStr) {
        HeadIndexer<Word> headIndexer = lookupHeadIndexer(getHead(sentenceStr));
        if (headIndexer != null) {
            Word[] words = headIndexer.findMultiWord(sentenceStr);
            if (words != null) {
                return words;
            }
        }
        return null;
    }

    @Override
    public Iterator<IWord> iterator() {
        List<Word> wordList = new LinkedList<Word>();
        for (int i = 0; i < headIndexers.size(); i++) {
            HeadIndexer headIndexer = headIndexers.get(i);
            IWordArray<Word> wordArray = headIndexer.getWordArray();
            wordList.addAll(Arrays.asList(wordArray.getWordItems()));
        }
        return (Iterator)wordList.iterator();
    }

    protected String getHead(String wordStr) {
        return wordStr.substring(0, headLength);
    }

    public void addWord(IWord word) throws DictionaryException {
        if (!loaded) {
            HeadIndexer headIndexer = lookupHeadIndexer(getHead(word.getWordName()));
            if (null == headIndexer) {
                headIndexer = createHeadIndexer(word);
            }
            headIndexer.add(word);
        } else {
            throw new DictionaryException("Dictionary is not in Edit mode.");
        }
    }

    private HeadIndexer createHeadIndexer(IWord word) {
        HeadIndexer headIndexer = new HeadIndexer(word, headLength);
        headIndexers.add(headIndexer);
        headIndexersHashMap.put(headIndexer.getHeadStr(), headIndexer);
        return headIndexer;
    }

    public void setHeadLength(int headLength) {
        this.headLength = headLength;
    }

    private int headLength = 1;
    private Map<String, HeadIndexer<Word>> headIndexersHashMap = new HashMap<String, HeadIndexer<Word>>();
    private List<HeadIndexer<Word>> headIndexers;
    private boolean loaded = false;
    private int maxWordLength;
}
