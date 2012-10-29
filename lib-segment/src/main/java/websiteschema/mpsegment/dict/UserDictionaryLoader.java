package websiteschema.mpsegment.dict;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.core.*;
import websiteschema.mpsegment.dict.domain.DomainDictionary;
import websiteschema.mpsegment.dict.domain.DomainDictFactory;
import java.io.*;
import java.util.ArrayList;

public class UserDictionaryLoader {

    public UserDictionaryLoader(DomainDictionary domaindictionary, HashDictionary hashdictionary) {
        defaultFreq = 50;
        defaultDomainType = 250;
        disambiguateRuleArrayList = new ArrayList();
        this.hashDictionary = hashdictionary;
        this.domainDict = domaindictionary;
    }

    private boolean existedInHashDict(String s) {
        boolean flag = false;
        if (hashDictionary != null && hashDictionary.lookupWord(s) != null) {
            flag = true;
        }
        return flag;
    }

    public void clear() {
        disambiguateRuleArrayList.clear();
    }

    public void loadUserDictionary(String file) throws DictionaryException {
        String encoding = "utf-8";
        try {
            int i = 0;

            BufferedReader bufferedreader = new BufferedReader(
                    new InputStreamReader(
                    UserDictionaryLoader.class.getClassLoader().getResourceAsStream(file), encoding));
            String line;
            while ((line = bufferedreader.readLine()) != null) {
                i++;
                line = line.trim();
                if (line.length() >= 1) {
                    processUserDictLine(line);
                }
            }
            bufferedreader.close();
            bufferedreader = null;
        } catch (Exception exception) {
            System.out.println((new StringBuilder("[UserDictionary] exception:")).append(exception.getMessage()).toString());
            exception.printStackTrace();
        }
    }

    private void processUserDictLine(String s) throws DictionaryException {
        String line = s;
        int freq = defaultFreq;
        String pos = "N";
        if (line.length() <= 0 || line.startsWith("#") || line.startsWith("~~")) {
            return;
        }
        if (line.indexOf("->") > 0) {
            addDisambiguateRule(s);
        } else if (line.indexOf("=") > 0) {
            int j = line.indexOf("=");
            String s4 = line.substring(0, j);
            String s5 = line.substring(j + 1);
            String as1[] = s4.split(",");
            if (as1 != null) {
                for (int k = 0; k < as1.length; k++) {
                    if (!existedInHashDict(as1[k])) {
                        domainDict.pushWord(as1[k], null, "N", defaultFreq, defaultDomainType);
                    }
                }

            }
        } else {
            String as[] = line.split(" ");
            String wordName = as[0].trim();
            if (as.length > 1) {
                as[1] = as[1].trim().toUpperCase();
                if (as[1].length() > 0) {
                    pos = as[1];
                }
            }
            if (as.length > 2) {
                as[2] = as[2].trim();
                freq = strToInt(as[2], defaultFreq);
            }
            domainDict.pushWord(wordName, null, pos, freq, defaultDomainType);
        }
    }

    private int strToInt(String intStr, int def) {
        int j = def;
        try {
            j = Integer.parseInt(intStr);
        } catch (Exception exception) {
        }
        return j;
    }

    private void addDisambiguateRule(String s) {
        disambiguateRuleArrayList.add(s);
    }

    public void buildDisambiguationRule(MPSegment mpSegment) throws DictionaryException {
        if (disambiguateRuleArrayList.size() > 0) {
            for (int k = 0; k < disambiguateRuleArrayList.size(); k++) {
                String rule = (String) disambiguateRuleArrayList.get(k);
                int i = rule.indexOf("->");
                String left = rule.substring(0, i);
                String right = rule.substring(i + 2);
                String as[] = right.split(" ");
                for (int i1 = 0; i1 < as.length; i1++) {
                    String s6 = as[i1].trim();
                    if (getLog2Freq(s6) < 0) {
                        domainDict.pushWord(s6, null, "N", defaultFreq, defaultDomainType);
                    }
                }
            }
//            domainDict.updateWordItems();
            int minTFIndex = 0;
            for (int l2 = 0; l2 < disambiguateRuleArrayList.size(); l2++) {
                String line = (String) disambiguateRuleArrayList.get(l2);
                int j = line.indexOf("->");
                String left = line.substring(0, j);
                String right = line.substring(j + 2);
                int leftFee = getFee(mpSegment, left);  //
                String as1[] = right.split(" ");
                int rightFee = getFee(as1);
                for (int k2 = 0; rightFee > leftFee && k2 < maxDisambiguateIteration; k2++) {
                    int minTF = 100000;
                    for (int i3 = 0; i3 < as1.length; i3++) {
                        String word = as1[i3].trim();
                        int tf = getTF(word);
                        if (tf < minTF) {
                            minTF = tf;
                            minTFIndex = i3;
                        }
                    }
                    String minTFWord = as1[minTFIndex].trim();
                    setOccurredCount(minTFWord, minTF + minTF);
                    rightFee = getFee(as1);
                }
                leftFee = getFee(mpSegment, left);
            }

        }
    }

