package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.dict.POSUtil;

import static websiteschema.mpsegment.util.WordUtil.*;

public final class QuerySyntaxFilter
        extends AbstractSegmentFilter {

    @Override
    public void doFilter() {
        if (supportQuerySyntax && segmentResult.length() <= maxQueryLength) {
            int length = segmentResult.length();
            int wordI = 0;

            for (; wordI < length; wordI++) {
                if (wordPosIndexes[wordI] <= 0) {
                    int pos = segmentResult.getPOS(wordI);
                    if (pos == posUN || pos == posW) {
                        String s1 = segmentResult.getWord(wordI);
                        if (s1.length() == 1 && !isLeftOrRightBraceOrColonOrSlash(s1)) {
                            if (s1.equals(leftBrace)) {
                                if (wordI + 2 < length && rightBrace.equals(segmentResult.getWord(wordI + 2))) {
                                    int k1 = isNumerical(segmentResult.getWord(wordI + 1));
                                    if (k1 == 1) {
                                        int i2;
                                        if (wordI >= 1) {
                                            i2 = wordI - 1;
                                            if (segmentResult.getWord(i2).equals(tilda)) {
                                                i2--;
                                            }
                                        } else {
                                            i2 = wordI;
                                        }
                                        int l2 = wordI + 2;
                                        mergeWordsWithPOS_UN(i2, l2);
                                    }
                                }
                            } else if (s1.equals(questionMark) || s1.equals(star)) {
                                if (wordI >= 1) {
                                    int k3 = wordI;
                                    int j4 = wordI;
                                    if (wordI + 1 < length) {
                                        if (isLetterOrDigitWithUnderscore(segmentResult.getWord(wordI + 1))) {
                                            j4 = wordI + 1;
                                        }
                                        if (isLetterOrDigitWithUnderscore(segmentResult.getWord(wordI - 1))) {
                                            k3 = wordI - 1;
                                        }
                                        mergeWordsWithPOS_UN(k3, j4);
                                    } else if (isLetterOrDigitWithUnderscore(segmentResult.getWord(wordI - 1))) {
                                        mergeWordsWithPOS_UN(wordI - 1, wordI);
                                    }
                                } else if (wordI + 1 < length && isLetterOrDigitWithUnderscore(segmentResult.getWord(wordI + 1))) {
                                    mergeWordsWithPOS_UN(wordI, wordI + 1);
                                }
                            } else if (s1.equals(colon)) {
                                if (wordI > 0 && wordI + 1 < length && isAlphaNumericWithUnderScore(segmentResult.getWord(wordI + 1))) {
                                    mergeWordsWithPOS_UN(wordI - 1, wordI + 1);
                                }
                            } else if (s1.equals(underScore)) {
                                int j2;
                                if (wordI > 1 && isAlphaNumericWithUnderScore(segmentResult.getWord(wordI - 1))) {
                                    j2 = wordI - 1;
                                } else {
                                    j2 = wordI;
                                }
                                int i3 = wordI;
                                for (int l3 = wordI + 1; l3 < length; l3++) {
                                    if (!isAlphaNumericWithUnderScore(segmentResult.getWord(l3))) {
                                        break;
                                    }
                                    i3 = l3;
                                }

                                if (i3 > j2) {
                                    mergeWordsWithPOS_UN(j2, i3);
                                }
                            } else if (s1.equals(slash)) {
                                int k2;
                                if (wordI > 1 && isAlphaNumericWithUnderScore_Slash_Colon(segmentResult.getWord(wordI - 1))) {
                                    k2 = wordI - 1;
                                } else {
                                    k2 = wordI;
                                }
                                int j3 = wordI;
                                for (int i4 = wordI + 1; i4 < length; i4++) {
                                    if (!isAlphaNumericWithUnderScore_Slash_Colon(segmentResult.getWord(i4))) {
                                        break;
                                    }
                                    j3 = i4;
                                }

                                if (j3 > k2) {
                                    mergeWordsWithPOS_UN(k2, j3);
                                }
                            } else if (s1.equals(tilda) && wordI + 1 < length && isLetterOrDigitWithUnderscore(segmentResult.getWord(wordI + 1))) {
                                mergeWordsWithPOS_UN(wordI, wordI + 1);
                            }
                        }
                    }
                }
            }
        }
    }

    private void mergeWordsWithPOS_UN(int firstWordIndex, int secondWordIndex) {
        mergeWordsWithPOS(firstWordIndex, secondWordIndex, posUN);
    }
    private static int posUN = POSUtil.POS_UNKOWN;
    private static int posW = POSUtil.POS_W;
    private static String leftBrace = "[";
    private static String rightBrace = "]";
    private static String questionMark = "?";
    private static String star = "*";
    private static String tilda = "~";
    private static String underScore = "_";
    private static String slash = "/";
    private static String colon = ":";
    private boolean supportQuerySyntax = MPSegmentConfiguration.getINSTANCE().isSupportQuerySyntax();
    private int maxQueryLength = MPSegmentConfiguration.getINSTANCE().getMaxQueryLength();
}
