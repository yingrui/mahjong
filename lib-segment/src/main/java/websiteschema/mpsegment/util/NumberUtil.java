package websiteschema.mpsegment.util;

import java.text.DecimalFormat;
import java.util.HashMap;

public class NumberUtil {

    public static void initialize() {
        hm = new HashMap();
        for (int i = 0; i < digits.length; i++) {
            hm.put(digits[i], new Integer(i));
            hm.put(digits2[i], new Integer(i));
            hm.put(digits3[i], new Integer(i));
        }

        hm.put("贰", new Integer(2));
        hm.put("两", new Integer(2));
        hm.put("兩", new Integer(2));
        hm.put("叁", new Integer(3));
        hm.put("叄", new Integer(3));
        hm.put("陆", new Integer(6));
    }

    public static String toChineseNumber(long l) {
        int ai[] = new int[30];
        int i = 0;
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        StringBuilder stringBuilder = new StringBuilder();
        if (l == 0L) {
            return digits[0];
        }
        if (l < 0L) {
            flag1 = true;
            l = -l;
        }
        for (; Math.pow(10D, i) <= (double) l; i++) {
            int j = (int) (((double) l % Math.pow(10D, i + 1)) / Math.pow(10D, i));
            ai[i] = j;
            l = (long) ((double) l - (double) l % Math.pow(10D, i + 1));
        }

        for (int k = 0; k < i; k++) {
            if (k % 4 == 0) {
                if (ai[k] != 0) {
                    flag2 = false;
                    flag3 = true;
                    stringBuilder.insert(0, (new StringBuilder(String.valueOf(digits[ai[k]]))).append(afterWan[k / 4]).toString());
                } else if (k + 3 < i && ai[k + 3] != 0 || k + 2 < i && ai[k + 2] != 0 || k + 1 < i && ai[k + 1] != 0) {
                    stringBuilder.insert(0, afterWan[k / 4]);
                }
            } else if (ai[k] != 0) {
                flag2 = false;
                flag3 = true;
                if (i == 2 && k == 1 && ai[k] == 1) {
                    stringBuilder.insert(0, beforeWan[k % 4 - 1]);
                } else {
                    stringBuilder.insert(0, (new StringBuilder(String.valueOf(digits[ai[k]]))).append(beforeWan[k % 4 - 1]).toString());
                }
            } else if (flag3 && !flag2) {
                flag2 = true;
                stringBuilder.insert(0, digits[ai[k]]);
            }
        }

        if (flag1) {
            stringBuilder.insert(0, "负");
        }
        return stringBuilder.toString();
    }

    public static String toChineseNumber(String s) {
        String s1 = null;
        String s3 = "";
        String s6 = "";
        String s2 = s.replaceAll(",", "");
        int i = s2.indexOf(".");
        if (i < 0) {
            i = s2.indexOf("．");
        }
        if (i >= 0) {
            String s4 = s2.substring(0, i);
            long l = strToLong(s4, 0L);
            s4 = toChineseNumber(l);
            if (i < s2.length()) {
                String s5 = s2.substring(i + 1);
                if (s5 != null && s5.length() > 0) {
                    for (int j = 0; j < s5.length(); j++) {
                        s6 = (new StringBuilder(String.valueOf(s6))).append(getChDigital(s5.charAt(j))).toString();
                    }

                }
            }
            if (s6.length() > 0) {
                s1 = (new StringBuilder(String.valueOf(s4))).append("点").append(s6).toString();
            } else {
                s1 = s4;
            }
        } else {
            long l1 = strToLong(s2, 0L);
            s1 = toChineseNumber(l1);
        }
        return s1;
    }

    public static long strToLong(String s, long l) {
        long l1 = l;
        try {
            l1 = Long.parseLong(s);
        } catch (Exception exception) {
        }
        return l1;
    }

