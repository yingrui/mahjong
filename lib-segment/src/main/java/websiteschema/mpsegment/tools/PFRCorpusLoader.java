package websiteschema.mpsegment.tools;

import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.dict.POSUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PFRCorpusLoader {

    private BufferedReader reader;

    public PFRCorpusLoader(InputStream inputStream) throws UnsupportedEncodingException {
        this(inputStream, "utf-8");
    }

    public PFRCorpusLoader(InputStream inputStream, String encoding) throws UnsupportedEncodingException {
        reader = new BufferedReader(new InputStreamReader(inputStream, encoding));
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
        int[] concepts = new int[elements.length - 1];

        int firstIndex = -1;
        for(int i = 0; i < elements.length - 1; i++) {
            String ele = elements[i + 1];
            concepts[i] = 0;
            if(ele.startsWith("[")) {
                firstIndex = i;
                ele = ele.substring(1);
            }
            if(ele.contains("]")) {
                String[] conceptInfo = ele.split("]");
                ele = conceptInfo[0];
                int concept = POSUtil.getPOSIndex(conceptInfo[1].toUpperCase());
                for(int j = firstIndex; j <= i; j++) {
                    concepts[j] = concept;
                }
                firstIndex = -1;

            }
            String[] info = ele.split("/");
            words.add(info[0]);
            posArray[i] = POSUtil.getPOSIndex(info[1].toUpperCase());
        }

        SegmentResult result = new SegmentResult(words.size());
        result.setWords(words.toArray(new String[0]));
        result.setPOSArray(posArray);
        result.setConcepts(concepts);
        return result;
    }


}
