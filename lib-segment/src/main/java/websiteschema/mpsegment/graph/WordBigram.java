package websiteschema.mpsegment.graph;

import websiteschema.mpsegment.hmm.Node;
import websiteschema.mpsegment.hmm.NodeRepository;
import websiteschema.mpsegment.hmm.Trie;
import websiteschema.mpsegment.util.SerializeHandler;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WordBigram {

    private static final Map<String,WordBigram> mapWordBigram = new ConcurrentHashMap<String, WordBigram>();
    private Trie trie = new Trie();
    private NodeRepository nodeRepository = new NodeRepository();

    public WordBigram() {
    }

    public WordBigram(Trie trie, NodeRepository nodeRepository) {
        this.trie = trie;
        this.nodeRepository = nodeRepository;
    }

    public WordBigram(String resource) {
        try {
            load(resource);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static WordBigram getInstance(String corpusName) {
        return mapWordBigram.containsKey(corpusName) ? mapWordBigram.get(corpusName) : createWordBigram(corpusName);
    }

    private static WordBigram createWordBigram(String corpusName) {
        WordBigram instance;
        synchronized (mapWordBigram) {
            if(!mapWordBigram.containsKey(corpusName)) {
                instance = new WordBigram(corpusName);
                mapWordBigram.put(corpusName, instance);
            } else {
                instance = mapWordBigram.get(corpusName);
            }
        }
        return instance;
    }

    public void load(String resource) throws IOException {
        InputStream inputStream = getInputStream(resource);
        SerializeHandler reader = new SerializeHandler(new DataInputStream(inputStream));
        nodeRepository.load(reader);
        trie.load(reader);
        trie.buildIndex(1);
    }

    public double getProbability(String word1, String word2) {
        Node firstWord = nodeRepository.get(getWordFirstTwoChars(word1));
        Node secondWord = nodeRepository.get(getWordFirstTwoChars(word2));
        if(firstWord == null || secondWord == null) {
            return -1;
        }
        int[] bigram = new int[]{
                firstWord.getIndex(),
                secondWord.getIndex()};
        Trie node = trie.searchNode(bigram);
        return null != node ? node.getProb() : -1;
    }

    private InputStream getInputStream(String resource) throws FileNotFoundException {
        InputStream inputStream = null;
        File file = new File(resource);
        if (file.exists()) {
            inputStream = new FileInputStream(file);
        } else {
            inputStream = getClass().getClassLoader().getResourceAsStream(resource);
        }
        return inputStream;
    }

    private String getWordFirstTwoChars(String word) {
        return word.length() > 2 ? word.substring(word.length() - 2) : word;
    }
}
