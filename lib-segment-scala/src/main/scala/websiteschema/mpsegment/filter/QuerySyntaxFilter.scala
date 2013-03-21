//package websiteschema.mpsegment.filter
//
//import websiteschema.mpsegment.conf.MPSegmentConfiguration
//import websiteschema.mpsegment.dict.POSUtil
//
//import static websiteschema.mpsegment.util.WordUtil.*
//
//class QuerySyntaxFilter
//        extends AbstractSegmentFilter {
//
//    public QuerySyntaxFilter(MPSegmentConfiguration config) {
//        this.config = config
//    }
//
//    override def doFilter() {
//        if (config.isSupportQuerySyntax() && segmentResult.length() <= config.getMaxQueryLength()) {
//            Int length = segmentResult.length()
//
//            for (Int index = 0; index < length; index++) {
//                if (isNotMarked(index)) {
//                    Int pos = segmentResult.getPOS(index)
//                    if (pos == posUN || pos == posW) {
//                        String s1 = segmentResult.getWord(index)
//                        if (s1.length() == 1 && !isLeftOrRightBraceOrColonOrSlash(s1)) {
//                            if (s1.equals(leftBrace)) {
//                                if (index + 2 < length && rightBrace.equals(segmentResult.getWord(index + 2))) {
//                                    Int k1 = isNumerical(segmentResult.getWord(index + 1))
//                                    if (k1 == 1) {
//                                        Int i2
//                                        if (index >= 1) {
//                                            i2 = index - 1
//                                            if (segmentResult.getWord(i2).equals(tilda)) {
//                                                i2--
//                                            }
//                                        } else {
//                                            i2 = index
//                                        }
//                                        Int l2 = index + 2
//                                        mergeWordsWithPOS_UN(i2, l2)
//                                    }
//                                }
//                            } else if (s1.equals(questionMark) || s1.equals(star)) {
//                                if (index >= 1) {
//                                    Int k3 = index
//                                    Int j4 = index
//                                    if (index + 1 < length) {
//                                        if (isLetterOrDigitWithUnderscore(segmentResult.getWord(index + 1))) {
//                                            j4 = index + 1
//                                        }
//                                        if (isLetterOrDigitWithUnderscore(segmentResult.getWord(index - 1))) {
//                                            k3 = index - 1
//                                        }
//                                        mergeWordsWithPOS_UN(k3, j4)
//                                    } else if (isLetterOrDigitWithUnderscore(segmentResult.getWord(index - 1))) {
//                                        mergeWordsWithPOS_UN(index - 1, index)
//                                    }
//                                } else if (index + 1 < length && isLetterOrDigitWithUnderscore(segmentResult.getWord(index + 1))) {
//                                    mergeWordsWithPOS_UN(index, index + 1)
//                                }
//                            } else if (s1.equals(colon)) {
//                                if (index > 0 && index + 1 < length && isAlphaNumericWithUnderScore(segmentResult.getWord(index + 1))) {
//                                    mergeWordsWithPOS_UN(index - 1, index + 1)
//                                }
//                            } else if (s1.equals(underScore)) {
//                                Int j2
//                                if (index > 1 && isAlphaNumericWithUnderScore(segmentResult.getWord(index - 1))) {
//                                    j2 = index - 1
//                                } else {
//                                    j2 = index
//                                }
//                                Int i3 = index
//                                for (Int l3 = index + 1; l3 < length; l3++) {
//                                    if (!isAlphaNumericWithUnderScore(segmentResult.getWord(l3))) {
//                                        break
//                                    }
//                                    i3 = l3
//                                }
//
//                                if (i3 > j2) {
//                                    mergeWordsWithPOS_UN(j2, i3)
//                                }
//                            } else if (s1.equals(slash)) {
//                                Int k2
//                                if (index > 1 && isAlphaNumericWithUnderScore_Slash_Colon(segmentResult.getWord(index - 1))) {
//                                    k2 = index - 1
//                                } else {
//                                    k2 = index
//                                }
//                                Int j3 = index
//                                for (Int i4 = index + 1; i4 < length; i4++) {
//                                    if (!isAlphaNumericWithUnderScore_Slash_Colon(segmentResult.getWord(i4))) {
//                                        break
//                                    }
//                                    j3 = i4
//                                }
//
//                                if (j3 > k2) {
//                                    mergeWordsWithPOS_UN(k2, j3)
//                                }
//                            } else if (s1.equals(tilda) && index + 1 < length && isLetterOrDigitWithUnderscore(segmentResult.getWord(index + 1))) {
//                                mergeWordsWithPOS_UN(index, index + 1)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private def mergeWordsWithPOS_UN(firstWordIndex: Int, secondWordIndex: Int) {
//        setWordIndexesAndPOSForMerge(firstWordIndex, secondWordIndex, posUN)
//    }
//    private static Int posUN = POSUtil.POS_UNKOWN
//    private static Int posW = POSUtil.POS_W
//    private static String leftBrace = "["
//    private static String rightBrace = "]"
//    private static String questionMark = "?"
//    private static String star = "*"
//    private static String tilda = "~"
//    private static String underScore = "_"
//    private static String slash = "/"
//    private static String colon = ":"
//    private var config : MPSegmentConfiguration = null
//}
