package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.dict.POSUtil;
import websiteschema.mpsegment.util.NumberUtil;

import static websiteschema.mpsegment.util.WordUtil.isNumerical;

public class NumberAndTimeFilter extends AbstractSegmentFilter {

    @Override
    public void doFilter() {
        int length = segmentResult.length();

        for (int wordI = 0; wordI < length; wordI++) {
            if (segmentResult.getPOS(wordI) == posUN) {
                int j1 = isNumerical(segmentResult.getWord(wordI));
                if (j1 == 1) {
                    segmentResult.setPOS(wordI, posM);
                }
            }
            if (segmentResult.getPOS(wordI) == posNR && wuLiuWan.indexOf(segmentResult.getWord(wordI)) >= 0) {
                if (wordI > 0) {
                    if (segmentResult.getPOS(wordI - 1) == posM || wuLiuWan.indexOf(segmentResult.getWord(wordI - 1)) >= 0) {
                        segmentResult.setPOS(wordI, posM);
                    } else if (wordI + 1 < length && (segmentResult.getPOS(wordI + 1) == posM || wuLiuWan.indexOf(segmentResult.getWord(wordI + 1)) >= 0)) {
                        segmentResult.setPOS(wordI, posM);
                    }
                } else if (length > 1 && (segmentResult.getPOS(wordI + 1) == posM || wuLiuWan.indexOf(segmentResult.getWord(wordI + 1)) >= 0)) {
                    segmentResult.setPOS(wordI, posM);
                }
            }
        }

        int numBegin = -1;
        int numEnd = -1;
        boolean isChineseNum = false;
        for (int k2 = 0; k2 < length; k2++) {
            if (wordPosIndexes[k2] <= 0) {
                if (segmentResult.getPOS(k2) == posUN) {
                    if (segmentResult.getWord(k2).equals("-") || segmentResult.getWord(k2).equals("－")) {
                        segmentResult.setPOS(k2, posM);
                    } else {
                        int l1 = isNumerical(segmentResult.getWord(k2));
                        if (l1 == 1) {
                            segmentResult.setPOS(k2, posM);
                        }
                    }
                }
                if (isChineseNumber(k2)) {
                    if (numBegin < 0) {
                        numBegin = k2;
                        isChineseNum = true;
                    } else {
                        numEnd = k2;
                    }
                } else if (isChineseNum) {
                    if (!isNumberSeparator(k2)) {
                        isChineseNum = false;
                        if (numEnd - numBegin >= 1) {
                            mergeWordsWithPOS(numBegin, numEnd, posM);
                        }
                        numBegin = -1;
                        numEnd = -1;
                    } else {
                        numEnd = k2;
                        if (k2 + 1 == length && numEnd - numBegin >= 1) {
                            mergeWordsWithPOS(numBegin, numEnd, posM);
                        }
                    }
                }
                if (recognizeTime && segmentResult.getWord(k2).length() == 1 && k2 > 0 && (segmentResult.getWord(k2).equals("年") || segmentResult.getWord(k2).equals("月") || segmentResult.getWord(k2).equals("日") || segmentResult.getWord(k2).equals("时") || segmentResult.getWord(k2).equals("分") || segmentResult.getWord(k2).equals("秒"))) {
                    if (segmentResult.getWord(k2).equals("年")) {
                        if (segmentResult.getPOS(k2 - 1) == posM && (segmentResult.getWord(k2 - 1).length() > 2 || wordPosIndexes[k2 - 1] > 0) && (wordPosIndexes[k2 - 1] <= 0 || segmentResult.getWord(k2 - 1).length() != 1 || k2 - 3 > 0 && (k2 - 3 < 0 || wordPosIndexes[k2 - 3] > 0))) {
                            mergeWordsWithPOS(k2 - 1, k2, posT);
                        }
                    } else if (segmentResult.getPOS(k2 - 1) == posM) {
                        mergeWordsWithPOS(k2 - 1, k2, posT);
                    }
                }
            }
        }

        if (numEnd > 0 && numEnd == length - 1 && numBegin >= 0 && numEnd - numBegin >= 1) {
            mergeWordsWithPOS(numBegin, numEnd, posM);
        }
    }

    private boolean isChineseNumber(int wordIndex) {
        boolean flag = false;
        if (segmentResult.getPOS(wordIndex) == posM) {
            if (segmentResult.getWord(wordIndex).length() == 1) {
                if (!segmentResult.getWord(wordIndex).equals("多") && !segmentResult.getWord(wordIndex).equals("余")) {
                    flag = true;
                }
            } else if (segmentResult.getWord(wordIndex).indexOf("分之") >= 0) {
                flag = true;
            } else if (isNumerical(segmentResult.getWord(wordIndex)) == 1 || NumberUtil.isChineseDigitalStr(segmentResult.getWord(wordIndex))) {
                flag = true;
            } else if (segmentResult.getWord(wordIndex).equals("几十") || segmentResult.getWord(wordIndex).equals("几百") || segmentResult.getWord(wordIndex).equals("几千") || segmentResult.getWord(wordIndex).equals("几亿") || segmentResult.getWord(wordIndex).equals("几万") || segmentResult.getWord(wordIndex).equals("千万") || segmentResult.getWord(wordIndex).equals("百万") || segmentResult.getWord(wordIndex).equals("上百") || segmentResult.getWord(wordIndex).equals("上千") || segmentResult.getWord(wordIndex).equals("数十") || segmentResult.getWord(wordIndex).equals("数百") || segmentResult.getWord(wordIndex).equals("数千") || segmentResult.getWord(wordIndex).equals("好几十") || segmentResult.getWord(wordIndex).equals("好几百") || segmentResult.getWord(wordIndex).equals("好几千") || segmentResult.getWord(wordIndex).equals("一个")) {
                flag = true;
            }
        }
        return flag;
    }

    private boolean isNumberSeparator(int wordIndex) {
        boolean flag = false;
        int length = segmentResult.length();
        String s1 = segmentResult.getWord(wordIndex);
        if (s1.length() == 1) {
            if (s1.equals(".") || s1.equals("．")) {
                if (wordIndex + 1 < length && (segmentResult.getPOS(wordIndex + 1) == posM || segmentResult.getPOS(wordIndex + 1) == posUN) && isNumerical(segmentResult.getWord(wordIndex + 1)) == 1) {
                    flag = true;
                }
            } else if (s1.equals("点") || s1.equals("/") || s1.equals("／")) {
                if (wordIndex + 1 < length && (segmentResult.getPOS(wordIndex + 1) == posM || segmentResult.getPOS(wordIndex + 1) == posUN)) {
                    flag = true;
                }
            } else if (wordIndex + 1 < length) {
                if (s1.equals("%") || s1.equals("％") || s1.equals("‰")) {
                    flag = true;
                }
            } else {
                flag = false;
                if (s1.equals("%") || s1.equals("％")) {
                    flag = true;
                }
            }
        }
        return flag;
    }
    private static int posM = POSUtil.POS_M;
    private static int posUN = POSUtil.POS_UNKOWN;
    private static int posT = POSUtil.POS_T;
    private static int posNR = POSUtil.POS_NR;
    private static String wuLiuWan = "伍陆万";
    private boolean recognizeTime = true;
}
