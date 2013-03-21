//package websiteschema.mpsegment.tools;
//
//import websiteschema.mpsegment.concept.Concept;
//import websiteschema.mpsegment.core.SegmentResult;
//import websiteschema.mpsegment.dict.DictionaryFactory;
//import websiteschema.mpsegment.dict.HashDictionary;
//import websiteschema.mpsegment.dict.IWord;
//import websiteschema.mpsegment.dict.POSUtil;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//class PFRCorpusLoader {
//
//    private var reader : BufferedReader = null
//    private var dictionary : HashDictionary = null
//    private var eliminatedDomainTypes : Set[Int] = null
//
//    public PFRCorpusLoader(InputStream inputStream) throws UnsupportedEncodingException {
//        this(inputStream, "utf-8");
//    }
//
//    public PFRCorpusLoader(InputStream inputStream, String encoding) throws UnsupportedEncodingException {
//        reader = new BufferedReader(new InputStreamReader(inputStream, encoding));
//        dictionary = DictionaryFactory.getInstance().getCoreDictionary();
//        if (dictionary == null) {
//            DictionaryFactory.getInstance().loadDictionary();
//            dictionary = DictionaryFactory.getInstance().getCoreDictionary();
//        }
//        eliminatedDomainTypes = new HashSet[Int](5);
//    }
//
//    def eliminateDomainType(domainType: Int) {
//        eliminatedDomainTypes.add(domainType);
//    }
//
//    def readLine() : SegmentResult = throws IOException {
//        var line = reader.readLine()
//        if (line == null || "".equals(line.trim()))
//            return null;
//
//        return buildSegmentResult(line);
//    }
//
//    private def buildSegmentResult(line: String) : SegmentResult = {
//        var elements = line.split("\\s+")
//
//        var words = new ArrayList[String]()
//        var posArray = new Int[elements.length - 1]
//        var domainTypes = new Int[elements.length - 1]
//        var concepts = new String[elements.length - 1]
//
//        var firstIndex = -1
//        for (Int i = 0; i < elements.length - 1; i++) {
//            var ele = elements[i + 1]
//            domainTypes(i) = 0;
//            if (ele.startsWith("[")) {
//                firstIndex = i;
//                ele = ele.substring(1);
//            }
//            if (ele.contains("]")) {
//                var conceptInfo = ele.split("]")
//                ele = conceptInfo(0);
//                var domainType = POSUtil.getPOSIndex(conceptInfo(1).toUpperCase())
//                setDomainType(domainTypes, firstIndex, i, domainType);
//                firstIndex = -1;
//            }
//            var info = ele.split("/")
//            var wordStr = info(0)
//            var posStr = info(1).toUpperCase()
//            if (posStr.equalsIgnoreCase(POSUtil.getPOSString(POSUtil.POS_NR))) {
//                setDomainType(domainTypes, i, POSUtil.POS_NR);
//            }
//            concepts(i) = getConcept(wordStr, posStr);
//            words.add(wordStr);
//            posArray(i) = POSUtil.getPOSIndex(posStr);
//        }
//
//        var result = new SegmentResult(words.size())
//        result.setWords(words.toArray(new String[0]));
//        result.setPOSArray(posArray);
//        result.setDomainTypes(domainTypes);
//        result.setConcepts(concepts);
//        return result;
//    }
//
//    private def setDomainType(domainTypes: Array[Int], index: Int, domainType: Int) {
//        setDomainType(domainTypes, index, index, domainType);
//    }
//
//    private def setDomainType(domainTypes: Array[Int], startIndex: Int, endIndex: Int, domainType: Int) {
//        if (!eliminatedDomainTypes.contains(domainType)) {
//            for (Int j = startIndex; j <= endIndex; j++) {
//                domainTypes(j) = domainType;
//            }
//        }
//    }
//
//    def getConcept(wordStr: String, posStr: String) : String = {
//        var word = dictionary.lookupWord(wordStr)
//        if (null != word) {
//            for (concept <- word.getConcepts()) {
//                if (concept.getName().startsWith(posStr.substring(0, 1).toLowerCase())) {
//                    return concept.getName();
//                } else if (POSUtil.getPOSIndex(posStr) == POSUtil.POS_J && concept.getName().startsWith("n")) {
//                    return concept.getName();
//                }
//            }
//        }
//        return Concept.UNKNOWN.getName();
//    }
//}
