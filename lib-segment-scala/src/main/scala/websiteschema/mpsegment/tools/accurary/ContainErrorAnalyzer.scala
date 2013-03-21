//package websiteschema.mpsegment.tools.accurary;
//
//import websiteschema.mpsegment.core.WordAtom;
//
//import java.util.Map;
//
//class ContainErrorAnalyzer extends AbstractErrorAnalyzer {
//
//    override def analysis(expect: WordAtom, possibleErrorWord: String) : Boolean = {
//        var foundError = false
//        if (possibleErrorWord.length() == 0) {
//            // Bigger word contains the littler words.
//            // Should remove the word from dictionary.
//            increaseOccur();
////            println(expect.word + " in " + errorSegment);
//            foundError = true;
//        } else if (!possibleErrorWord.replaceAll(" ", "").equals(expect.word)
//                && possibleErrorWord.contains(expect.word)) {
//            increaseOccur();
//            addErrorWord(possibleErrorWord);
////            println(expect.word + " in " + errorSegment);
//            foundError = true;
//        }
//        return foundError;
//    }
//
//    override def postAnalysis(allWordsAndFreqInCorpus: Map[String,Int]) {
//        for(String word : getWords().keySet()) {
//            if(allWordsAndFreqInCorpus.containsKey(word)) {
//                removeErrorWord(word);
//            }
//        }
//    }
//}
