package websiteschema.mpsegment.core;

/**
* Created with IntelliJ IDEA.
* User: twer
* Date: 9/23/12
* Time: 8:41 PM
* To change this template use File | Settings | File Templates.
*/
public class WordAtom {
    public String word;
    public int pos;
    public int concept;

    public int length() {
        return word.length();
    }
}