    public static double getPower(int i) {
        double d = 1.0D;
        if (i > 0) {
            for (int j = 0; j < i; j++) {
                d *= 10D;
            }

        } else {
            i = -i;
            for (int k = 0; k < i; k++) {
                d /= 10D;
            }

        }
        return d;
    }

    public static String getChDigital(char c) {
        int i = c - 48;
        if (i >= 0 && i <= 9) {
            return digits[i];
        } else {
            return "";
        }
    }

    public static boolean isDefined(String s) {
        if (hm == null) {
            initialize();
        }
        return hm.containsKey(s);
    }

    public static int isDigital(String s) {
        Integer integer = (Integer) hm.get(s);
        if (integer != null) {
            return integer.intValue();
        } else {
            return 0;
        }
    }

    public static boolean isChineseDigitalStr(String chineseNumberStr) {
        boolean flag = false;
        if (chineseNumberStr.length() <= 0) {
            flag = false;
        } else {
            for (int i = 0; i < chineseNumberStr.length(); i++) {
                if (!isDefined(chineseNumberStr.substring(i, i + 1))) {
                    continue;
                }
                flag = true;
                break;
            }

        }
        return flag;
    }

    public static String chineseToEnglishNumberStr(String chineseNumberStr) {
        String s1 = "";
        if (chineseNumberStr != null && chineseNumberStr.indexOf("分之") > 0) {
            s1 = chineseNumberStr.substring(0, chineseNumberStr.indexOf("分之") + 2);
            chineseNumberStr = chineseNumberStr.substring(s1.length());
        }
        if (chineseNumberStr != null && chineseNumberStr.indexOf("第") == 0) {
            s1 = chineseNumberStr.substring(0, 1);
            chineseNumberStr = chineseNumberStr.substring(1);
        }
        if (chineseNumberStr == null || chineseNumberStr.length() < 1) {
            return "";
        }
        if (!isChineseDigitalStr(chineseNumberStr)) {
            return "";
        }
        if (chineseNumberStr.indexOf("-") > 0 || chineseNumberStr.indexOf("－") > 0) {
            return "";
        }
        String s2 = chinesePositiveRealNumberToEnglishNumber(chineseNumberStr);
        if (s1.length() > 0) {
            s2 = (new StringBuilder(String.valueOf(s1))).append(s2).toString();
        }
        String s3 = chineseNumberStr.substring(chineseNumberStr.length() - 1);
        if (!isDefined(s3) && "〇零十拾百佰千仟万亿兆.．".indexOf(s3) < 0) {
            s2 = (new StringBuilder(String.valueOf(s2))).append(s3).toString();
        }
        return s2;
    }

    public static String chinesePositiveRealNumberToEnglishNumber(String positiveRealNumberStr) {
        double number = chineseToEnglishNumber(positiveRealNumberStr);
        if (number == 0.0D && (!positiveRealNumberStr.equals("零") || !positiveRealNumberStr.equals("〇") || !positiveRealNumberStr.equals("０"))) {
            return "";
        }
        DecimalFormat decimalformat;
        if (containsDot(positiveRealNumberStr)) {
            decimalformat = new DecimalFormat("###.#########");
        } else {
            decimalformat = new DecimalFormat("###");
        }
        return decimalformat.format(number);
    }

    private static boolean containsDot(String str) {
        return str.contains("点") || str.contains(".") || str.contains("．") || str.contains("·");
    }

