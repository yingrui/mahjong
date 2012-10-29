// Source File Name:   CharCheckUtils.java
package websiteschema.mpsegment.util;

public class CharCheckUtil {

    public static boolean isChinese(char c) {
        Character.UnicodeBlock unicodeblock = Character.UnicodeBlock.of(c);
        return unicodeblock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;
    }

    public static boolean isChinese(String s) {
        boolean flag = true;
        char ac[] = s.toCharArray();
        for (int i = 0; i < ac.length; i++) {
            char c = ac[i];
            if (isChinese(c)) {
                continue;
            }
            flag = false;
            break;
        }

        return flag;
    }

    public static boolean isSymbol(char c) {
        int i = Character.getType(c);
        return i == 24 || i == 25 || i == 21 || i == 22 || i == 27 || i == 23 || i == 26 || i == 20;
    }
}
