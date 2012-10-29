package websiteschema.mpsegment.dict;

import websiteschema.mpsegment.concept.Concept;

/**
 *
 * @author ray
 */
public class UnknownWord implements IWord {

    final static int log2Freq = (int) (Math.log(1) * 100D);
    String wordName;

    public UnknownWord(String wordName) {
        this.wordName = wordName;
    }

    @Override
    public int getLog2Freq() {
        return log2Freq;
    }

    @Override
    public int getDomainType() {
        return 0;
    }

    @Override
    public long getOccuredCount(String s) {
        if (s.equals(POSUtil.getPOSString(POSUtil.POS_UNKOWN))) {
            return 1L;
        }
        return 0L;
    }

    @Override
    public long getOccuredSum() {
        return 1L;
    }

    @Override
    public POSArray getPOSArray() {
        return POSArrayFactory.getUnknownWordPOSArray();
    }

    @Override
    public int[][] getWordPOSTable() {
        return getPOSArray().getWordPOSTable();
    }

    @Override
    public int getWordLength() {
        return wordName.length();
    }

    @Override
    public String getWordName() {
        return wordName;
    }

    @Override
    public void incOccuredCount(String s) {
    }

    @Override
    public void setDomainType(int i) {
    }

    @Override
    public void setOccuredCount(String s, int i) {
    }

    @Override
    public void setOccuredSum(int i) {
    }

    @Override
    public int getWordMaxPOS() {
        return POSUtil.POS_UNKOWN;
    }

    @Override
    public String toDBFString() {
        StringBuilder stringbuffer = new StringBuilder();
        stringbuffer.append(getPOSArray().toDBFString(getWordName()));
        return stringbuffer.toString();
    }

    @Override
    public Concept[] getConcepts() {
        return new Concept[]{Concept.UNKNOWN};
    }

}
