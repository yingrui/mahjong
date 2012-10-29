/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.util;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.dict.POSUtil;

/**
 *
 * @author ray
 */
public class WordUtil {

    public static int isNumerical(String wordString) {
        byte byte0 = 1;
        if (wordString.length() <= 0) {
            byte0 = 2;
        } else {
            for (int i1 = 0; i1 < wordString.length(); i1++) {
                if (Character.isDigit(wordString.charAt(i1))) {
                    continue;
                }
                byte0 = 2;
                break;
            }
        }
        return byte0;
    }

    public static boolean isCharaterOrDigit(char ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9';
    }

    public static boolean isAlphaNumericWithUnderScore(String wordString) {
        boolean flag = true;
        for (int i = 0; i < wordString.length(); i++) {
            char c1 = wordString.charAt(i);
            if (isCharaterOrDigit(c1) || c1 == '_') {
                continue;
            }
            flag = false;
            break;
        }

        return flag;
    }

    public static boolean isAlphaNumericWithUnderScore_Slash_Colon(String wordString) {
        boolean flag = true;
        for (int i = 0; i < wordString.length(); i++) {
            char ch = wordString.charAt(i);
            if (isCharaterOrDigit(ch) || ch == '_' || ch == '/' || ch == ':') {
                continue;
            }
            flag = false;
            break;
        }

        return flag;
    }

    public static boolean isLetterOrDigitWithUnderscore(String wordString) {
        boolean flag = true;
        for (int i = 0; i < wordString.length(); i++) {
            char ch = wordString.charAt(i);
            if (Character.isLetterOrDigit(ch) || ch == '_') {
                continue;
            }
            flag = false;
            break;
        }

        return flag;
    }

    public static boolean isPos_P_C_U_W_UN(int POS) {
        return POS == POSUtil.POS_P || POS == POSUtil.POS_C || POS == POSUtil.POS_U || POS == POSUtil.POS_W || POS == POSUtil.POS_UNKOWN;
    }

    public static boolean isChineseJieCi(String wordString) {
        return wordString.equals("向") || wordString.equals("和") || wordString.equals("丁") || wordString.equals("自") || wordString.equals("若") || wordString.equals("于") || wordString.equals("同") || wordString.equals("为") || wordString.equals("以") || wordString.equals("连") || wordString.equals("从") || wordString.equals("得") || wordString.equals("则");
    }

    public static boolean isLeftOrRightBraceOrColonOrSlash(String ch) {
        boolean flag = false;
        if (glueChars.indexOf(ch) >= 0 && glueChar.indexOf(ch) < 0) {
            flag = true;
        }
        return flag;
    }

    public static boolean isSpecialMingChar(String ch) {
        return ch.equals("向") || ch.equals("自") || ch.equals("乃") || ch.equals("以") || ch.equals("从") || ch.equals("和") || ch.equals("得") || ch.equals("为") || ch.equals("则") || ch.equals("如");
    }
    private static String glueChars = "*?~/_[]:";
    private static String glueChar = MPSegmentConfiguration.getINSTANCE().getGlueChar();//"~_:";
}
