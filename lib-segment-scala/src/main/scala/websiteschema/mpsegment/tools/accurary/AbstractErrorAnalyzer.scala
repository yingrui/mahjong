//package websiteschema.mpsegment.tools.accurary;
//
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
//abstract class AbstractErrorAnalyzer extends ErrorAnalyzer {
//
//    private var occurTimes : Int = 0;
//    private Map[String,Int] wordsWithError = new ConcurrentHashMap[String,Int]();
//
//    override def getErrorOccurTimes() : Int = {
//        return occurTimes;
//    }
//
//    override public Map<String,Int> getWords() {
//        return sort();
//    }
//
//    override def postAnalysis(allWordsAndFreqInCorpus: Map[String,Int]) {
//    }
//
//    def increaseOccur() {
//        occurTimes++;
//    }
//
//    def addErrorWord(word: String) {
//        var occurs = wordsWithError.containsKey(word) ? wordsWithError.get(word) + 1 : 1
//        wordsWithError.put(word, occurs);
//    }
//
//    def removeErrorWord(word: String) {
//        wordsWithError.remove(word);
//    }
//
//    private def sort() : Map<String,Int> = {
//        List<Map.Entry<String,Int>> entries = new ArrayList<Map.Entry<String,Int>>(wordsWithError.entrySet());
//        Collections.sort(entries, new Comparator<Map.Entry<String,Int>>() {
//            override def compare(Map.Entry<String,Int> o1, Map.Entry<String,Int> o2) : Int = {
//                return o2.getValue().compareTo(o1.getValue());
//            }
//        });
//        var sortedMap = new LinkedHashMap[String,Int](entries.size())
//        for(Map.Entry[String,Int] mapEntry : entries) {
//            sortedMap.put(mapEntry.getKey(), mapEntry.getValue());
//        }
//        return sortedMap;
//    }
//}