    public static double chineseToEnglishNumber(String s) {
        boolean flag = false;
        int j = 0;

        boolean flag1 = false;
        int k = 0;
        double d = 0.0D;
        double d1 = 0.0D;
        long l = 0L;
        s = s.replaceAll("亿万", "兆");
        s = s.replaceAll("万亿", "兆");
        s = s.replaceAll("万万", "亿");
        s = s.replaceAll("廿", "二十");
        s = s.replaceAll("卄", "二十");
        s = s.replaceAll("卅", "三十");
        s = s.replaceAll("卌", "四十");
        for (int i = s.length(); j < i; j++) {
            char c = s.charAt(j);
            if (j == 0 && (c == '-' || c == '负')) {
                flag1 = true;
            } else if (j != 0 || c != '第') {
                if (c == '点' || c == '.' || c == '．') {
                    l = 1L;
                    k = -1;
                } else {
                    if (c == '兆') {
                        k = 12;
                        if (d == 0.0D) {
                            d = 1.0D;
                        }
                        d1 += d * getPower(k);
                        d = 0.0D;
                        k -= 4;
                    }
                    if (c == '亿') {
                        k = 8;
                        if (d == 0.0D) {
                            d = 1.0D;
                        }
                        d1 += d * getPower(k);
                        d = 0.0D;
                        k -= 4;
                    }
                    if (c == '万') {
                        k = 4;
                        if (d == 0.0D) {
                            d = 1.0D;
                        }
                        d1 += d * getPower(k);
                        d = 0.0D;
                        k -= 4;
                    }
                    if (c == '千' || c == '仟') {
                        d += 1000D;
                    }
                    if (c == '百' || c == '佰') {
                        d += 100D;
                    }
                    if (c == '十' || c == '拾') {
                        d += 10D;
                    }
                    if (c == '零' || c == '〇' || c == '0' || c == '０') {
                        k = 0;
                    } else {
                        String s1 = s.substring(j, j + 1);
                        if (isDefined(s1)) {
                            int i1 = isDigital(s1);
                            if (l > 0L) {
                                d += (double) i1 * getPower(k);
                                k--;
                                for (; j + 1 < i && isDefined(s.substring(j + 1, j + 2)); j++) {
                                    i1 = isDigital(s.substring(j + 1, j + 2));
                                    d += (double) i1 * getPower(k);
                                    k--;
                                }

                            } else if (j + 1 < i) {
                                char c1 = s.charAt(j + 1);
                                if (c1 == '十' || c1 == '拾') {
                                    d += i1 * 10;
                                    j++;
                                } else if (c1 == '百' || c1 == '佰') {
                                    d += i1 * 100;
                                    j++;
                                } else if (c1 == '千' || c1 == '仟') {
                                    d += i1 * 1000;
                                    j++;
                                } else if (isDefined(s.substring(j + 1, j + 2))) {
                                    int j1 = 0;
                                    j1 += i1;
                                    for (; j + 1 < i && isDefined(s.substring(j + 1, j + 2)); j++) {
                                        j1 *= 10;
                                        j1 += isDigital(s.substring(j + 1, j + 2));
                                    }

                                    d += j1;
                                } else {
                                    d += i1;
                                }
                            } else if (j + 1 == i && j > 0) {
                                char c2 = s.charAt(j - 1);
                                if (c2 == '兆') {
                                    d += (double) i1 * getPower(11);
                                } else if (c2 == '亿') {
                                    d += (double) i1 * getPower(7);
                                } else if (c2 == '万') {
                                    d += i1 * 1000;
                                } else if (c2 == '千' || c2 == '仟') {
                                    d += i1 * 100;
                                } else if (c2 == '百' || c2 == '佰') {
                                    d += i1 * 10;
                                } else {
                                    d += i1;
                                }
                            } else {
                                d += i1;
                            }
                        }
                    }
                }
            }
        }

        d1 += d;
        if (flag1) {
            d1 = -d1;
        }
        return d1;
    }
    public static final String minus = "负";
    public static final String digits[] = {
        "零", "一", "二", "三", "四", "五", "六", "七", "八", "九"
    };
    public static final String digits2[] = {
        "０", "１", "２", "３", "４", "５", "６", "７", "８", "９"
    };
    public static final String digits3[] = {
        "〇", "壹", "貳", "參", "肆", "伍", "陸", "柒", "捌", "玖"
    };
    public static final String beforeWan[] = {
        "十", "百", "千"
    };
    public static final String afterWan[] = {
        "", "万", "亿", "兆"
    };
    public static final String ALTTWO = "两";
    public static final long TEN = 10L;
    public static HashMap hm = null;
}
