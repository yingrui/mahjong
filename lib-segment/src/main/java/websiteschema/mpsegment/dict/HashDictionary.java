package websiteschema.mpsegment.dict;

import websiteschema.mpsegment.util.BufReader;
import websiteschema.mpsegment.util.ByteArrayReader;
import websiteschema.mpsegment.util.FileUtil;

import java.io.*;
import java.util.*;

public class HashDictionary<Word extends IWord> implements IDictionary {

    public HashDictionary(String dictResource) {
        maxWordLength = 0;
        if (!loaded) {
            loadDict(dictResource);
            loaded = true;
        }
    }

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

    private synchronized void loadDict(String s) {
        try {
            BufReader bufReader = new ByteArrayReader(FileUtil.getResourceAsStream("segment.dict"));
            loadDict(bufReader);
            bufReader.close();
            bufReader = null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadDict(BufReader bufreader)
            throws IOException {
        int wordOccuredSum = 0;
        maxWordLength = 0;
        int wordCount = 0;
        int totalHeader = bufreader.readInt();
        headIndexers = new ArrayList<HeadIndexer<Word>>();
        for (int i = 0; i < totalHeader; i++) {
            HeadIndexer headindexer = new HeadIndexer(bufreader);
            wordOccuredSum += headindexer.getWordOccuredSum();
            if (maxWordLength < headindexer.getMaxWordLength()) {
                maxWordLength = headindexer.getMaxWordLength();
            }
            wordCount += headindexer.getWordCount();
            headIndexers.add(headindexer);
            headIndexersHashMap.put(headindexer.getHeadStr(), headindexer);
        }
    }

    protected HeadIndexer<Word> lookupHeadIndexer(String head) {
        return headIndexersHashMap.get(head);
    }

    @Override
    public Word getWord(String wordStr) {
        HeadIndexer<Word> headIndexer = lookupHeadIndexer(getHead(wordStr));
        if (headIndexer != null) {
            Word word = headIndexer.findWord(wordStr);
            if (word != null) {
                return word;
            }
        }
        return null;
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

    public Word lookupWord(String wordStr) {
        Word word = null;
        HeadIndexer<Word> headindexer = lookupHeadIndexer(getHead(wordStr));
        if (headindexer != null) {
            word = headindexer.get(wordStr);
        }
        return word;
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
