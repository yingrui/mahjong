package websiteschema.mpsegment.util;

/**
 * Created with IntelliJ IDEA.
 * User: twer
 * Date: 9/22/12
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil {

    public static char halfShape(final char original) {
        if (isFullShapedChar(original)) {
            return (char) (original - 65248);
        }

        return original;
    }

    public static boolean isFullShapedChar(char original) {
        return (original >= '\uFF21' && original <= '\uFF41' || original >= '\uFF3A' && original <= '\uFF5A' || original >= '\uFF10' && original <= '\uFF19');
    }

    public static String halfShape(final String original) {
        char chArray[] = original.toCharArray();
        for (int i1 = 0; i1 < chArray.length; i1++) {
            if (isCharAlphabeticalOrDigital(chArray[i1])) {
                chArray[i1] = StringUtil.halfShape(chArray[i1]);
            }
        }

        return new String(chArray);
    }

    public static String toUpperCase(final String original) {
        char chArray[] = original.toCharArray();
        for (int i1 = 0; i1 < chArray.length; i1++) {
            if (isCharAlphabeticalOrDigital(chArray[i1])) {
                chArray[i1] = StringUtil.toUpperCase(chArray[i1]);
            }
        }

        return new String(chArray);
    }

    public static char toUpperCase(char original) {
        if (isLowerCaseChar(original)) {
            return (char) (original - 32);
        }
        return original;
    }

    private static boolean isLowerCaseChar(char original) {
        return original >= 'a' && original <= 'z';
    }

    public static String doUpperCaseAndHalfShape(String original) {
        char chArray[] = original.toCharArray();
        for (int i1 = 0; i1 < chArray.length; i1++) {
            if (isCharAlphabeticalOrDigital(chArray[i1])) {
                chArray[i1] = StringUtil.halfShape(chArray[i1]);
                chArray[i1] = StringUtil.toUpperCase(chArray[i1]);
            }
        }

        return new String(chArray);
    }

    public static boolean isCharAlphabeticalOrDigital(char ch) {
        if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch >= '0' && ch <= '9' || ch >= '\uFF21' && ch <= '\uFF41' || ch >= '\uFF3A' && ch <= '\uFF5A' || ch >= '\uFF10' && ch <= '\uFF19') {
            return true;
        }
        return false;
    }

}
