package websiteschema.mpsegment.dict;

import org.apache.log4j.Logger;
import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.core.MPSegment;
import websiteschema.mpsegment.dict.domain.DomainDictFactory;
import websiteschema.mpsegment.dict.domain.DomainDictionary;
import websiteschema.mpsegment.tools.StringWordConverter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DictionaryFactory {

    private final static Logger l = Logger.getLogger("segment");
    private final static DictionaryFactory ins = new DictionaryFactory();

    public static DictionaryFactory getInstance() {
        return ins;
    }

    private HashDictionary coreDict;
    private DomainDictFactory domainFactory;
    private boolean loadDomainDictionary = MPSegmentConfiguration.getINSTANCE().isLoadDomainDictionary();
    private boolean loadUserDictionary = MPSegmentConfiguration.getINSTANCE().isLoadUserDictionary();

    public HashDictionary getCoreDictionary() {
        return coreDict;
    }

    public void loadDictionary() {
        try {
            ArrayList<String> list = loadWordStr("websiteschema/mpsegment/dict.txt");

            String[] array = list.toArray(new String[0]);
            Arrays.sort(array);

            StringWordConverter converter = new StringWordConverter();
            for (String wordStr : array) {
                IWord word = converter.convert(wordStr);
                if (word != null) {
                    coreDict.addWord(word);
                }
            }
            array = null;
        } catch (IOException e) {
            l.error(e);
        } catch (DictionaryException e) {
            l.error(e);
        }
    }

    private ArrayList<String> loadWordStr(String dictResource) throws IOException {
        InputStream resourceStream = null;
        File file = new File(dictResource);
        if (file.exists()) {
            resourceStream = new FileInputStream(file);
        } else {
            resourceStream = DictionaryFactory.class.getClassLoader().getResourceAsStream(dictResource);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream, "utf-8"));

        coreDict = new HashDictionary();
        String line = reader.readLine();
        ArrayList<String> listWordStr = new ArrayList<String>();
        while (line != null) {
            listWordStr.add(line.trim());
            line = reader.readLine();
        }
        reader.close();
        reader = null;
        return listWordStr;
    }

    public void loadDomainDictionary() {
        if (loadDomainDictionary || loadUserDictionary) {
            domainFactory = DomainDictFactory.getInstance();
            domainFactory.buildDictionary();
        }
    }

    public void loadUserDictionary() {
        if (loadUserDictionary) {
            long l1 = System.currentTimeMillis();
            String userDictFile = MPSegmentConfiguration.getINSTANCE().getUserDictionaryFile();
            DomainDictionary domainDictionary = DomainDictFactory.getInstance().getDomainDictionary();
            UserDictionaryLoader userDictionaryLoader = new UserDictionaryLoader(domainDictionary, coreDict);
            try {
                userDictionaryLoader.loadUserDictionary(userDictFile);
                userDictionaryLoader.buildDisambiguationRule(new MPSegment());
            } catch (DictionaryException e) {
                l.error(e);
            }
            userDictionaryLoader.clear();
            l1 = System.currentTimeMillis() - l1;
            l.debug((new StringBuilder()).append("[Segment] ").append("loading user dictionary time used(ms): ").append(l1).toString());
        }
    }
}
