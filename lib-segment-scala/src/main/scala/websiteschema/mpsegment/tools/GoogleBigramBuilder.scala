//package websiteschema.mpsegment.tools
//
//import websiteschema.mpsegment.hmm.Node
//import websiteschema.mpsegment.hmm.NodeRepository
//import websiteschema.mpsegment.hmm.Trie
//import websiteschema.mpsegment.util.CharCheckUtil
//import websiteschema.mpsegment.util.SerializeHandler
//
//import java.io.*
//import java.util.zip.GZIPInputStream
//
//class GoogleBigramBuilder {
//
//    private static Trie trie = new Trie()
//    private static NodeRepository nodeRepository = new NodeRepository()
//
//    public static void main(Array[String] args) throws IOException {
//        var directory = args.length > 0 ? args(0) : "/Users/twer/workspace/nlp/google-ngram/2gram"
//        var dir = new File(directory)
//        if(dir.isDirectory()) {
//            scanDirectory(dir)
//            save("google-bigram.dat")
//        } else {
//            System.exit(1)
//        }
//    }
//
//    private static void scanDirectory(File dir) throws IOException {
//        var files = dir.listFiles()
//        for(File file : files) {
//            println(file.getName())
//            if(file.getName().startsWith("googlebooks-chi-sim-all-2gram-")) {
//                loadFile(file)
//            }
//        }
//    }
//
//    private static void loadFile(File file) throws IOException {
//        var reader = getBufferedReader(file)
//        var line = reader.readLine()
//        while(line != null) {
//            parseLine(line)
//            line = reader.readLine()
//        }
//    }
//
//    private static void parseLine(String line) {
//        var elements = line.split("\\s+")
//        if(elements.length == 5) {
//            var word1 = elements(0)
//            var word2 = elements(1)
//            var year = Int.parseInt(elements(2))
//            var count = Int.parseInt(elements(3))
//            var documentCount = Int.parseInt(elements(4))
//            statisticBigram(word1, word2, count)
//        }
//    }
//
//    private static void statisticBigram(String word1, String word2, Int count) {
//        if (CharCheckUtil.isChinese(word1) && CharCheckUtil.isChinese(word2)) {
//            var wordNode1 = new Node(getWordFirstTwoChars(word1))
//            var wordNode2 = new Node(getWordFirstTwoChars(word2))
//            wordNode1 = nodeRepository.add(wordNode1)
//            wordNode2 = nodeRepository.add(wordNode2)
//            trie.insert(new Array[Int]{wordNode1.getIndex(), wordNode2.getIndex()}, count)
//        }
//    }
//
//    private static String getWordFirstTwoChars(String word) {
//        return word.length() > 2 ? word.substring(word.length() - 2) : word
//    }
//
//    private static BufferedReader getBufferedReader(File file) throws IOException {
//        var inputStream = getInputStream(file)
//        return new BufferedReader(new InputStreamReader(inputStream, "utf-8"))
//    }
//
//    private static InputStream getInputStream(File file) throws IOException {
//        if(file.getName().endsWith(".gz")) {
//            return new GZIPInputStream(new FileInputStream(file))
//        }
//        return new FileInputStream(file);  //To change body of created methods use File | Settings | File Templates.
//    }
//
//    public static void save(String filename) throws IOException {
//        var writer = new SerializeHandler(new File(filename), SerializeHandler.MODE_WRITE_ONLY)
//        nodeRepository.save(writer)
//        trie.save(writer)
//    }
//}
