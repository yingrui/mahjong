//package websiteschema.mpsegment.filter;
//
//import websiteschema.mpsegment.dict.POSUtil;
//import websiteschema.mpsegment.util.NumberUtil;
//
//import static websiteschema.mpsegment.util.WordUtil.isNumerical;
//
//class NumberAndTimeFilter extends AbstractSegmentFilter {
//
//    override def doFilter() {
//        var length = segmentResult.length()
//
//        recognizeChineseNumber(length);
//
//        searchAndMarkNumberWords(length);
//    }
//
//    private def searchAndMarkNumberWords(length: Int) {
//        var numBegin = -1
//        var numEnd = -1
//        var isLastWordChineseNum = false
//        for (Int index = 0; index < length; index++) {
//            if (isNotMarked(index)) {
//                if (segmentResult.getPOS(index) == posUN) {
//                    if (segmentResult.getWord(index).equals("-") || segmentResult.getWord(index).equals("－")) {
//                        segmentResult.setPOS(index, posM);
//                    } else {
//                        var l1 = isNumerical(segmentResult.getWord(index))
//                        if (l1 == 1) {
//                            segmentResult.setPOS(index, posM);
//                        }
//                    }
//                }
//
//                var isCurrentWordChineseNumber = isChineseNumber(index)
//                if (isCurrentWordChineseNumber) {
//                    if (isEndOfNumber(index)) {
//                        if (numBegin > 0) {
//                            setWordIndexesAsNumberForMerge(numBegin, numEnd);
//                        }
//                    } else if (numBegin < 0) {
//                        numBegin = index;
//                        isLastWordChineseNum = true;
//                    } else {
//                        numEnd = index;
//                    }
//                }
//
//                if (!isCurrentWordChineseNumber && isLastWordChineseNum) {
//                    if (!isNumberSeparator(index)) {
//                        isLastWordChineseNum = false;
//                        setWordIndexesAsNumberForMerge(numBegin, numEnd);
//                        numBegin = -1;
//                        numEnd = -1;
//                    } else {
//                        numEnd = index;
//                        if (index + 1 == length) {
//                            setWordIndexesAsNumberForMerge(numBegin, numEnd);
//                        }
//                    }
//                }
//                recognizeTime(index);
//            }
//        }
//
//        if (numEnd > 0 && numEnd == length - 1 && numBegin >= 0 && numEnd - numBegin >= 1) {
//            setWordIndexesAndPOSForMerge(numBegin, numEnd, posM);
//        }
//    }
//
//    private def setWordIndexesAsNumberForMerge(numBegin: Int, numEnd: Int) {
//        if (numEnd - numBegin >= 1) {
//            setWordIndexesAndPOSForMerge(numBegin, numEnd, posM);
//        }
//    }
//
//    private def recognizeTime(index: Int) {
//        if (recognizeTime && segmentResult.getWord(index).length() == 1 && index > 0 && isTimeSuffix(index)) {
//            if (segmentResult.getWord(index).equals("年")) {
//                if (segmentResult.getPOS(index - 1) == posM && (segmentResult.getWord(index - 1).length() > 2 || isWordConfirmed(index - 1)) && (isNotMarked(index - 1) || segmentResult.getWord(index - 1).length() != 1 || index - 3 > 0 && (index - 3 < 0 || isWordConfirmed(index - 3)))) {
//                    setWordIndexesAndPOSForMerge(index - 1, index, posT);
//                }
//            } else if (segmentResult.getPOS(index - 1) == posM) {
//                setWordIndexesAndPOSForMerge(index - 1, index, posT);
//            }
//        }
//    }
//
//    private def isEndOfNumber(index: Int) : Boolean = {
//        return segmentResult.getWord(index).endsWith("个");
//    }
//
//    private def isTimeSuffix(index: Int) : Boolean = {
//        return segmentResult.getWord(index).equals("年")
//                || segmentResult.getWord(index).equals("月")
//                || segmentResult.getWord(index).equals("日")
//                || segmentResult.getWord(index).equals("时")
//                || segmentResult.getWord(index).equals("分")
//                || segmentResult.getWord(index).equals("秒");
//    }
//
//    private def recognizeChineseNumber(length: Int) {
//        for (Int wordI = 0; wordI < length; wordI++) {
//            if (segmentResult.getPOS(wordI) == posUN) {
//                var j1 = isNumerical(segmentResult.getWord(wordI))
//                if (j1 == 1) {
//                    segmentResult.setPOS(wordI, posM);
//                }
//            }
//            if (segmentResult.getPOS(wordI) == posNR && wuLiuWan.indexOf(segmentResult.getWord(wordI)) >= 0) {
//                if (wordI > 0) {
//                    if (segmentResult.getPOS(wordI - 1) == posM || wuLiuWan.indexOf(segmentResult.getWord(wordI - 1)) >= 0) {
//                        segmentResult.setPOS(wordI, posM);
//                    } else if (wordI + 1 < length && (segmentResult.getPOS(wordI + 1) == posM || wuLiuWan.indexOf(segmentResult.getWord(wordI + 1)) >= 0)) {
//                        segmentResult.setPOS(wordI, posM);
//                    }
//                } else if (length > 1 && (segmentResult.getPOS(wordI + 1) == posM || wuLiuWan.indexOf(segmentResult.getWord(wordI + 1)) >= 0)) {
//                    segmentResult.setPOS(wordI, posM);
//                }
//            }
//        }
//    }
//
//    private def isChineseNumber(wordIndex: Int) : Boolean = {
//        var flag = false
//        if (segmentResult.getPOS(wordIndex) == posM) {
//            if (segmentResult.getWord(wordIndex).length() == 1) {
//                if (!segmentResult.getWord(wordIndex).equals("多") && !segmentResult.getWord(wordIndex).equals("余")) {
//                    flag = true;
//                }
//            } else if (segmentResult.getWord(wordIndex).indexOf("分之") >= 0) {
//                flag = true;
//            } else if (isNumerical(segmentResult.getWord(wordIndex)) == 1 || NumberUtil.isChineseDigitalStr(segmentResult.getWord(wordIndex))) {
//                flag = true;
//            } else if (segmentResult.getWord(wordIndex).equals("几十") || segmentResult.getWord(wordIndex).equals("几百") || segmentResult.getWord(wordIndex).equals("几千") || segmentResult.getWord(wordIndex).equals("几亿") || segmentResult.getWord(wordIndex).equals("几万") || segmentResult.getWord(wordIndex).equals("千万") || segmentResult.getWord(wordIndex).equals("百万") || segmentResult.getWord(wordIndex).equals("上百") || segmentResult.getWord(wordIndex).equals("上千") || segmentResult.getWord(wordIndex).equals("数十") || segmentResult.getWord(wordIndex).equals("数百") || segmentResult.getWord(wordIndex).equals("数千") || segmentResult.getWord(wordIndex).equals("好几十") || segmentResult.getWord(wordIndex).equals("好几百") || segmentResult.getWord(wordIndex).equals("好几千") || segmentResult.getWord(wordIndex).equals("一个")) {
//                flag = true;
//            }
//        }
//        return flag;
//    }
//
//    private def isNumberSeparator(wordIndex: Int) : Boolean = {
//        var flag = false
//        var length = segmentResult.length()
//        var wordStr = segmentResult.getWord(wordIndex)
//        if (wordStr.length() == 1) {
//            if (wordStr.equals(".") || wordStr.equals("．")) {
//                if (wordIndex + 1 < length && (segmentResult.getPOS(wordIndex + 1) == posM || segmentResult.getPOS(wordIndex + 1) == posUN) && isNumerical(segmentResult.getWord(wordIndex + 1)) == 1) {
//                    flag = true;
//                }
//            } else if (wordStr.equals("点") || wordStr.equals("/") || wordStr.equals("／")) {
//                if (wordIndex + 1 < length && (segmentResult.getPOS(wordIndex + 1) == posM || segmentResult.getPOS(wordIndex + 1) == posUN)) {
//                    flag = true;
//                }
//            } else if (wordIndex + 1 < length) {
//                if (wordStr.equals("%") || wordStr.equals("％") || wordStr.equals("‰")) {
//                    flag = true;
//                }
//            } else {
//                flag = false;
//                if (wordStr.equals("%") || wordStr.equals("％")) {
//                    flag = true;
//                }
//            }
//        }
//        return flag;
//    }
//
//    private static Int posM = POSUtil.POS_M;
//    private static Int posUN = POSUtil.POS_UNKOWN;
//    private static Int posT = POSUtil.POS_T;
//    private static Int posNR = POSUtil.POS_NR;
//    private static String wuLiuWan = "伍陆万";
//    private var recognizeTime : Boolean = true;
//}
