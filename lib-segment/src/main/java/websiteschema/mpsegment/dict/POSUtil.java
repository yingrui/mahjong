package websiteschema.mpsegment.dict;

import java.util.HashMap;

public class POSUtil {

    public POSUtil() {
    }

    public static int getSize() {
        return posTable.size();
    }

    public static int getPOSIndex(String s) {
        int i = 44;
        if (s != null && s.length() > 0) {
            Integer integer = (Integer) posTable.get(s);
            if (integer != null) {
                i = integer.intValue();
            }
        }
        return i;
    }

    public static String getPOSString(int i) {
        return posString[i];
    }

    public static boolean isSubstantive(int i) {
        boolean flag = false;
        if (i <= 11 || i >= 21 && i <= 23 || i >= 27 && i <= 30 || i >= 40 && i <= 43 || i == 44 || i == 33) {
            flag = true;
        }
        return flag;
    }

    public static boolean isUnknown(int i) {
        boolean flag = false;
        if (i == 44) {
            flag = true;
        }
        return flag;
    }

    public static boolean isNWord(int i) {
        return i == 1 || i == 21 || i == 2 || i == 3 || i == 22 || i == 23 || i == 40 || i == 41 || i == 28 || i == 27 || i == 29 || i == 30 || i == 33 || i == 44 || i == 36;
    }

    public static boolean isVWord(int i) {
        return i == 9 || i == 34 || i == 40 || i == 42;
    }

    public static boolean isAWord(int i) {
        return i == 10 || i == 7 || i == 11 || i == 35 || i == 41 || i == 43;
    }

    public static boolean isDWord(int i) {
        return i == 12 || i == 42 || i == 43;
    }

    public static boolean isILWord(int i) {
        return i == 19 || i == 20;
    }
    public static final int POS_N = 1;
    public static final int POS_T = 2;
    public static final int POS_S = 3;
    public static final int POS_F = 4;
    public static final int POS_M = 5;
    public static final int POS_Q = 6;
    public static final int POS_B = 7;
    public static final int POS_R = 8;
    public static final int POS_V = 9;
    public static final int POS_A = 10;
    public static final int POS_Z = 11;
    public static final int POS_D = 12;
    public static final int POS_P = 13;
    public static final int POS_C = 14;
    public static final int POS_U = 15;
    public static final int POS_Y = 16;
    public static final int POS_E = 17;
    public static final int POS_O = 18;
    public static final int POS_I = 19;
    public static final int POS_L = 20;
    public static final int POS_J = 21;
    public static final int POS_H = 22;
    public static final int POS_K = 23;
    public static final int POS_G = 24;
    public static final int POS_X = 25;
    public static final int POS_W = 26;
    public static final int POS_NR = 27;
    public static final int POS_NS = 28;
    public static final int POS_NT = 29;
    public static final int POS_NZ = 30;
    public static final int POS_NX = 31;
    public static final int POS_NG = 33;
    public static final int POS_VG = 34;
    public static final int POS_AG = 35;
    public static final int POS_TG = 36;
    public static final int POS_DG = 37;
    public static final int POS_OG = 38;
    public static final int POS_AUX = 39;
    public static final int POS_VN = 40;
    public static final int POS_AN = 41;
    public static final int POS_VD = 42;
    public static final int POS_AD = 43;
    public static final int POS_UNKOWN = 44;
    public static HashMap posTable;
    public static String posString[];

    static {
        posTable = new HashMap(256);
        posString = new String[50];
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
        posString[1] = "N";
        posString[2] = "T";
        posString[3] = "S";
        posString[4] = "F";
        posString[5] = "M";
        posString[6] = "Q";
        posString[7] = "B";
        posString[8] = "R";
        posString[9] = "V";
        posString[10] = "A";
        posString[11] = "Z";
        posString[12] = "D";
        posString[13] = "P";
        posString[14] = "C";
        posString[15] = "U";
        posString[16] = "Y";
        posString[17] = "E";
        posString[18] = "O";
        posString[19] = "I";
        posString[20] = "L";
        posString[21] = "J";
        posString[22] = "H";
        posString[23] = "K";
        posString[24] = "G";
        posString[25] = "X";
        posString[26] = "W";
        posString[27] = "NR";
        posString[28] = "NS";
        posString[29] = "NT";
        posString[30] = "NZ";
        posString[31] = "NX";
        posString[33] = "NG";
        posString[34] = "VG";
        posString[35] = "AG";
        posString[36] = "TG";
        posString[37] = "DG";
        posString[38] = "OG";
        posString[40] = "VN";
        posString[41] = "AN";
        posString[42] = "VD";
        posString[43] = "AD";
        posString[39] = "AUX";
        posString[44] = "UN";
    }

    

}

/*

---- 附北大词性标注版本 ----
Ag
形语素
形容词性语素。形容词代码为a，语素代码ｇ前面置以A。

A
形容词
取英语形容词adjective的第1个字母。

AD
副形词
直接作状语的形容词。形容词代码a和副词代码d并在一起。

AN
名形词
具有名词功能的形容词。形容词代码a和名词代码n并在一起。

B
区别词
取汉字“别”的声母。

C
连词
取英语连词conjunction的第1个字母。

Dg
副语素
副词性语素。副词代码为d，语素代码ｇ前面置以D。

D
副词
取adverb的第2个字母，因其第1个字母已用于形容词。

E
叹词
取英语叹词exclamation的第1个字母。

F
方位词
取汉字“方”

G
语素
绝大多数语素都能作为合成词的“词根”，取汉字“根”的声母。

H
前接成分
取英语head的第1个字母。

I
成语
取英语成语idiom的第1个字母。

J
简称略语
取汉字“简”的声母。

K
后接成分
　
L
习用语
习用语尚未成为成语，有点“临时性”，取“临”的声母。

M
数词
取英语numeral的第3个字母，n，u已有他用。

Ng
名语素
名词性语素。名词代码为n，语素代码ｇ前面置以N。

N
名词
取英语名词noun的第1个字母。

Nr
人名
名词代码n和“人(ren)”的声母并在一起。

Ns
地名
名词代码n和处所词代码s并在一起。

Nt
机构团体
“团”的声母为t，名词代码n和t并在一起。

Nz
其他专名
“专”的声母的第1个字母为z，名词代码n和z并在一起。

O
拟声词
取英语拟声词onomatopoeia的第1个字母。

ba 介词 把、将 　
bei 介词 被 　


P
介词
取英语介词prepositional的第1个字母。

Q
量词
取英语quantity的第1个字母。

R
代词
取英语代词pronoun的第2个字母,因p已用于介词。

S
处所词
取英语space的第1个字母。

Tg
时语素
时间词性语素。时间词代码为t,在语素的代码g前面置以T。

T
时间词
取英语time的第1个字母。

dec 助词 的、之 　
deg 助词 得 　
di 助词 地 　
etc 助词 等、等等 　
as 助词 了、着、过 　
msp 助词 所 　

 U
其他助词
取英语助词auxiliary

Vg
动语素
动词性语素。动词代码为v。在语素的代码g前面置以V。

V
动词
取英语动词verb的第一个字母。

Vd
副动词
直接作状语的动词。动词和副词的代码并在一起。

Vn
名动词
指具有名词功能的动词。动词和名词的代码并在一起。

W
其他标点符号
　
X
非语素字
非语素字只是一个符号，字母x通常用于代表未知数、符号。

Y
语气词
取汉字“语”的声母。

Z
状态词
取汉字“状”的声母的前一个字母。
 * */