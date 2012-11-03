package websiteschema.mpsegment.core;

public class WordAtom {
    public String word;
    public int pos;
    public int domainType;
    public String concept;

    public int length() {
        return word.length();
    }
}
