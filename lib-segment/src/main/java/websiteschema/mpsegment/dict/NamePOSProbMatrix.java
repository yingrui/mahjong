package websiteschema.mpsegment.dict;

import websiteschema.mpsegment.util.*;
import java.io.*;

public class NamePOSProbMatrix {

    public NamePOSProbMatrix() {
        numPOS = 45;
        posFreq = new int[numPOS];
        posBigram = new int[numPOS][numPOS];
        posBigram3 = new int[numPOS][numPOS];
        corpusSize = 1;
    }

    public NamePOSProbMatrix(String s) {
        numPOS = 45;
        posFreq = new int[numPOS];
        posBigram = new int[numPOS][numPOS];
        posBigram3 = new int[numPOS][numPOS];
        corpusSize = 1;
        loadProbMatrix(s);
    }

    public int getCorpusSize() {
        return corpusSize;
    }

    public void addMatrix(String pos1, String pos2) {
        int posIndex1 = 0;
        int posIndex2 = 0;
        if (pos1.length() > 0) {
            posIndex1 = POSUtil.getPOSIndex(pos1);
        }
        if (pos2.length() > 0) {
            posIndex2 = POSUtil.getPOSIndex(pos2);
        }
        if (posIndex1 >= 0 && posIndex2 >= 0) {
            posBigram[posIndex1][posIndex2]++;
            posFreq[posIndex2]++;
            if (posIndex2 >= 44) {
                System.out.println((new StringBuilder("---")).append(pos2).toString());
            }
        }
    }

    public void addMatrix3(String pos1, String pos2) {
        int posIndex1 = 0;
        int posIndex2 = 0;
        if (pos1.length() > 0) {
            posIndex1 = POSUtil.getPOSIndex(pos1);
        }
        if (pos2.length() > 0) {
            posIndex2 = POSUtil.getPOSIndex(pos2);
        }
        if (posIndex1 >= 0 && posIndex2 >= 0) {
            posBigram3[posIndex1][posIndex2]++;
            if (posIndex2 >= 44) {
                System.out.println((new StringBuilder("---")).append(pos2).toString());
            }
        }
    }

    public void outMatrix() {
        for (int i = 0; i < numPOS; i++) {
            for (int l = 0; l < numPOS; l++) {
                System.out.print((new StringBuilder(String.valueOf(posBigram[i][l]))).append(" ").toString());
            }
            System.out.println((new StringBuilder("---")).append(i).toString());
        }
        System.out.println("---------------------------");
        for (int j = 0; j < numPOS; j++) {
            System.out.println((new StringBuilder(String.valueOf(POSUtil.getPOSString(j)))).append(" ").append(posFreq[j]).toString());
        }
        System.out.println("---------------------------");
        for (int k = 0; k < numPOS; k++) {
            System.out.println(posFreq[k]);
        }
        System.out.println("---------------------------");
    }

    public void outMatrix3() {
        for (int i = 0; i < numPOS; i++) {
            for (int k = 0; k < numPOS; k++) {
                System.out.print((new StringBuilder(String.valueOf(posBigram3[i][k]))).append(" ").toString());
            }
            System.out.println((new StringBuilder("---")).append(i).toString());
        }
        System.out.println("---------------------------");
        for (int j = 0; j < numPOS; j++) {
            System.out.println((new StringBuilder(String.valueOf(POSUtil.getPOSString(j)))).append(" ").append(posFreq[j]).toString());
        }
        System.out.println("---------------------------");
    }

    public int getTagFreqs(int i) {
        return posFreq[i];
    }

    public double getNameCoProb(int i, int j) {
        double d1 = 0.0D;
        if (posFreq[i] == 0) {
            return 1.0000000000000001E-005D;
        } else {
            double d2 = posBigram[i][j] + 1;
            d2 /= corpusSize;
            return d2;
        }
    }

    public double getCoProb(int i, int j) {
        double d1 = 0.0D;
        if (posFreq[i] == 0) {
            return 1.0000000000000001E-005D;
        } else {
            double d2 = (0.29999999999999999D * (double) (posFreq[j] + 1)) / (double) corpusSize + (0.69999999999999996D * (double) (posBigram[i][j] + 1)) / (double) posFreq[i];
            return d2;
        }
    }

