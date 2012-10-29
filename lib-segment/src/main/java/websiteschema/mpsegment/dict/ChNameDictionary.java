package websiteschema.mpsegment.dict;

import java.io.*;
import java.util.Map;
import org.apache.log4j.Logger;
import websiteschema.mpsegment.util.FileUtil;
import websiteschema.mpsegment.util.SerializeHandler;

public class ChNameDictionary {

    private static Logger l = Logger.getLogger("segment");

    public ChNameDictionary() {
        factor = 0.88400000000000001D;
        xingTop = 1;
        mingTop = 1;
    }

    public void outSummary() {
        System.out.println((new StringBuilder("xingTop=")).append(xingTop).toString());
        System.out.println((new StringBuilder("mingTop=")).append(mingTop).toString());
        for (int i1 = 0; i1 < 3; i1++) {
            System.out.println((new StringBuilder("totalMingProb[i]=")).append(totalMingProb[i1]).toString());
        }
    }

    private int getXingProb(String s) {
        int index = get(xingHashMap, s);
        double prob = 0.0D;
        if (index <= 0) {
            index = 0;
        } else {
            prob = (double) xingFreq[index] * (1.0D + xingProb[index]);
        }
        return (int) prob;
    }

    private int get(Map<String, Integer> map, String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return 0;
    }

    private int getMingFreq0(String name) {
        int index = get(mingHashMap, name);
        if (index <= 0) {
            index = 0;
        } else {
            index = mingFreqs[index][0];
        }
        return index;
    }

    private int getMingFreq1(String name) {
        int index = get(mingHashMap, name);
        if (index <= 0) {
            index = 0;
        } else {
            index = mingFreqs[index][1];
        }
        return index;
    }

    private int getMingFreq2(String name) {
        int index = get(mingHashMap, name);
        if (index <= 0) {
            index = 0;
        } else {
            index = mingFreqs[index][2];
        }
        return index;
    }

    public boolean isXing(String familyName) {
        return xingHashMap.containsKey(familyName);
    }

    public double computeLgLP3(String familyName, String fisrtNameWord, String secondNameWord) {
        double d2 = getXingProb(familyName);
        double d3 = getMingFreq0(fisrtNameWord);
        double d4 = getMingFreq1(secondNameWord);
        return getNameWordProb(d2, d3, d4);
    }

    private double getNameWordProb(double d2, double d3, double d4) {
        d2 = d2 * factor * ((d3 + d4) / 1000000D);
        if (d4 <= 0.0D && d2 > 1.0D) {
            d2 *= 0.90000000000000002D;
        }
        return d2;
    }

    public double computeLgLP3_2(String s, String s1) {
        double d2 = getXingProb(s.substring(0, 1));
        double d3 = getMingFreq0(s.substring(1));
        double d4 = getMingFreq1(s1);
        return getNameWordProb(d2, d3, d4);
    }

    public double computeLgLP2(String s, String s1) {
        double d2 = getXingProb(s);
        double d3 = getMingFreq2(s1);
        return getNameWordProb(d2, d3);
    }

    private double getNameWordProb(double d2, double d3) {
        d2 *= d3 / 1000000D;
        return d2;
    }

    public double computeLgMing23(String s, String s1) {
        double d1 = getMingFreq0(s);
        double d2 = getMingFreq1(s1);
        d1 += d2;
        d1 /= 1000D;
        return d1;
    }

    public double getRightBoundaryWordLP(String s) {
        int index = get(rightBoundaryHashMap, s);
        double d1 = 0.0D;
        if (index > 0) {
            d1 = (rightBoundaryProbs[index] - 0.10000000000000001D) / 3D;
        }
        return d1;
    }

    public String toText() {
        String ls = System.getProperty("line.separator");
        String space = " ";
        StringBuilder sb = new StringBuilder();
        sb.append("//ChName.dict").append(ls);
        sb.append("[xingHashMap] //").append(xingHashMap.size()).append(ls);
        for (String key : xingHashMap.keySet()) {
            sb.append(key).append(space).append(xingHashMap.get(key)).append(space);
        }
        sb.append(ls);
        sb.append("[xingFreq] //").append(xingFreq.length).append(ls);
        for (int i = 0; i < xingFreq.length; i++) {
            sb.append(xingFreq[i]).append(space);
        }

        sb.append(ls);
        sb.append("[mingHashMap] //").append(mingHashMap.size()).append(ls);

        for (String key : mingHashMap.keySet()) {
            sb.append(key).append(space).append(mingHashMap.get(key)).append(space);
        }
        sb.append(ls);
        sb.append("[mingFreqs] //").append(mingFreqs.length).append(ls);
        for (int i = 0; i < mingFreqs.length; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(mingFreqs[i][j]).append(space);
            }
            sb.append("\t");
        }

