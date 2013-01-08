package websiteschema.mpsegment.graph;

import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.hmm.Node;
import websiteschema.mpsegment.hmm.NodeRepository;
import websiteschema.mpsegment.hmm.Trie;
import websiteschema.mpsegment.tools.PFRCorpusLoader;
import websiteschema.mpsegment.util.CharCheckUtil;
import websiteschema.mpsegment.util.SerializeHandler;

import java.io.*;

public class WordBigram {

//    private static final WordBigram instance = new WordBigram("word-bigram.dat");
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

//    public static WordBigram getInstance() {
//        return instance;
//    }

    public static void main(String args[]) throws IOException {
        String folder = "src/test/resources";
        if (args.length > 0) {
            folder = args[0];
        } else {
            System.out.println("prepare to train a model with corpus in folder: corpus/LCMC.");
        }

        WordBigram bigram = new WordBigram();
        File dir = new File(folder);
        if (dir.isDirectory() && dir.exists()) {
            for (File f : dir.listFiles()) {
                String filename = f.getName();
                if (filename.endsWith(".txt") && filename.startsWith("PFR-")) {
                    bigram.train(f.getAbsolutePath());
                }
            }
        }
        bigram.build();

        bigram.save("word-bigram.dat");
    }

    public void build() {
        trie.buildIndex(1);
    }

    public void save(String filename) throws IOException {
        SerializeHandler writer = new SerializeHandler(new File(filename), SerializeHandler.MODE_WRITE_ONLY);
        nodeRepository.save(writer);
        trie.save(writer);
    }

    public void load(String resource) throws IOException {
        InputStream inputStream = getInputStream(resource);
        SerializeHandler reader = new SerializeHandler(new DataInputStream(inputStream));
        nodeRepository.load(reader);
        trie.load(reader);
    }

    public double getProbability(String word1, String word2) {
        Node firstWord = nodeRepository.get(word1);
        Node secondWord = nodeRepository.get(word2);
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

    public void train(String filename) throws IOException {
        File file = new File(filename);
        if (file.exists()) {
            System.out.println("[WordBigram] train file " + file.getAbsolutePath());
            PFRCorpusLoader corpusLoader = new PFRCorpusLoader(new FileInputStream(file));
            SegmentResult segmentResult = corpusLoader.readLine();
            while (null != segmentResult) {
                analysis(segmentResult);
                segmentResult = corpusLoader.readLine();
            }
        }
    }

    private void analysis(SegmentResult segmentResult) {
        for (int i = 0; i < segmentResult.length() - 1; i++) {
            String word1 = segmentResult.getWord(i);
            String word2 = segmentResult.getWord(i + 1);
            statisticBigram(word1, word2);
        }
    }

    private void statisticBigram(final String word1, final String word2) {
        if (CharCheckUtil.isChinese(word1) && CharCheckUtil.isChinese(word2)) {
            Node wordNode1 = new Node(getWordLastTwoChars(word1));
            Node wordNode2 = new Node(getWordLastTwoChars(word2));
            wordNode1 = nodeRepository.add(wordNode1);
            wordNode2 = nodeRepository.add(wordNode2);
            trie.insert(new int[]{wordNode1.getIndex(), wordNode2.getIndex()});
        }
    }

    private String getWordLastTwoChars(String word) {
        return word.length() > 2 ? word.substring(word.length() - 2) : word;
    }
}
