//package websiteschema.mpsegment.tools;
//
//import websiteschema.mpsegment.concept.Concept;
//import websiteschema.mpsegment.concept.ConceptRepository;
//import websiteschema.mpsegment.concept.ConceptTree;
//import websiteschema.mpsegment.dict.*;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//class ConceptLoader {
//
//    public static void main(Array[String] args) throws IOException, DictionaryException {
//        DictionaryFactory.getInstance().loadDictionary();
//        DictionaryFactory.getInstance().loadDomainDictionary();
//        DictionaryFactory.getInstance().loadUserDictionary();
//
//        Array[String] fileNames = new Array[String]{
//                "/Users/twer/workspace/nlp/ccd/noun-list.txt",
//                "/Users/twer/workspace/nlp/ccd/verb-list.txt",
//                "/Users/twer/workspace/nlp/ccd/adj-list.txt"};
//        var file1 = new File(fileNames(0))
//        load(file1, ConceptRepository.getInstance().getNounConceptTree(), "N");
//
//        var file2 = new File(fileNames(1))
//        load(file2, ConceptRepository.getInstance().getVerbConceptTree(), "V");
//
//        var file3 = new File(fileNames(2))
//        load(file3, ConceptRepository.getInstance().getAdjConceptTree(), "A");
//
//        var writer = new DictionaryWriter(new File("dict.txt"))
//        writer.write(DictionaryFactory.getInstance().getCoreDictionary());
//        writer.close();
//    }
//
//    private static void load(File file, ConceptTree nounConceptTree, String pos) throws IOException, DictionaryException {
//        var reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"))
//        var line = reader.readLine()
//        while (null != line) {
//            var array = line.split(" ")
//            if (null != array && array.length == 2) {
//                var wordName = array(0)
//                var concepts = array(1).split("\\|")
//                var word = (WordImpl) DictionaryFactory.getInstance().getCoreDictionary().getWord(wordName)
//                if (word == null) {
//                    println("missing word: " + wordName + " " + array(1));
//                    word = new WordImpl(wordName);
//                    var posArray = new POSArray()
//                    posArray.add(new POS(pos, 50));
//                    word.setPosArray(posArray);
//                    DictionaryFactory.getInstance().getCoreDictionary().addWord(word);
//                }
//                if (word != null) {
//                    var conceptList = new ArrayList[Concept]()
//                    for (conceptStr <- concepts) {
//                        if (!conceptStr.equals("null")) {
//                            var concept = nounConceptTree.getConceptByDescription(conceptStr)
//                            if (null != concept) {
//                                conceptList.add(concept);
//                            }
//                        }
//                    }
//                    word.setConcepts(conceptList.toArray(new Concept[0]));
//                }
//            }
//            line = reader.readLine();
//        }
//        reader.close();
//    }
//}
