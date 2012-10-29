package websiteschema.mpsegment.dict;

import java.io.*;
import websiteschema.mpsegment.util.FileUtil;

public class POSAndFreq {

    public static void buildNewDb(int i) {
        pos = new int[length];
        freq = new int[length];
    }

    public static void put(int posIndex, int posFreq) {
        pos[top] = posIndex;
        freq[top] = posFreq;
        top++;
    }

    public static void setFreq(int posBaseIndex, int posIndexOffset, int posIndex, int posFreq) {
        for (int i = 0; i < posIndexOffset; i++) {
            if (pos[posBaseIndex + i] == posIndex) {
                freq[posBaseIndex + i] = posFreq;
            }
        }

    }

    public static void setScaleFreq(int posBaseIndex, int posIndexOffset, int k) {
        double d1 = getFreqSum(posBaseIndex, posIndexOffset);
        if (d1 > 0.0D) {
            d1 = (double) k / d1;
        }
        for (int l = 0; l < posIndexOffset; l++) {
            freq[posBaseIndex + l] = (int) ((double) freq[posBaseIndex + l] * d1);
        }

    }

    public static int getTop() {
        return top;
    }

    public static int getPOS(int i) {
        return pos[i];
    }

    public static int getFreq(int i) {
        return freq[i];
    }

    public static int getFreqSum(int posBaseIndex, int posIndexOffset) {
        int k = 0;
        for (int l = 0; l < posIndexOffset; l++) {
            k += freq[posBaseIndex + l];
        }

        return k;
    }

    public static int getPOSFreq(int posBaseIndex, int posIndexOffset, int posIndex) {
        int l = 0;
        for (int i1 = 0; i1 < posIndexOffset; i1++) {
            if (pos[posBaseIndex + i1] == posIndex) {
                l = freq[posBaseIndex + i1];
            }
        }

        return l;
    }

    public static void incPOSFreq(int posBaseIndex, int posIndexOffset, int posIndex) {

        for (int l = 0; l < posIndexOffset; l++) {
            if (pos[posBaseIndex + l] == posIndex) {
                freq[posBaseIndex + l] = freq[posBaseIndex + l] + 1;
            }
        }

    }

    public static int getMaxOccuriedPOS(int posBaseIndex, int posIndexOffset) {
        int k = 0;
        int l = 0;
        for (int i1 = 0; i1 < posIndexOffset; i1++) {
            if (freq[posBaseIndex + i1] > k) {
                k = freq[posBaseIndex + i1];
                l = i1;
            }
        }

        return pos[posBaseIndex + l];
    }

    public static void loadPOSDb(String s) {
        try {
            if (top <= 0) {
                ObjectInputStream objectinputstream = new ObjectInputStream(FileUtil.getResourceAsStream("segment.fre"));
                top = objectinputstream.readInt();
                pos = (int[]) objectinputstream.readObject();
                freq = (int[]) objectinputstream.readObject();
            }
        } catch (Exception exception) {
            System.out.println((new StringBuilder("Error: POSDb.load(")).append(s).append(") ").append(exception.getMessage()).toString());
        }
    }

    public static void savePOSDb(String s) {
        try {
            System.out.println((new StringBuilder("top=")).append(top).toString());
            ObjectOutputStream objectoutputstream = new ObjectOutputStream(new FileOutputStream(s));
            objectoutputstream.writeInt(top);
            objectoutputstream.writeObject(pos);
            objectoutputstream.writeObject(freq);
            objectoutputstream.close();
        } catch (Exception exception) {
            System.out.println((new StringBuilder("Error: POSDb.save(")).append(s).append(") ").append(exception.getMessage()).toString());
        }
    }

    public static String toText() {
        String ls = System.getProperty("line.separator");
        String space = " ";
        StringBuilder sb = new StringBuilder();
        sb.append("//segment.fre").append(ls);

        //sb.append("total items in this dictionary"+ls);
        sb.append("[top]").append(ls);
        sb.append(top).append(ls);

        //pos
        sb.append("[pos]").append(ls);
        for (int i = 0; i < top; i++) {
            sb.append(pos[i]).append(space);
        }

        //freq
        sb.append(ls);
        sb.append("[freq]").append(ls);
        for (int i = 0; i < top; i++) {
            sb.append(freq[i]).append(space);
        }
        sb.append(ls);
        return sb.toString();
    }
    private static int length = 91000;
    private static int pos[];
    private static int freq[];
    private static int top = 0;
}
