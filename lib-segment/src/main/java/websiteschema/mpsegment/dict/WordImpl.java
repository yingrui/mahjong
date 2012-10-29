package websiteschema.mpsegment.dict;

import websiteschema.mpsegment.concept.Concept;
import websiteschema.mpsegment.util.BufReader;

import java.io.IOException;
import java.io.Serializable;

public class WordImpl implements Serializable, Comparable, IWord {

    public WordImpl(String wordName) {
        log2Freq = 0;
        this.wordName = wordName;
        domainType = 0;
        posArray = new POSArray();
        posArray.add(new POS("UN", 200));
    }

    public WordImpl(String wordName, int i) {
        log2Freq = 0;
        this.wordName = wordName;
        domainType = i;
    }

    public WordImpl(BufReader bufreader)
            throws IOException {
        log2Freq = 0;
    }

    @Override
    public void setOccuredCount(String posName, int freq) {
        int[][] posTable = posArray.getWordPOSTable();
        for (int i = 0; i < posTable.length; i++) {
            String pos = POSUtil.getPOSString(posTable[i][0]);
            if (posName.equals(pos)) {
                posTable[i][1] = freq;
                calculateLogFreq();
                break;
            }
        }
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

    @Override
    public POSArray getPOSArray() {
        return posArray;
    }

    @Override
    public int[][] getWordPOSTable() {
        return getPOSArray().getWordPOSTable();
    }

    public void setPosArray(POSArray posArray) {
        this.posArray = posArray;
    }

    @Override
    public int getWordMaxPOS() {
        int maxOccuredPOS = POSUtil.POS_UNKOWN;
        int maxOccured = 0;
        int[][] posTable = posArray.getWordPOSTable();
        for (int i = 0; i < posTable.length; i++) {
            int freq = posTable[i][1];
            if (freq > maxOccured) {
                maxOccuredPOS = posTable[i][0];
            }
        }

        return maxOccuredPOS;
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
    public void setDomainType(int i) {
        domainType = i;
    }

    @Override
    public int getDomainType() {
        return domainType;
    }

    @Override
    public int getLog2Freq() {
        if (log2Freq == 0) {
            calculateLogFreq();
        }
        return log2Freq;
    }

    private void calculateLogFreq() {
        log2Freq = (int) (Math.log(getOccuredSum() + 1L) * 100D);
    }

    @Override
    public long getOccuredSum() {
        int[][] posTable = getPOSArray().getWordPOSTable();
        int occuredSum = 0;
        for (int i = 0; i < posTable.length; i++) {
            occuredSum += posTable[i][1];
        }
        return occuredSum;
    }

    @Override
    public long getOccuredCount(String s) {
        int pos = POSUtil.getPOSIndex(s);
        int[][] posTable = getPOSArray().getWordPOSTable();
        for (int i = 0; i < posTable.length; i++) {
            if (posTable[i][0] == pos) {
                return (long) posTable[i][1];
            }
        }
        return 0L;
    }

    @Override
    public void incOccuredCount(String s) {
        setOccuredCount(s, (int) getOccuredCount(s) + 1);
    }

    private POSArray buildPOSArray() {
        posArray = new POSArray();
        for (int i = 0; i < wordPOSNumber; i++) {
            String name = POSUtil.getPOSString(POSAndFreq.getPOS(indexOfPosDB + i));
            int count = POSAndFreq.getFreq(indexOfPosDB + i);
            POS pos = new POS(name, count);
            posArray.add(pos);
        }
        return posArray;
    }

    @Override
    public int compareTo(Object obj) {
        if (obj != null && (obj instanceof String)) {
            return wordName.compareTo((String) obj);
        }
        if (obj != null && (obj instanceof WordImpl)) {
            return wordName.compareTo(((WordImpl) obj).wordName);
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof WordImpl)) {
            return wordName.equals(((WordImpl) obj).wordName);
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
        return null != concepts ? concepts : new Concept[]{Concept.UNKNOWN};
    }

    public void setConcepts(Concept[] concepts) {
        this.concepts = concepts;
    }

    private String wordName;
    private int domainType;
    private int log2Freq;
    private int indexOfPosDB;
    private int wordPOSNumber;
    private POSArray posArray;
    private Concept[] concepts;
}
