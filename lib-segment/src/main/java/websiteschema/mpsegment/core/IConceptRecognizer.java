package websiteschema.mpsegment.core;

import websiteschema.mpsegment.dict.IWord;

public interface IConceptRecognizer {

    public void setWordArray(IWord[] wordArray);

    public void setPosArray(int[] posArray);

    public String[] getConcepts();

    public void reset();
}
