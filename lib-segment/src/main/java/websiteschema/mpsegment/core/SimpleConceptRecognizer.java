package websiteschema.mpsegment.core;

import websiteschema.mpsegment.concept.Concept;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.POSUtil;

public class SimpleConceptRecognizer implements IConceptRecognizer {
    private IWord[] words;
    private int[] posArray;

    @Override
    public void setWordArray(IWord[] wordArray) {
        this.words = wordArray;
    }

    @Override
    public void setPosArray(int[] posArray) {
        this.posArray = posArray;
    }

    @Override
    public String[] getConcepts() {
        assert (words != null && posArray != null && words.length == posArray.length);
        final int length = words.length;
        String[] concepts = new String[length];
        for(int i = 0; i < length; i++) {
            concepts[i] = getConcept(words[i], posArray[i]);
        }
        return concepts;
    }

    private String getConcept(IWord word, int pos) {
        Concept[] concepts = word.getConcepts();

        if(pos == POSUtil.POS_NS) {
            return "n-location";
        }

        if(pos == POSUtil.POS_NR) {
            return "n-name";
        }

        if (null != concepts) {
            String primaryPOS = POSUtil.getPOSString(pos).substring(0, 1).toLowerCase();
            for (int i = 0; i < concepts.length; i++) {
                if (concepts[i].getName().startsWith(primaryPOS)) {
                    return concepts[i].getName();
                }
            }
        }
        return Concept.UNKNOWN.getName();
    }

    @Override
    public void reset() {
        words = null;
        posArray = null;
    }


}
