package websiteschema.mpsegment.dict;

import org.apache.log4j.Logger;
import websiteschema.mpsegment.util.BufReader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class POSArray
        implements Serializable {

    private static final Logger l = Logger.getLogger("segment");

    public POSArray(RandomAccessFile randomaccessfile)
            throws IOException {
        load(randomaccessfile);
    }

    public POSArray(BufReader bufreader)
            throws IOException {
        load(bufreader);
    }

    public POSArray() {
        posTable = new LinkedHashMap<String, POS>();
    }

    public void setPOSCount(String pos, int freq) {
        if (pos == null || pos.trim().equals("")) {
            return;
        }
        POS a1 = posTable.get(pos.trim());
        if (a1 != null) {
            a1.setCount(freq);
        } else {
            a1 = new POS(pos.trim(), freq);
        }
        posTable.put(pos.trim(), a1);
    }

    public void setPOSCount(int count) {
        double discount = (double) getOccurredSum();
        if (discount > 0.0D) {
            discount = (double) count / discount;
        }
        for (String name : posTable.keySet()) {
            POS pos = posTable.get(name);
            double d1 = ((double) pos.getCount()) * discount;
            setPOSCount(name, (int) d1);
        }
    }

    public void add(POSArray posArray) {
        if (posArray == null && arrayPOSAndFreq == null) {
            return;
        }

        if (posArray == null) {
            posTable = new LinkedHashMap<String, POS>(arrayPOSAndFreq.length);
            for(int i = 0; i < arrayPOSAndFreq.length; i++) {
                String posString = POSUtil.getPOSString(arrayPOSAndFreq[i][0]);
                posTable.put(posString, new POS(posString, arrayPOSAndFreq[i][1]));
            }
        }

        int [][] arrayPosTable = posArray.getWordPOSTable();
        for(int i = 0; i < arrayPosTable.length; i++) {
            String posString = POSUtil.getPOSString(arrayPosTable[i][0]);
            add(new POS(posString, arrayPosTable[i][1]));
        }
    }

    public void add(POS pos) {
        if (pos == null) {
            return;
        }
        POS a2 = posTable.get(pos.getName());
        if (a2 != null) {
            a2.setCount(a2.getCount() + pos.getCount());
        } else {
            a2 = new POS(pos.getName(), pos.getCount());
        }
        posTable.put(a2.getName(), a2);
    }

    public void incPOSCount(String s) {
        if (s == null || s.trim().equals("")) {
            return;
        }
        POS a1 = posTable.get(s.trim());
        if (a1 != null) {
            a1.setCount(a1.getCount() + 1);
        } else {
            a1 = new POS(s.trim(), 1);
        }
        posTable.put(s.trim(), a1);
    }

    public int getSize() {
        return posTable.size();
    }

    public int[][] getWordPOSTable() {
        if(null == arrayPOSAndFreq) {
            arrayPOSAndFreq = buildPOSArray();
        }
        return arrayPOSAndFreq;
    }

    public int[][] buildPOSArray() {
        POS arrayPOS[] = new POS[getSize()];
        int i = 0;
        for (String name : posTable.keySet()) {
            arrayPOS[i++] = posTable.get(name);
        }
        int[][] posAndFreq = new int[arrayPOS.length][2];
        for (int j = 0; j < arrayPOS.length; j++) {
            posAndFreq[j][0] = POSUtil.getPOSIndex(arrayPOS[j].getName());
            posAndFreq[j][1] = arrayPOS[j].getCount();
        }
        posTable.clear();
        posTable = null;
        this.arrayPOSAndFreq = posAndFreq;
        return posAndFreq;
    }

    public int getWordPOSTable(int arrayPOSAndFreq[][]) {
        int i = 0;
        int j = 0;
        for (String name : posTable.keySet()) {
            POS pos = posTable.get(name);
            if (j < arrayPOSAndFreq.length) {
                arrayPOSAndFreq[j][0] = POSUtil.getPOSIndex(pos.getName());
                arrayPOSAndFreq[j][1] = pos.getCount();
            }
            j++;
        }

        i = j;
        for (int k = i; k < arrayPOSAndFreq.length; k++) {
            arrayPOSAndFreq[k][0] = 0;
            arrayPOSAndFreq[k][1] = 0;
        }

        return i;
    }

    public int getWordMaxPOS() {
        int posIndex = 1;
        int count = 0;
        for (String name : posTable.keySet()) {
            POS pos = posTable.get(name);
            if (pos.getCount() > count) {
                posIndex = POSUtil.getPOSIndex(pos.getName());
                count = pos.getCount();
            }
        }

        return posIndex;
    }

    private void load(RandomAccessFile resources)
            throws IOException {
        byte numPos = resources.readByte();
        posTable = new LinkedHashMap<String, POS>(numPos);
        for (int i = 0; i < numPos; i++) {
            byte nameLength = resources.readByte();
            byte nameBytes[] = new byte[nameLength];
            resources.read(nameBytes);
            String name = new String(nameBytes);
            int count = resources.readInt();
            l.trace(name + "->" + count);
            POS pos = new POS(name, count);
            posTable.put(name, pos);
        }
    }

    private void load(BufReader resources)
            throws IOException {
        int size = resources.readIntByte();
        posTable = new LinkedHashMap<String, POS>(size);
        for (int i = 0; i < size; i++) {
            int len = resources.readIntByte();
            byte nameBytes[] = new byte[len];
            resources.read(nameBytes);
            String name = new String(nameBytes);
            int count = resources.readInt();
            l.trace(name + "->" + count);
            POS pos = new POS(name, count);
            posTable.put(name, pos);
        }
    }

    public void save(RandomAccessFile randomaccessfile)
            throws IOException {
        randomaccessfile.write((byte) getSize());
        POS pos[] = new POS[getSize()];
        int i = 0;
        for (String name : posTable.keySet()) {
            pos[i++] = posTable.get(name);
        }
        Arrays.sort(pos);
        for (int j = 0; j < pos.length; j++) {
            byte bytes[] = pos[j].getName().getBytes();
            randomaccessfile.write((byte) bytes.length);
            randomaccessfile.write(bytes);
            randomaccessfile.writeInt(pos[j].getCount());
        }

    }

    public long getOccurredCount(String s) {
        if (s == null || s.trim().equals("")) {
            return 0L;
        }
        POS pos = posTable.get(s.trim());
        if (pos == null) {
            return 0L;
        } else {
            return (long) pos.getCount();
        }
    }

    public long getOccurredSum() {
        long sum = 0L;
        int[][] posAndFreq = getWordPOSTable();
        for(int i = 0; i < posAndFreq.length; i++) {
            sum += posAndFreq[i][1];
        }

        return sum;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < arrayPOSAndFreq.length; i++) {
            POS pos = new POS(POSUtil.getPOSString(arrayPOSAndFreq[i][0]), arrayPOSAndFreq[i][1]);
            stringBuilder.append((new StringBuilder(String.valueOf(pos.toString()))).append("\n").toString());
        }

        return stringBuilder.toString();
    }

    public String toDBFString(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < arrayPOSAndFreq.length; i++) {
            POS pos = new POS(POSUtil.getPOSString(arrayPOSAndFreq[i][0]), arrayPOSAndFreq[i][1]);
            stringBuilder.append((new StringBuilder(String.valueOf(s))).append(pos.toDBFString()).append("\n").toString());
        }

        return stringBuilder.toString();
    }
    int arrayPOSAndFreq[][] = null;
    //TODO: remove posTable, use array of Int only.
    LinkedHashMap<String, POS> posTable;
}
