package websiteschema.mpsegment.tools;

import java.io.Serializable;
import java.util.ArrayList;

class WordHeadNode implements Serializable {

    public String wordName;//当前词
    public int logFreq;// 默认是0，只有领域词才为1
    public int d;// 对应词性数组中的维数，通过d去另外词典中找对应的词性，不知道删去能否影响性能？
    public int wordPOSTable; //这里对应当前词总共有多少种词性，与posfree词典相对应

    WordHeadNode(String str, int i, int j, int k) {
        this.wordName = str;
        this.logFreq = i;
        this.d = j;
        this.wordPOSTable = k;
    }
}


