package websiteschema.mpsegment.dict;

import websiteschema.mpsegment.concept.Concept;

import java.io.Serializable;

public class DomainWordItem
        implements Serializable, Comparable, IWord {

    public DomainWordItem(String wordName, int domainType) {
        log2Freq = 0;
        this.wordName = wordName;
        this.domainType = domainType;
    }

    @Override
    public void setOccuredCount(String pos, int freq) {
        posArray.setPOSCount(pos, freq);
    }

    @Override
    public void setOccuredSum(int sum) {
        double factor = (double)sum / (double)getOccuredSum();
        int[][] posTable = posArray.getWordPOSTable();
        for (int i = 0; i < posTable.length; i++) {
            int freq = posTable[i][1];
            posTable[i][1] = (int) (freq * factor);
            calculateLogFreq();
        }
        calculateLogFreq();
    }

    private void calculateLogFreq() {
        log2Freq = (int) (Math.log(getOccuredSum() + 1L) * 100D);
    }

    @Override
    public POSArray getPOSArray() {
        return posArray;
    }

    @Override
    public int[][] getWordPOSTable() {
        return getPOSArray().getWordPOSTable();
    }

    @Override
    public int getWordMaxPOS() {
        return posArray.getWordMaxPOS();
    }

    @Override
    public String getWordName() {
        return wordName;
    }

    @Override
    public int getWordLength() {
        return wordName.length();
    }

    @Override
    public void setDomainType(int domainType) {
        this.domainType = domainType;
    }

    @Override
    public int getDomainType() {
        return domainType;
    }

    @Override
    public int getLog2Freq() {
        if (log2Freq == 0) {
            log2Freq = (int) (Math.log(getOccuredSum() + 1L) * 100D);
        }
        return log2Freq;
    }

    @Override
    public long getOccuredSum() {
        return posArray.getOccurredSum();
    }

    @Override
    public long getOccuredCount(String s) {
        return posArray.getOccurredCount(s);
    }

    @Override
    public void incOccuredCount(String s) {
        posArray.incPOSCount(s);
    }

    @Override
    public int compareTo(Object obj) {
        if (obj != null && (obj instanceof String)) {
            return wordName.compareTo((String) obj);
        }
        if (obj != null && (obj instanceof DomainWordItem)) {
            String otherString = ((DomainWordItem) obj).wordName;
            return wordName.compareTo(otherString);
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof DomainWordItem)) {
            return wordName.equals(((DomainWordItem) obj).wordName);
        }
        if (obj != null && (obj instanceof String)) {
            return wordName.equals(obj);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((new StringBuilder(String.valueOf(getWordName()))).append("\n").toString());
        stringBuilder.append(getPOSArray().toString());
        return stringBuilder.toString();
    }

    @Override
    public String toDBFString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getPOSArray().toDBFString(getWordName()));
        return stringBuilder.toString();
    }

    @Override
    public Concept[] getConcepts() {
        return new Concept[]{Concept.UNKNOWN};
    }

    private void addPOS(int posIndex, int posFreq) {
        if (posIndex > 0) {
            String posString = POSUtil.getPOSString(posIndex);
            posArray.add(new POS(posString, posFreq));
        }
    }
    //
    private String wordName;
    private int domainType;
    private int log2Freq;
    POSArray posArray = new POSArray();
}
