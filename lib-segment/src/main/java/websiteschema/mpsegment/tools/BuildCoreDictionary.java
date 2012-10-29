/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.tools;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuildCoreDictionary {

    public void loadDictFromPlainFile(String filename) {
        {
            InputStreamReader fileIn = null;
            try {
                FileInputStream fin = null;
                fin = new FileInputStream(filename);
                fileIn = new InputStreamReader(fin, "utf-8");
                //指定读文件格式“UTF-8”
                BufferedReader br = new BufferedReader(fileIn);
                String line = br.readLine();
                while (null != line) {
                    parseLine(line);
                    line = br.readLine();
                }
            } catch (IOException ex) {
                Logger.getLogger(BuildCoreDictionary.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fileIn.close();
                } catch (IOException ex) {
                    Logger.getLogger(BuildCoreDictionary.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void parseLine(String line) {
        String s[] = line.split("[\\t\\s]+");
        if (null != s && 3 == s.length) {
            Word w = dict.get(s[0]);
            if (null == w) {
                w = new Word();
                w.wordName = s[0];
                dict.put(s[0], w);
            }
            w.addPOSAndFreq(Integer.valueOf(s[1]), Integer.valueOf(s[2]));
        }
    }

    public void serialize() {
//        List<Integer> posArray = new ArrayList<Integer>();
//        List<Integer> freqArray = new ArrayList<Integer>();
        Map<String, Integer> words = new LinkedHashMap<String, Integer>();
        List<String> list = new ArrayList<String>(dict.keySet());
        Collections.sort(list);
        for (String key : list) {
            Word w = dict.get(key);
            Set<Integer> pSet = w.pos2freq.keySet();
            words.put(key, pSet.size());
            for (Integer p : pSet) {
                int f = w.pos2freq.get(p);
                posArray.add(p);
                freqArray.add(f);
            }
        }

        int offset = 0;
        for (String word : list) {
            int numberOfPos = words.get(word);
            System.out.println(word + ":0:" + offset + ":" + numberOfPos);
            for (int i = 0; i < numberOfPos; i++) {
                int pos = posArray.get(offset + i);
                int freq = freqArray.get(offset + i);
                System.out.println(pos + ":" + freq);
            }
            offset += numberOfPos;
        }
        System.out.println("offset " + offset);
    }

    public void headWordProcess() {
        List<String> keywords = new ArrayList<String>(dict.keySet());
        Collections.sort(keywords);

        for (String key : keywords) {
            String headword = key.substring(0, 1);
            Word w = dict.get(key);
            ArrayList<Word> wordList = headIndexWords.get(headword);
            if (null == wordList) {
                wordList = new ArrayList<Word>();
                headIndexWords.put(headword, wordList);
            }
            wordList.add(w);
        }
    }

    // print the head word and their words
    // ﻿总共有词：5943：
    //
    // 首字：,：以首字开头的词总数：1：
    // ,:0:27:1:
    public void SaveWIDOutput(String s) throws IOException {
        BufferedWriter output = new BufferedWriter(new FileWriter(s));
        output.write("﻿总共有词：" + headIndexWords.size());
        output.newLine();

        System.out.println("﻿总共有首字词：" + headIndexWords.size() + "：");
        System.out.println("﻿总共有词：" + dict.size() + "：");
        int offset = 0;
        List<String> headwordList = new ArrayList(headIndexWords.keySet());
        Collections.sort(headwordList);

        for (int i = 0; i < headIndexWords.size(); i++) {
            String head = headwordList.get(i);
            List<Word> wordList = headIndexWords.get(head);

            output.newLine();
            output.write("首字：" + head + "：以首字开头的词总数：" + wordList.size() + "：");
            output.newLine();
//            System.out.println("首字：" + head + "以首字开头的词总数：" + wordList.size() + "：");

            for (int j = 0; j < wordList.size(); j++) {
                Word w = wordList.get(j);
                Set<Integer> pSet = w.pos2freq.keySet();
                int numberOfPos = pSet.size();

                output.write(w.wordName + ":0:" + offset + ":" + numberOfPos + ":");
                output.newLine();
//                System.out.println(w.wordName + ":0:" + offset + ":" + numberOfPos + ":");

                for (Integer pos : pSet) {
                    int freq = w.pos2freq.get(pos);
                    posArray.add(pos);
                    freqArray.add(freq);
                }
                offset += numberOfPos;
            }
        }
        output.close();
    }

    public void SaveWPOSOutput(String s) throws IOException {
        BufferedWriter output = new BufferedWriter(new FileWriter(s));

        System.out.println("the total words are :" + posArray.size());
        for (int i = 0; i < posArray.size(); i++) {
            System.out.print(String.valueOf(posArray.get(i)) + "\t");
        }
        System.out.println();
        for (int j = 0; j < freqArray.size(); j++) {
            System.out.println(String.valueOf(freqArray.get(j)) + "\t");
        }
        output.close();
    }

    public void generateWordID_Dic(String origSegFile, String SegFile) {
        try {
            ModifyDic test = new ModifyDic();
            test.loadSegDb(origSegFile);

            test.Save(SegFile);
        } catch (IOException ex) {
            Logger.getLogger(BuildCoreDictionary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generateWordPOS_Dic(String SegFile) {
        try {
            System.out.println((new StringBuilder("top=")).append(posArray.size()).toString());
            ObjectOutputStream objectoutputstream = new ObjectOutputStream(new FileOutputStream(SegFile));
            objectoutputstream.writeInt(posArray.size());
            int[] pos = new int[posArray.size() + 1024];
            int[] freq = new int[posArray.size() + 1024];
            for (int i = 0; i < posArray.size() + 1024; i++) {
                if (i < posArray.size()) {
                    pos[i] = posArray.get(i);
                    freq[i] = freqArray.get(i);
                } else {
                    pos[i] = 0;
                    freq[i] = 0;
                }
            }
            objectoutputstream.writeObject(pos);
            objectoutputstream.writeObject(freq);
            objectoutputstream.close();
        } catch (Exception exception) {
            System.out.println((new StringBuilder("Error: POSDb.save(")).append(SegFile).append(") ").append(exception.getMessage()).toString());
        }
    }

    // 0208add
    private void parseNewWordLine(String line) {
        String s[] = line.split("[\\t\\s]+");
        if (null != s && 2 == s.length) {
            Word w = dict.get(s[0]);
            String posP[] = s[1].split(",");
            if (null == w) {
                w = new Word();
                w.wordName = s[0];
                // add 0221
                for (int i = 0; i < posP.length; i++) {
                    Integer tempPos = (Integer) posTable.get(posP[i]);
                    w.addPOSAndFreq(Integer.valueOf(tempPos), 1);
//					System.out.println(w.wordName + " "
//							+ String.valueOf(tempPos));
                }

                dict.put(s[0], w);
            }
            // w.addPOSAndFreq(Integer.valueOf(s[1]), Integer.valueOf(s[2]));
        }
    }

    public ArrayList addNewWordfile(String inputName) {
        ArrayList<String> words = new ArrayList<String>();
        BufferedReader reader = null;
        try {

            File input = new File(inputName);
            if (!input.exists()) {
                System.out.println("the file doesn't exist...");
            }
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(input), "UTF-8"));
            String sword = null;
            sword = reader.readLine();
            while (sword != null) {
                if (sword.length() > 2) {
                    parseNewWordLine(sword);
                }

                sword = reader.readLine();

            }
        } catch (Exception ex) {
            Logger.getLogger(BuildCoreDictionary.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(BuildCoreDictionary.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return words;
    }

    public static void main(String[] args) {
        BuildCoreDictionary tt = new BuildCoreDictionary();
        try {
            if (args.length > 3) {
//                tt.loadDictFromPlainFile("giant.txt");
                tt.loadDictFromPlainFile(args[0]);
//                tt.addNewWordfile("0208newword.txt");
                tt.addNewWordfile(args[1]);
                tt.headWordProcess();
//                tt.SaveWIDOutput("0221gt1.txt");
                tt.SaveWIDOutput(args[2]);
//                tt.generateWordID_Dic("0221gt1.txt", "segment0221.dict");
//                tt.generateWordPOS_Dic("segment0221.fre");
                tt.generateWordID_Dic(args[2], args[3]+".dict");
                tt.generateWordPOS_Dic(args[3]+".fre");
            }else if(args.length == 3){
                tt.loadDictFromPlainFile(args[0]);
                tt.headWordProcess();
                tt.SaveWIDOutput(args[1]);
                tt.generateWordID_Dic(args[1], args[2]+".dict");
                tt.generateWordPOS_Dic(args[2]+".fre");
            }
            else {
                System.out.println("usage:\njava -cp lib.segment.jar cnnlp.tools.dict.BuildCoreDictionary BasicDictFile AdditionDictFile TmpFile PrefixOfGenerateFile\n" +
                        "java -cp lib.segment.jar cnnlp.tools.dict.BuildCoreDictionary BasicDictFile TmpFile PrefixOfGenerateFile");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    final static Map<String, Integer> newWList = new HashMap<String, Integer>();
//    final static Map<String, Word> dict = new HashMap<String, Word>();
    final static Map<String, Word> dict = new HashMap<String, Word>();
    final static Map<String, ArrayList<Word>> headIndexWords = new HashMap<String, ArrayList<Word>>();
    final static List<Integer> posArray = new ArrayList<Integer>();
    final static List<Integer> freqArray = new ArrayList<Integer>();
    public static HashMap posTable;

    static {
        posTable = new HashMap(256);

        posTable.put("N", new Integer(1));
        posTable.put("T", new Integer(2));
        posTable.put("S", new Integer(3));
        posTable.put("F", new Integer(4));
        posTable.put("M", new Integer(5));
        posTable.put("Q", new Integer(6));
        posTable.put("B", new Integer(7));
        posTable.put("R", new Integer(8));
        posTable.put("V", new Integer(9));
        posTable.put("A", new Integer(10));
        posTable.put("Z", new Integer(11));
        posTable.put("D", new Integer(12));
        posTable.put("P", new Integer(13));
        posTable.put("C", new Integer(14));
        posTable.put("U", new Integer(15));
        posTable.put("Y", new Integer(16));
        posTable.put("E", new Integer(17));
        posTable.put("O", new Integer(18));
        posTable.put("I", new Integer(19));
        posTable.put("L", new Integer(20));
        posTable.put("J", new Integer(21));
        posTable.put("H", new Integer(22));
        posTable.put("K", new Integer(23));
        posTable.put("G", new Integer(24));
        posTable.put("X", new Integer(25));
        posTable.put("W", new Integer(26));
        posTable.put("NR", new Integer(27));
        posTable.put("NS", new Integer(28));
        posTable.put("NT", new Integer(29));
        posTable.put("NZ", new Integer(30));
        posTable.put("NX", new Integer(31));
        posTable.put("NG", new Integer(33));
        posTable.put("VG", new Integer(34));
        posTable.put("AG", new Integer(35));
        posTable.put("TG", new Integer(36));
        posTable.put("DG", new Integer(37));
        posTable.put("OG", new Integer(38));
        posTable.put("VN", new Integer(40));
        posTable.put("AN", new Integer(41));
        posTable.put("VD", new Integer(42));
        posTable.put("AD", new Integer(43));
        posTable.put("UN", new Integer(44));
        posTable.put("NUM", new Integer(5));
        posTable.put("ADJ", new Integer(10));
        posTable.put("PUNC", new Integer(26));
        posTable.put("ECHO", new Integer(18));
        posTable.put("ADV", new Integer(12));
        posTable.put("CLAS", new Integer(6));
        posTable.put("PRON", new Integer(8));
        posTable.put("PREP", new Integer(13));
        posTable.put("STRU", new Integer(15));
        posTable.put("EXPR", new Integer(16));
        posTable.put("CONJ", new Integer(14));
        posTable.put("COOR", new Integer(14));
        posTable.put("PREFIX", new Integer(22));
        posTable.put("SUFFIX", new Integer(23));
        posTable.put("AUX", new Integer(9));
        posTable.put("NT/N", new Integer(29));
//		add by 0221
        posTable.put("MQ", new Integer(6));
        posTable.put("JN", new Integer(1));
        posTable.put("RR", new Integer(8));
        posTable.put("LN", new Integer(20));
        posTable.put("IV", new Integer(19));
        posTable.put("IN", new Integer(19));
        posTable.put("VQ", new Integer(9));
        posTable.put("JB", new Integer(21));
        posTable.put("VI", new Integer(9));
//		add by 0211
        posTable.put("n", new Integer(1));
        posTable.put("t", new Integer(2));
        posTable.put("s", new Integer(3));
        posTable.put("f", new Integer(4));
        posTable.put("m", new Integer(5));
        posTable.put("q", new Integer(6));
        posTable.put("b", new Integer(7));
        posTable.put("r", new Integer(8));
        posTable.put("v", new Integer(9));
        posTable.put("a", new Integer(10));
        posTable.put("z", new Integer(11));
        posTable.put("d", new Integer(12));
        posTable.put("p", new Integer(13));
        posTable.put("c", new Integer(14));
        posTable.put("u", new Integer(15));
        posTable.put("y", new Integer(16));
        posTable.put("e", new Integer(17));
        posTable.put("o", new Integer(18));
        posTable.put("i", new Integer(19));
        posTable.put("l", new Integer(20));
        posTable.put("j", new Integer(21));
        posTable.put("h", new Integer(22));
        posTable.put("k", new Integer(23));
        posTable.put("g", new Integer(24));
        posTable.put("x", new Integer(25));
        posTable.put("w", new Integer(26));
        posTable.put("nr", new Integer(27));
        posTable.put("ns", new Integer(28));
        posTable.put("nt", new Integer(29));
        posTable.put("nz", new Integer(30));
        posTable.put("nx", new Integer(31));
        posTable.put("ng", new Integer(33));
        posTable.put("vg", new Integer(34));
        posTable.put("ag", new Integer(35));
        posTable.put("tg", new Integer(36));
        posTable.put("dg", new Integer(37));
        posTable.put("og", new Integer(38));
        posTable.put("vn", new Integer(40));
        posTable.put("an", new Integer(41));
        posTable.put("vd", new Integer(42));
        posTable.put("ad", new Integer(43));
        posTable.put("un", new Integer(44));
        posTable.put("num", new Integer(5));
        posTable.put("adj", new Integer(10));
        posTable.put("punc", new Integer(26));
        posTable.put("echo", new Integer(18));
        posTable.put("adv", new Integer(12));
        posTable.put("clas", new Integer(6));
        posTable.put("pron", new Integer(8));
        posTable.put("prep", new Integer(13));
        posTable.put("stru", new Integer(15));
        posTable.put("expr", new Integer(16));
        posTable.put("conj", new Integer(14));
        posTable.put("coor", new Integer(14));
        posTable.put("prefix", new Integer(22));
        posTable.put("suffix", new Integer(23));
        posTable.put("aux", new Integer(9));
    }
}
