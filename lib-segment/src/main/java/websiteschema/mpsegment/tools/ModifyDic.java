/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.tools;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Administrator
 */
public class ModifyDic implements Serializable {

    public static void main(String args[]) {
        ModifyDic test = new ModifyDic();
        try {
//            test.loadSegDb(args[0]);//（删去财经，金融类相关词）修改后的词典的文件名
//            test.Save(args[1]);//重新生成的词典文件名，对应的是 segment.dic
            test.loadSegDb(args[0]);
            test.Save(args[1]);
        } catch (IOException ex) {
            Logger.getLogger(ModifyDic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public ArrayList<WordHead> getHeadindex() {
        return headindex;
    }

    public void setHeadindex(ArrayList<WordHead> headindex) {
        this.headindex = headindex;
    }

    public void loadSegDb(String fileName) throws IOException {
        loadSegDb(fileName, "utf-8");
    }

    /**
     * @param fileName 读入要修改的原始词表
     * @throws IOException
     */
    public void loadSegDb(String fileName, String enc) throws IOException {

        ArrayList<WordHead> headindex = new ArrayList<WordHead>();
        int totalWords1 = 0;
        try {
            FileInputStream fin = new FileInputStream(fileName);
            InputStreamReader fileIn = new InputStreamReader(fin, enc);//指定读文件格式“UTF-8”
            BufferedReader br = new BufferedReader(fileIn);

            String myreadline = "";    //定义一个String类型的变量,用来每次读取一行
            myreadline = br.readLine();
            String temp[] = myreadline.split("：");
            totalWords1 = Integer.parseInt(temp[1]);
            while (br.ready()) {
                myreadline = br.readLine();//读取一行
                if (myreadline.length() <= 0) {
                    continue;
                }
                Matcher m = pat.matcher(myreadline);
                if (m.matches()) {
                    {
                        WordHead wHead = new WordHead(m.group(1), Integer.parseInt(m.group(2)));
                        while ((myreadline = br.readLine()) != null) {
                            if (myreadline.length() <= 0) {
                                break;
                            }

//                            String str2[] = myreadline.split(":");
//                            if(myreadline.indexOf("::")>=0){
//                                System.out.println(myreadline);
//                            }
                            Matcher m2 = pat2.matcher(myreadline);
                            if (m2.matches()) {
                                WordHeadNode worditemp = new WordHeadNode(m2.group(1), Integer.parseInt(m2.group(2)), Integer.parseInt(m2.group(3)), Integer.parseInt(m2.group(4)));
                                wHead.addtemp(worditemp);
                            }
                        }
                        if (wHead.getWordOccuredSum() > wHead.getSizePre()) {
//                       System.out.println("有一个删去的信息：" + wHead.getHeadWord() + wHead.getSizePre());
//                        wHead.setSizePre(wHead.getSizePre());
                            wHead.setWordOccuredSum(wHead.getSizePre());
                        }
                        headindex.add(wHead);
                    }
                }
            }
//            把：单独加上
            WordHead wHead = new WordHead("：", 1);
            WordHeadNode worditemp = new WordHeadNode("：", 0, 23, 1);
            WordHead wHead1 = new WordHead(":", 1);
            WordHeadNode worditemp1 = new WordHeadNode("：", 0, 28, 1);
            wHead.addtemp(worditemp);
            wHead1.addtemp(worditemp1);
            headindex.add(wHead);
            headindex.add(wHead1);

            System.out.println(headindex.size() + ":=========headindex.size()");
            if (totalWords1 > headindex.size()) {
                totalWords1 = headindex.size();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setHeadindex(headindex);
        this.setTotalWords(totalWords1);

    }

    /**
     * @param 生成的词典名称 segment.dic2112
     * @throws IOException
     * 注意生成二进制格式的时候，不要用ObjectInputStream对象来写入，这种对象写入默认初始化的空间会影响到实际存值的位置。
     */
    public void Save(String s) throws IOException {
//        s = "result7.dic";
        try {
            int totalWords2 = this.getTotalWords();
//            System.out.println("注意： ====totalWords" + totalWords + "  没有对对象进行类型object，直接输出二进制文件");
            FileOutputStream obout = new FileOutputStream(s);
            byte abytetemp[] = integerToBytes(totalWords2);//写入总首字开头的词数
            obout.write(abytetemp);
            for (int i = 0; i < headindex.size(); i++) {
                byte abyte0[] = headindex.get(i).getHeadWord().getBytes("GBK");
                obout.write((byte) abyte0.length);
                obout.write(abyte0);
                abytetemp = integerToBytes(headindex.get(i).getWordOccuredSum());
                obout.write(abytetemp);
                ArrayList<WordHeadNode> temp = headindex.get(i).getWordItems();
                for (int j = 0; j < temp.size(); j++) {
                    byte abyte1[] = temp.get(j).wordName.getBytes("GBK");
                    obout.write((byte) abyte1.length);
                    obout.write(abyte1);
                    abytetemp = integerToBytes(temp.get(j).logFreq);
                    obout.write(abytetemp);
                    abytetemp = integerToBytes(temp.get(j).d);
                    obout.write(abytetemp);
                    abytetemp = integerToBytes(temp.get(j).wordPOSTable);
                    obout.write(abytetemp);
                }
            }
            obout.close();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println((new StringBuilder("Error: .save(")).append(s).append(") ").append(exception.getMessage()).toString());
        }

    }

    public static byte[] integerToBytes(int intValue) {
        byte[] byteValue = new byte[4];
        int shift = 0;

        for (int x = 0; x < 4; x++) {
            shift -= 8;
            byteValue[x] = (byte) (intValue >>> shift);
        }
        return byteValue;
    }
    private int totalWords; //公共有多少个首字开头的词
    private ArrayList<WordHead> headindex;//以首字开头的词
    private Pattern pat = Pattern.compile("首字：(.)：以首字开头的词总数：(.*)：");
    private Pattern pat2 = Pattern.compile("(.*):(.*):(.*):(.*):");
}
