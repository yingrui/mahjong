//package websiteschema.mpsegment.core;
//
//import websiteschema.mpsegment.dict.DictionaryLookupResult;
//import websiteschema.mpsegment.dict.DictionaryService;
//import websiteschema.mpsegment.dict.IWord;
//import websiteschema.mpsegment.util.StringUtil;
//
//public abstract class AbstractWordScanner {
//    private var sentence : String = null
//    private var maxWordLength : Int = null
//    private var dictionaryService : DictionaryService = null
//
//    def getDictionaryService() : DictionaryService = {
//        return dictionaryService;
//    }
//
//    def startScanningAt(startPos: Int) {
//        var lastMinWordLen = 0
//        var length = sentence.length()
//        try {
//            for (Int begin = startPos; begin < length; begin += 1) {
//                var minWordLen = scanTheMinWordLength(begin)
//
//                //find single Char word or multi-chars alpha-numeric word
//                var atomWord = sentence.substring(begin, begin + minWordLen)
//                var singleCharWord = foundAtomWord(atomWord)
//
//                //find all possible slices except single word
//                var candidateWord = getCandidateSentence(begin, lastMinWordLen)
//                var result = dictionaryService.lookup(candidateWord)
//                processFoundWordItems(begin, singleCharWord, result);
//            }
//        } catch {
//            println(ex);
//        } finally {
//        }
//    }
//
//    abstract def foundAtomWord(atomWord: String) : IWord
//    abstract def processFoundWordItems(begin: Int, singleCharWord: IWord, lookupResult: DictionaryLookupResult) 
//
//    def setSentence(sentence: String) {
//        this.sentence = sentence;
//    }
//
//    def setMaxWordLength(maxWordLength: Int) {
//        this.maxWordLength = maxWordLength;
//    }
//
//    def setDictionaryService(dictionaryService: DictionaryService) {
//        this.dictionaryService = dictionaryService;
//    }
//
//    private def scanTheMinWordLength(begin: Int) : Int = {
//        var index = scanEnglishWordAndShorten(begin)
//        var minWordLen = (index - begin) + 1
//        return minWordLen;
//    }
//
//    private def scanEnglishWordAndShorten(begin: Int) : Int = {
//        var sentenceLength = sentence.length()
//        var index = begin
//        if (StringUtil.isCharAlphabeticalOrDigital(sentence.charAt(index))) {
//            while (index < sentenceLength && StringUtil.isCharAlphabeticalOrDigital(sentence.charAt(index))) {
//                index++;
//            }
//            index--;
//        }
//        return index;
//    }
//
//    private def getCandidateSentence(begin: Int, lastMinWordLen1: Int) : String = {
//        var candidateWord = ""
//        var length = sentence.length()
//        var rest = length - begin
//        if (maxWordLength <= rest) {
//            var end = (begin + maxWordLength + lastMinWordLen1) - 1
//            end = end < length
//                    ? end : length;
//            candidateWord = sentence.substring(begin, end);
//        } else {
//            candidateWord = sentence.substring(begin);
//        }
//        return candidateWord;
//    }
//}
