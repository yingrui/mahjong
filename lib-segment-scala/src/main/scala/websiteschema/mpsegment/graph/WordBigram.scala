//package websiteschema.mpsegment.graph
//
//import websiteschema.mpsegment.hmm.Node
//import websiteschema.mpsegment.hmm.NodeRepository
//import websiteschema.mpsegment.hmm.Trie
//import websiteschema.mpsegment.util.SerializeHandler
//
//import java.io.*
//import java.util.Map
//import java.util.concurrent.ConcurrentHashMap
//
//class WordBigram {
//
//    private static Map<String,WordBigram> mapWordBigram = new ConcurrentHashMap[String,WordBigram]()
//    private var trie : Trie = new Trie()
//    private var nodeRepository : NodeRepository = new NodeRepository()
//
//    public WordBigram() {
//    }
//
//    public WordBigram(Trie trie, NodeRepository nodeRepository) {
//        this.trie = trie
//        this.nodeRepository = nodeRepository
//    }
//
//    public WordBigram(String resource) {
//        try {
//            load(resource)
//        } catch {
//            ex.printStackTrace()
//        }
//    }
//
//    public static WordBigram getInstance(String corpusName) {
//        return mapWordBigram.containsKey(corpusName) ? mapWordBigram.get(corpusName) : createWordBigram(corpusName)
//    }
//
//    private static WordBigram createWordBigram(String corpusName) {
//        var instance: WordBigram = null
//        synchronized (mapWordBigram) {
//            if(!mapWordBigram.containsKey(corpusName)) {
//                instance = new WordBigram(corpusName)
//                mapWordBigram.put(corpusName, instance)
//            } else {
//                instance = mapWordBigram.get(corpusName)
//            }
//        }
//        return instance
//    }
//
//    def load(resource: String) throws IOException {
//        var inputStream = getInputStream(resource)
//        var reader = new SerializeHandler(new DataInputStream(inputStream))
//        nodeRepository.load(reader)
//        trie.load(reader)
//        trie.buildIndex(1)
//    }
//
//    def getProbability(word1: String, word2: String) : Double = {
//        var firstWord = nodeRepository.get(getWordFirstTwoChars(word1))
//        var secondWord = nodeRepository.get(getWordFirstTwoChars(word2))
//        if(firstWord == null || secondWord == null) {
//            return -1
//        }
//        Array[Int] bigram = new Array[Int]{
//                firstWord.getIndex(),
//                secondWord.getIndex()}
//        var node = trie.searchNode(bigram)
//        return null != node ? node.getProb() : -1
//    }
//
//    private def getInputStream(resource: String) : InputStream = throws FileNotFoundException {
//        var inputStream = null
//        var file = new File(resource)
//        if (file.exists()) {
//            inputStream = new FileInputStream(file)
//        } else {
//            inputStream = getClass().getClassLoader().getResourceAsStream(resource)
//        }
//        return inputStream
//    }
//
//    private def getWordFirstTwoChars(word: String) : String = {
//        return word.length() > 2 ? word.substring(word.length() - 2) : word
//    }
//}
