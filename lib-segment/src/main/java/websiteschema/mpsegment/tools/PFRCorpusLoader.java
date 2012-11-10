package websiteschema.mpsegment.tools;

import websiteschema.mpsegment.concept.Concept;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.dict.DictionaryFactory;
import websiteschema.mpsegment.dict.HashDictionary;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.POSUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PFRCorpusLoader {

    private BufferedReader reader;
    HashDictionary dictionary;

    public PFRCorpusLoader(InputStream inputStream) throws UnsupportedEncodingException {
        this(inputStream, "utf-8");
    }

    public PFRCorpusLoader(InputStream inputStream, String encoding) throws UnsupportedEncodingException {
        reader = new BufferedReader(new InputStreamReader(inputStream, encoding));
        dictionary = DictionaryFactory.getInstance().getCoreDictionary();
        if(dictionary == null) {
            DictionaryFactory.getInstance().loadDictionary();
            dictionary = DictionaryFactory.getInstance().getCoreDictionary();
        }
    }

    public SegmentResult readLine() throws IOException {
        String line = reader.readLine();
        if (line == null || "".equals(line.trim()))
            return null;

        return buildSegmentResult(line);
    }

    private SegmentResult buildSegmentResult(String line) {
        String elements[] = line.split("\\s+");

        List<String> words = new ArrayList<String>();
        int[] posArray = new int[elements.length - 1];
        int[] domainTypes = new int[elements.length - 1];
        String[] concepts = new String[elements.length - 1];

        int firstIndex = -1;
        for (int i = 0; i < elements.length - 1; i++) {
            String ele = elements[i + 1];
            domainTypes[i] = 0;
            if (ele.startsWith("[")) {
                firstIndex = i;
                ele = ele.substring(1);
            }
            if (ele.contains("]")) {
                String[] conceptInfo = ele.split("]");
                ele = conceptInfo[0];
                int domainType = POSUtil.getPOSIndex(conceptInfo[1].toUpperCase());
                for (int j = firstIndex; j <= i; j++) {
                    domainTypes[j] = domainType;
                }
                firstIndex = -1;
            }
            String[] info = ele.split("/");
            String wordStr = info[0];
            String posStr = info[1].toUpperCase();
            concepts[i] = getConcept(wordStr, posStr);
            words.add(wordStr);
            posArray[i] = POSUtil.getPOSIndex(posStr);
        }

        SegmentResult result = new SegmentResult(words.size());
        result.setWords(words.toArray(new String[0]));
        result.setPOSArray(posArray);
        result.setDomainTypes(domainTypes);
        result.setConcepts(concepts);
        return result;
    }

    public String getConcept(String wordStr, String posStr) {
        IWord word = dictionary.lookupWord(wordStr);
        if (null != word) {
            for (Concept concept : word.getConcepts()) {
                if (concept.getName().startsWith(posStr.substring(0, 1).toLowerCase())) {
                    return concept.getName();
                } else if(POSUtil.getPOSIndex(posStr) == POSUtil.POS_J && concept.getName().startsWith("n")) {
                    return concept.getName();
                }
            }
        }
        return Concept.UNKNOWN.getName();
    }
}