    private void loadProbMatrix(BufReader bufreader)
            throws IOException {
        try {
            numPOS = bufreader.readInt();
            for (int i = 0; i < numPOS; i++) {
                posFreq[i] = bufreader.readInt();
                corpusSize += posFreq[i];
            }

            for (int j = 0; j < numPOS; j++) {
                for (int l = 0; l < numPOS; l++) {
                    posBigram[j][l] = bufreader.readInt();
                }

            }

            for (int k = 0; k < numPOS; k++) {
                for (int i1 = 0; i1 < numPOS; i1++) {
                    posBigram3[k][i1] = bufreader.readInt();
                }

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String toText() {

        String ls = System.getProperty("line.separator");
        String space = " ";
        StringBuilder sb = new StringBuilder();

        sb.append("//namePOSMatrix.fre").append(ls);
        sb.append("[NumPOS]").append(ls);
        sb.append(numPOS).append(ls);
        sb.append("[posFreq]").append(ls);
        for (int i = 0; i < numPOS; i++) {
            sb.append(posFreq[i]).append(space);
        }
        sb.append(ls);
        sb.append("[posBigram]").append(ls);
        for (int i = 0; i < numPOS; i++) {
            for (int j = 0; j < numPOS; j++) {
                sb.append(posBigram[i][j]).append(space);
            }
            sb.append(ls);
        }
        return sb.toString();
    }

    private void sortBigram(int posIndex1, int posIndex2, int posBigram) {
        for (int l = 0; l < sortedBigram.length; l++) {
            if (sortedBigram[l][2] >= posBigram) {
                continue;
            }
            for (int i1 = sortedBigram.length - 1; i1 > l; i1--) {
                sortedBigram[i1][0] = sortedBigram[i1 - 1][0];
                sortedBigram[i1][1] = sortedBigram[i1 - 1][1];
                sortedBigram[i1][2] = sortedBigram[i1 - 1][2];
            }

            sortedBigram[l][0] = posIndex1;
            sortedBigram[l][1] = posIndex2;
            sortedBigram[l][2] = posBigram;
            break;
        }

    }

    public void printSortedList() {
        double d1 = 0.0D;
        for (int i = 0; i < sortedBigram.length; i++) {
            double d2 = sortedBigram[i][2] * 10000;
            d2 /= posBigram3[sortedBigram[i][0]][sortedBigram[i][1]];
            System.out.print((new StringBuilder(String.valueOf(POSUtil.getPOSString(sortedBigram[i][0])))).append(" ").append(POSUtil.getPOSString(sortedBigram[i][1])).append(" ").append(sortedBigram[i][2]).toString());
            System.out.println((new StringBuilder("  ")).append(d2).toString());
        }

    }

    public void sort1() {
        sortedBigram = new int[200][3];
        int ai[] = new int[20];
        for (int i = 0; i < numPOS; i++) {
            for (int j = 0; j < numPOS; j++) {
                if (posBigram[i][j] > 0) {
                    sortBigram(i, j, posBigram[i][j]);
                }
            }

        }

    }

    private void loadProbMatrix(String s) {
        try {
            BufReader bytearrayreader = new ByteArrayReader(FileUtil.getResourceAsStream("namePOSMatrix.fre"));
            loadProbMatrix(bytearrayreader);
            bytearrayreader.close();
            bytearrayreader = null;
        } catch (IOException ex) {
            System.out.println((new StringBuilder()).append("[Segment] ").append(s).append("\u6CA1\u627E\u5230\uFF01").toString());
        }
    }

    public void saveProbMatrix(RandomAccessFile randomaccessfile) {
        try {
            randomaccessfile.writeInt(numPOS);
            for (int i = 0; i < numPOS; i++) {
                randomaccessfile.writeInt(posFreq[i]);
            }

            for (int j = 0; j < numPOS; j++) {
                for (int l = 0; l < numPOS; l++) {
                    randomaccessfile.writeInt(posBigram[j][l]);
                }

            }

            for (int k = 0; k < numPOS; k++) {
                for (int i1 = 0; i1 < numPOS; i1++) {
                    randomaccessfile.writeInt(posBigram3[k][i1]);
                }

            }

        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }

    public void saveToFile(String s) {
        File file = new File(s);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile randomaccessfile = new RandomAccessFile(file, "rw");
            saveProbMatrix(randomaccessfile);
            randomaccessfile.close();
            randomaccessfile = null;
        } catch (IOException ioexception) {
            System.out.println((new StringBuilder("NamePOSProbMatrix.saveToFile() error:")).append(ioexception.getMessage()).toString());
        }
    }
    private int numPOS;
    private int posFreq[];
    private int posBigram[][];
    private int posBigram3[][];
    private int corpusSize;
    private int sortedBigram[][];
}
