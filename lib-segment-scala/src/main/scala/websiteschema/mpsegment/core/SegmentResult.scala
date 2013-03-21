//package websiteschema.mpsegment.core;
//
//import websiteschema.mpsegment.dict.POSUtil;
//import websiteschema.mpsegment.util.CharCheckUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
//class SegmentResult {
//
//    public SegmentResult(Int size) {
//        initWordElements(size);
//    }
//
//    private def initWordElements(size: Int) {
//        wordAtoms = new ArrayList[WordAtom](size);
//        for (Int i = 0; i < size; i++) {
//            wordAtoms.add(new WordAtom());
//        }
//    }
//
//    def getWordAtoms() : List[WordAtom] = {
//        return wordAtoms;
//    }
//
//    def setWords(words: Array[String]) {
//        for (Int i = 0; i < words.length; i++) {
//            wordAtoms.get(i).word = words(i);
//        }
//    }
//
//    def setPOSArray(tags: Array[Int]) {
//        for (Int i = 0; i < tags.length; i++) {
//            wordAtoms.get(i).pos = tags(i);
//        }
//    }
//
//    def setDomainTypes(marks: Array[Int]) {
//        for (Int i = 0; i < marks.length; i++) {
//            wordAtoms.get(i).domainType = marks(i);
//        }
//    }
//
//    def setConcepts(concepts: Array[String]) {
//        for (Int i = 0; i < concepts.length; i++) {
//            wordAtoms.get(i).concept = concepts(i);
//        }
//    }
//
//    def setPinyin(pinyin: Array[String]) {
//        for (Int i = 0; i < pinyin.length; i++) {
//            wordAtoms.get(i).pinyin = pinyin(i);
//        }
//    }
//
//    def getWord(i: Int) : String = {
//        return wordAtoms.get(i).word;
//    }
//
//    def getWordIndexInOriginalString(index: Int) : Int = {
//        Int wordIndexInOriginalString = 0;
//        for (Int i = 0; i < index; i++) {
//            wordIndexInOriginalString += wordAtoms.get(i).word.length();
//        }
//        return wordIndexInOriginalString;
//    }
//
//    def getPinyin(i: Int) : String = {
//        return wordAtoms.get(i).pinyin;
//    }
//
//    def getPOS(i: Int) : Int = {
//        return wordAtoms.get(i).pos;
//    }
//
//    def getConcept(i: Int) : String = {
//        return wordAtoms.get(i).concept;
//    }
//
//    def getDomainType(i: Int) : Int = {
//        return wordAtoms.get(i).domainType;
//    }
//
//    def length() : Int = {
//        if (wordAtoms != null) {
//            return wordAtoms.size();
//        } else {
//            return 0;
//        }
//    }
//
//    def getWordAtom(i: Int) : WordAtom = {
//        return wordAtoms.get(i);
//    }
//
//    def setWord(index: Int, word: String) {
//        wordAtoms.get(index).word = word;
//    }
//
//    def setPOS(index: Int, pos: Int) {
//        wordAtoms.get(index).pos = pos;
//    }
//
//    def setConcept(index: Int, concept: String) {
//        wordAtoms.get(index).concept = concept;
//    }
//
//    def setPinyin(index: Int, pinyin: String) {
//        wordAtoms.get(index).pinyin = pinyin;
//    }
//
//    def append(segmentResult: SegmentResult) {
//        this.wordAtoms.addAll(segmentResult.wordAtoms);
//    }
//
//    def merge(start: Int, end: Int, pos: Int) {
//        WordAtom wordAtom = mergedWordAtom(start, end, pos);
//        if (null != wordAtom) {
//            for (Int i = start + 1; i <= end; i++) {
//                markWordToBeDeleted(i);
//            }
//        }
//    }
//
//    def markWordToBeDeleted(i: Int) {
//        wordAtoms.set(i, null);
//    }
//
//    def compact() {
//        Int i = 0;
//        while (i < length()) {
//            if (wordAtoms.get(i) == null) {
//                wordAtoms.remove(i);
//            } else {
//                i++;
//            }
//        }
//    }
//
//    def toOriginalString() : String = {
//        StringBuilder stringBuilder = new StringBuilder();
//        for (Int i = 0; i < length(); i++) {
//            stringBuilder.append(getWord(i));
//        }
//        return stringBuilder.toString();
//    }
//
//    override def toString() : String = {
//        StringBuilder retString = new StringBuilder();
//        for (Int j = 0; j < length(); j++) {
//            retString.append(getWord(j)).append("/").append(POSUtil.getPOSString(getPOS(j))).append(" ");
//        }
//
//        return retString.toString();
//    }
//
//    private def mergedWordAtom(start: Int, end: Int, pos: Int) : WordAtom = {
//        StringBuilder wordName = new StringBuilder();
//        StringBuilder pinyin = new StringBuilder();
//        WordAtom startWordAtom = getStartWordAtom(start);
//        appendWordAndPinyin(wordName, pinyin, startWordAtom);
//        for (Int i = start + 1; i <= end; i++) {
//            WordAtom wordAtom = wordAtoms.get(i);
//            if (wordAtom != null) {
//                appendWordAndPinyin(wordName, pinyin, wordAtom);
//            }
//        }
//        startWordAtom.word = wordName.toString();
//        startWordAtom.pinyin = pinyin.toString();
//        startWordAtom.pos = pos;
//        return startWordAtom;
//    }
//
//    private def appendWordAndPinyin(wordName: StringBuilder, pinyin: StringBuilder, startWordAtom: WordAtom) {
//        wordName.append(startWordAtom.word);
//        appendPinyin(pinyin, startWordAtom);
//    }
//
//    private def appendPinyin(pinyin: StringBuilder, startWordAtom: WordAtom) {
//        if (null != startWordAtom.pinyin) {
//            if (pinyin.length() > 0 && CharCheckUtil.isChinese(startWordAtom.word)) {
//                pinyin.append("'");
//            }
//            pinyin.append(startWordAtom.pinyin);
//        }
//    }
//
//    private def getStartWordAtom(index: Int) : WordAtom = {
//        WordAtom wordAtom = getWordAtom(index);
//        if (null != wordAtom) {
//            return wordAtom;
//        } else {
//            return lookupWordAtomBefore(index);
//        }
//    }
//
//    private def lookupWordAtomBefore(index: Int) : WordAtom = {
//        for (Int i = index - 1; i >= 0; i--) {
//            WordAtom wordAtom = getWordAtom(i);
//            if (null != wordAtom) {
//                return wordAtom;
//            }
//        }
//        return null;
//    }
//
//    private var wordAtoms : List[WordAtom] = null
//}