    private int getFee(MPSegment mpsegment, String s) {
        int i = 0;
        SegmentResult segmentResult = mpsegment.segmentMP(s, true);
        for (int j = 0; j < segmentResult.length(); j++) {
            i = (i + getLogCorpus()) - getLog2Freq(segmentResult.getWord(j));
        }
        return i;
    }

    private int getFee(String as[]) {
        int i = 0;
        for (int j = 0; j < as.length; j++) {
            i = (i + getLogCorpus()) - getLog2Freq(as[j]);
        }
        return i;
    }

    private int getLog2Freq(String s) {
        IWord word = getItem(s);
        if (word != null) {
            return word.getLog2Freq();
        } else {
            return -1;
        }
    }

    private void setOccurredCount(String s, int factor) {
        IWord word = getItem(s);
        if (word != null) {
            word.setOccuredSum(factor);
        }
    }

    public ArrayList loadSynonymMap(String s) {
        File file = new File(s);
        String s1 = MPSegmentConfiguration.getINSTANCE().getDefaultFileEncoding();
        ArrayList arraylist = new ArrayList();
        if (file.isFile() && file.exists()) {
            try {
                int i = 0;

                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file), s1));
                String s2;
                while ((s2 = bufferedreader.readLine()) != null) {
                    i++;
                    s2 = s2.trim();
                    if (s2.length() >= 1 && !s2.startsWith("#") && s2.indexOf("=") > 0) {
                        arraylist.add(s2);
                    }
                }
                bufferedreader.close();
                bufferedreader = null;
            } catch (Exception exception) {
                System.out.println((new StringBuilder("[UserDictionary] exception:")).append(exception.getMessage()).toString());
                exception.printStackTrace();
            }
        } else {
            System.out.println((new StringBuilder("[UserDictionary] ")).append(s).append(" 不存在！").toString());
        }
        return arraylist;
    }

    public ArrayList loadStopWord(String s) {
        File file = new File(s);
        String s1 = MPSegmentConfiguration.getINSTANCE().getDefaultFileEncoding();
        ArrayList arrayList = new ArrayList();
        if (file.isFile() && file.exists()) {
            try {
                int i = 0;
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file), s1));
                String s2;
                while ((s2 = bufferedreader.readLine()) != null) {
                    i++;
                    s2 = s2.trim();
                    if (s2.length() >= 1 && s2.startsWith("~~")) {
                        arrayList.add(s2);
                    }
                }
                bufferedreader.close();
                bufferedreader = null;
            } catch (Exception exception) {
                System.out.println((new StringBuilder("[UserDictionary] exception:")).append(exception.getMessage()).toString());
                exception.printStackTrace();
            }
        } else {
            System.out.println((new StringBuilder("[UserDictionary] ")).append(s).append(" 不存在！").toString());
        }
        return arrayList;
    }

    private IWord getItem(String wordStr) {
        IWord word = null;
        if (wordStr.length() > 1) {
            word = domainDict.getWord(wordStr);
        }
        if (word == null) {
            word = hashDictionary.getWord(wordStr);
        }
        return word;
    }

    private int getTF(String wordStr) {
        IWord iworditem = getItem(wordStr);
        int tf = 0;
        if (iworditem != null) {
            tf = (int) iworditem.getOccuredSum();
        }
        return tf;
    }

    private int getLogCorpus() {
        return (int) MPSegmentConfiguration.LOG_CORPUS;
    }

    private DomainDictionary domainDict = DomainDictFactory.getInstance().getDomainDictionary();
    private HashDictionary hashDictionary;
    private int defaultFreq;
    private int defaultDomainType;
    private ArrayList disambiguateRuleArrayList;
    private int maxDisambiguateIteration = 50;

}
