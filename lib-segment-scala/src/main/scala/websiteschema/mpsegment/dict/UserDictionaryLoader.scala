//package websiteschema.mpsegment.dict
//
//import websiteschema.mpsegment.conf.MPSegmentConfiguration
//import websiteschema.mpsegment.core._
//import websiteschema.mpsegment.dict.domain.DomainDictionary
//import websiteschema.mpsegment.dict.domain.DomainDictFactory
//import java.util.ArrayList
//
//object UserDictionaryLoader {
//  def apply( domaindictionary: DomainDictionary, hashdictionary: HashDictionary) = {
//
//  }
//}
//
//class UserDictionaryLoader {
//
//    public UserDictionaryLoader(DomainDictionary domaindictionary, HashDictionary hashdictionary) {
//        defaultFreq = 50;
//        defaultDomainType = 250;
//        disambiguateRuleArrayList = new ArrayList();
//        this.hashDictionary = hashdictionary;
//        this.domainDict = domaindictionary;
//    }
//
//    private def existedInHashDict(s: String) : Boolean = {
//        Boolean flag = false;
//        if (hashDictionary != null && hashDictionary.lookupWord(s) != null) {
//            flag = true;
//        }
//        return flag;
//    }
//
//    def clear() {
//        disambiguateRuleArrayList.clear();
//    }
//
//    def loadUserDictionary(file: String) throws DictionaryException {
//        String encoding = "utf-8";
//        try {
//            Int i = 0;
//
//            BufferedReader bufferedreader = new BufferedReader(
//                    new InputStreamReader(
//                    UserDictionaryLoader.class.getClassLoader().getResourceAsStream(file), encoding));
//            String line;
//            while ((line = bufferedreader.readLine()) != null) {
//                i++;
//                line = line.trim();
//                if (line.length() >= 1) {
//                    processUserDictLine(line);
//                }
//            }
//            bufferedreader.close();
//            bufferedreader = null;
//        } catch {
//            println((new StringBuilder("[UserDictionary] exception:")).append(exception.getMessage()).toString());
//            exception.printStackTrace();
//        }
//    }
//
//    private def processUserDictLine(s: String) throws DictionaryException {
//        String line = s;
//        Int freq = defaultFreq;
//        String pos = "N";
//        if (line.length() <= 0 || line.startsWith("#") || line.startsWith("~~")) {
//            return;
//        }
//        if (line.indexOf("->") > 0) {
//            addDisambiguateRule(s);
//        } else if (line.indexOf("=") > 0) {
//            Int j = line.indexOf("=");
//            String s4 = line.substring(0, j);
//            String s5 = line.substring(j + 1);
//            Array[String] as1 = s4.split(",");
//            if (as1 != null) {
//                for (Int k = 0; k < as1.length; k++) {
//                    if (!existedInHashDict(as1(k))) {
//                        domainDict.pushWord(as1(k), null, "N", defaultFreq, defaultDomainType);
//                    }
//                }
//
//            }
//        } else {
//            Array[String] as = line.split(" ");
//            String wordName = as(0).trim();
//            if (as.length > 1) {
//                as(1) = as(1).trim().toUpperCase();
//                if (as(1).length() > 0) {
//                    pos = as(1);
//                }
//            }
//            if (as.length > 2) {
//                as(2) = as(2).trim();
//                freq = strToInt(as(2), defaultFreq);
//            }
//            domainDict.pushWord(wordName, null, pos, freq, defaultDomainType);
//        }
//    }
//
//    private def strToInt(intStr: String, def: Int) : Int = {
//        Int j = def;
//        try {
//            j = Int.parseInt(intStr);
//        } catch {
//        }
//        return j;
//    }
//
//    private def addDisambiguateRule(s: String) {
//        disambiguateRuleArrayList.add(s);
//    }
//
//    def buildDisambiguationRule(mpSegment: MPSegment) throws DictionaryException {
//        if (disambiguateRuleArrayList.size() > 0) {
//            for (Int k = 0; k < disambiguateRuleArrayList.size(); k++) {
//                String rule = (String) disambiguateRuleArrayList.get(k);
//                Int i = rule.indexOf("->");
//                String left = rule.substring(0, i);
//                String right = rule.substring(i + 2);
//                Array[String] as = right.split(" ");
//                for (Int i1 = 0; i1 < as.length; i1++) {
//                    String s6 = as(i1).trim();
//                    if (getLog2Freq(s6) < 0) {
//                        domainDict.pushWord(s6, null, "N", defaultFreq, defaultDomainType);
//                    }
//                }
//            }
////            domainDict.updateWordItems();
//            Int minTFIndex = 0;
//            for (Int l2 = 0; l2 < disambiguateRuleArrayList.size(); l2++) {
//                String line = (String) disambiguateRuleArrayList.get(l2);
//                Int j = line.indexOf("->");
//                String left = line.substring(0, j);
//                String right = line.substring(j + 2);
//                Int leftFee = getFee(mpSegment, left);  //
//                Array[String] as1 = right.split(" ");
//                Int rightFee = getFee(as1);
//                for (Int k2 = 0; rightFee > leftFee && k2 < maxDisambiguateIteration; k2++) {
//                    Int minTF = 100000;
//                    for (Int i3 = 0; i3 < as1.length; i3++) {
//                        String word = as1(i3).trim();
//                        Int tf = getTF(word);
//                        if (tf < minTF) {
//                            minTF = tf;
//                            minTFIndex = i3;
//                        }
//                    }
//                    String minTFWord = as1(minTFIndex).trim();
//                    setOccurredCount(minTFWord, minTF + minTF);
//                    rightFee = getFee(as1);
//                }
//                leftFee = getFee(mpSegment, left);
//            }
//
//        }
//    }
//
//    private def getFee(mpsegment: MPSegment, s: String) : Int = {
//        Int i = 0;
//        SegmentResult segmentResult = mpsegment.segmentMP(s, true);
//        for (Int j = 0; j < segmentResult.length(); j++) {
//            i = (i + getLogCorpus()) - getLog2Freq(segmentResult.getWord(j));
//        }
//        return i;
//    }
//
//    private def getFee(as: Array[String]) : Int = {
//        Int i = 0;
//        for (Int j = 0; j < as.length; j++) {
//            i = (i + getLogCorpus()) - getLog2Freq(as(j));
//        }
//        return i;
//    }
//
//    private def getLog2Freq(s: String) : Int = {
//        IWord word = getItem(s);
//        if (word != null) {
//            return word.getLog2Freq();
//        } else {
//            return -1;
//        }
//    }
//
//    private def setOccurredCount(s: String, factor: Int) {
//        IWord word = getItem(s);
//        if (word != null) {
//            word.setOccuredSum(factor);
//        }
//    }
//
//    def loadSynonymMap(s: String) : ArrayList = {
//        File file = new File(s);
//        ArrayList arraylist = new ArrayList();
//        if (file.isFile() && file.exists()) {
//            try {
//                Int i = 0;
//
//                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
//                String s2;
//                while ((s2 = bufferedreader.readLine()) != null) {
//                    i++;
//                    s2 = s2.trim();
//                    if (s2.length() >= 1 && !s2.startsWith("#") && s2.indexOf("=") > 0) {
//                        arraylist.add(s2);
//                    }
//                }
//                bufferedreader.close();
//                bufferedreader = null;
//            } catch {
//                println((new StringBuilder("[UserDictionary] exception:")).append(exception.getMessage()).toString());
//                exception.printStackTrace();
//            }
//        } else {
//            println((new StringBuilder("[UserDictionary] ")).append(s).append(" 不存在！").toString());
//        }
//        return arraylist;
//    }
//
//    def loadStopWord(s: String) : ArrayList = {
//        File file = new File(s);
//        ArrayList arrayList = new ArrayList();
//        if (file.isFile() && file.exists()) {
//            try {
//                Int i = 0;
//                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
//                String s2;
//                while ((s2 = bufferedreader.readLine()) != null) {
//                    i++;
//                    s2 = s2.trim();
//                    if (s2.length() >= 1 && s2.startsWith("~~")) {
//                        arrayList.add(s2);
//                    }
//                }
//                bufferedreader.close();
//                bufferedreader = null;
//            } catch {
//                println((new StringBuilder("[UserDictionary] exception:")).append(exception.getMessage()).toString());
//                exception.printStackTrace();
//            }
//        } else {
//            println((new StringBuilder("[UserDictionary] ")).append(s).append(" 不存在！").toString());
//        }
//        return arrayList;
//    }
//
//    private def getItem(wordStr: String) : IWord = {
//        IWord word = null;
//        if (wordStr.length() > 1) {
//            word = domainDict.getWord(wordStr);
//        }
//        if (word == null) {
//            word = hashDictionary.getWord(wordStr);
//        }
//        return word;
//    }
//
//    private def getTF(wordStr: String) : Int = {
//        IWord iworditem = getItem(wordStr);
//        Int tf = 0;
//        if (iworditem != null) {
//            tf = (Int) iworditem.getOccuredSum();
//        }
//        return tf;
//    }
//
//    private def getLogCorpus() : Int = {
//        return (Int) MPSegmentConfiguration.LOG_CORPUS;
//    }
//
//    private var encoding : String = MPSegmentConfiguration.getInstance().getDefaultFileEncoding();
//    private var domainDict : DomainDictionary = DomainDictFactory.getInstance().getDomainDictionary();
//    private var hashDictionary : HashDictionary = null
//    private var defaultFreq : Int = null
//    private var defaultDomainType : Int = null
//    private var disambiguateRuleArrayList : ArrayList = null
//    private var maxDisambiguateIteration : Int = 50;
//
//}