        sb.append(ls);
        sb.append("[totalMingProb] //").append(totalMingProb.length).append(ls);
        for (int i = 0; i < totalMingProb.length; i++) {
            sb.append(totalMingProb[i]).append(space);
        }

        sb.append(ls);
        sb.append("[fuXing] //").append(fuXing.size()).append(ls);
        for (String key : fuXing.keySet()) {
            sb.append(key).append(space).append(fuXing.get(key)).append(space);
        }
        sb.append(ls);
        sb.append("[xingProb] //").append(xingProb.length).append(ls);
        for (int i = 0; i < xingProb.length; i++) {
            sb.append(xingProb[i]).append(space);
        }
        sb.append(ls);
        sb.append("[rightBoundaryHashMap] //").append(rightBoundaryHashMap.size()).append(ls);
        for (String key : rightBoundaryHashMap.keySet()) {
            sb.append(key).append(space).append(rightBoundaryHashMap.get(key)).append(space);
        }
        sb.append(ls);
        sb.append("[rightBoundaryProbs] //").append(rightBoundaryProbs.length).append(ls);
        for (int i = 0; i < rightBoundaryProbs.length; i++) {
            sb.append(rightBoundaryProbs[i]).append(space);
        }
        sb.append(ls);
        return sb.toString();
    }

    public void saveNameDict(String dictFile) {
        try {
            SerializeHandler writeHandler = new SerializeHandler(new File(dictFile), SerializeHandler.MODE_WRITE_ONLY);
            writeHandler.serializeMapStringInt(xingHashMap);
            writeHandler.serializeArrayInt(xingFreq);
            writeHandler.serializeMapStringInt(mingHashMap);
            writeHandler.serialize2DArrayInt(mingFreqs);
            writeHandler.serializeArrayDouble(totalMingProb);
            writeHandler.serializeMapStringInt(fuXing);
            writeHandler.serializeArrayDouble(xingProb);
            writeHandler.serializeMapStringInt(rightBoundaryHashMap);
            writeHandler.serializeArrayDouble(rightBoundaryProbs);
            writeHandler.close();
        } catch (Exception exception) {
            System.out.println((new StringBuilder("Error: saveNameDict.save(")).append(dictFile).append(") ").append(exception.getMessage()).toString());
            l.error((new StringBuilder("Error: saveNameDict.save(")).append(dictFile).append(") ").append(exception.getMessage()).toString());
        }
    }

    public void loadNameDict(String dictFile) {
        try {
            DataInputStream objectinputstream = null;
            File f = new File(dictFile);
            SerializeHandler readHandler = null;
            if (f.exists()) {
                readHandler = new SerializeHandler(f, SerializeHandler.MODE_READ_ONLY);
            } else {
                objectinputstream = new DataInputStream(
                        FileUtil.getResourceAsStream(dictFile));
                readHandler = new SerializeHandler(objectinputstream);
            }
            xingHashMap = readHandler.deserializeMapStringInt();
            xingFreq = readHandler.deserializeArrayInt();
            mingHashMap = readHandler.deserializeMapStringInt();
            mingFreqs = readHandler.deserialize2DArrayInt();
            totalMingProb = readHandler.deserializeArrayDouble();
            fuXing = readHandler.deserializeMapStringInt();
            xingProb = readHandler.deserializeArrayDouble();
            rightBoundaryHashMap = readHandler.deserializeMapStringInt();
            rightBoundaryProbs = readHandler.deserializeArrayDouble();
            objectinputstream.close();
        } catch (Exception exception) {
            System.out.println((new StringBuilder()).append("[Segment] ").append(dictFile).append("没找到！").append(exception.getMessage()).toString());
            l.debug((new StringBuilder()).append("[Segment] ").append(dictFile).append("没找到！").append(exception.getMessage()).toString());
        }
    }
    private double factor;
    private Map<String, Integer> xingHashMap;
    private int xingFreq[];
    private double xingProb[];
    private Map<String, Integer> mingHashMap;
    private int mingFreqs[][];
    private double totalMingProb[];
    private int xingTop;
    private int mingTop;
    private Map<String, Integer> fuXing;

    public Map<String, Integer> getFuXing() {
        return fuXing;
    }
    private Map<String, Integer> rightBoundaryHashMap;
    private double rightBoundaryProbs[];
}
