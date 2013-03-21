//package websiteschema.mpsegment.tools;
//
//import websiteschema.mpsegment.core.SegmentResult;
//import websiteschema.mpsegment.hmm.Node;
//import websiteschema.mpsegment.hmm.NodeRepository;
//import websiteschema.mpsegment.hmm.Trie;
//import websiteschema.mpsegment.util.CharCheckUtil;
//import websiteschema.mpsegment.util.SerializeHandler;
//
//import java.io.*;
//
//class WordBigramBuilder {
//
//    private var trie : Trie = new Trie();
//    private var nodeRepository : NodeRepository = new NodeRepository();
//
//    public static void main(Array[String] args) throws IOException {
//        var folder = "src/test/resources"
//        if (args.length > 0) {
//            folder = args(0);
//        } else {
//            println("prepare to train a model with corpus in folder: corpus/LCMC.");
//        }
//
//        var bigram = new WordBigramBuilder()
//        var dir = new File(folder)
//        if (dir.isDirectory() && dir.exists()) {
//            for (f <- dir.listFiles()) {
//                var filename = f.getName()
//                if (filename.endsWith(".txt") && filename.startsWith("PFR-")) {
//                    bigram.train(f.getAbsolutePath());
//                }
//            }
//        }
//        bigram.build();
//
//        bigram.save("word-bigram.dat");
//    }
//
//    def build() {
//        trie.buildIndex(1);
//    }
//
//    def save(filename: String) throws IOException {
//        var writer = new SerializeHandler(new File(filename), SerializeHandler.MODE_WRITE_ONLY)
//        nodeRepository.save(writer);
//        trie.save(writer);
//    }
//
//    def train(filename: String) throws IOException {
//        var file = new File(filename)
//        if (file.exists()) {
//            println("[WordBigramBuilder] train file " + file.getAbsolutePath());
//            var corpusLoader = new PFRCorpusLoader(new FileInputStream(file))
//            var segmentResult = corpusLoader.readLine()
//            while (null != segmentResult) {
//                analysis(segmentResult);
//                segmentResult = corpusLoader.readLine();
//            }
//        }
//    }
//
//    def getTrie() : Trie = {
//        return trie;
//    }
//
//    def getNodeRepository() : NodeRepository = {
//        return nodeRepository;
//    }
//
//    private def analysis(segmentResult: SegmentResult) {
//        for (Int i = 0; i < segmentResult.length() - 1; i++) {
//            var word1 = segmentResult.getWord(i)
//            var word2 = segmentResult.getWord(i + 1)
//            statisticBigram(word1, word2);
//        }
//    }
//
//    private def statisticBigram(word1: String, word2: String) {
//        if (CharCheckUtil.isChinese(word1) && CharCheckUtil.isChinese(word2)) {
//            var wordNode1 = new Node(getWordFirstTwoChars(word1))
//            var wordNode2 = new Node(getWordFirstTwoChars(word2))
//            wordNode1 = nodeRepository.add(wordNode1);
//            wordNode2 = nodeRepository.add(wordNode2);
//            trie.insert(new Array[Int]{wordNode1.getIndex(), wordNode2.getIndex()});
//        }
//    }
//
//    private def getWordFirstTwoChars(word: String) : String = {
//        return word.length() > 2 ? word.substring(word.length() - 2) : word;
//    }
//}
