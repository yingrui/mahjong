package me.yingrui.segment.filter

import me.yingrui.segment.conf.SegmentConfiguration
import me.yingrui.segment.dict.POSUtil
import me.yingrui.segment.util.WordUtil._

class QuerySyntaxFilter(config: SegmentConfiguration)
        extends AbstractSegmentFilter {


    override def doFilter() {
        if (config.isSupportQuerySyntax() && segmentResult.length() <= config.getMaxQueryLength()) {
            val length = segmentResult.length()

            for (index <- 0 until length) {
                if (isNotMarked(index)) {
                    val pos = segmentResult.getPOS(index)
                    if (pos == posUN || pos == posW) {
                        val s1 = segmentResult.getWord(index)
                        if (s1.length() == 1 && !isLeftOrRightBraceOrColonOrSlash(s1)) {
                            if (s1.equals(leftBrace)) {
                                if (index + 2 < length && rightBrace.equals(segmentResult.getWord(index + 2))) {
                                    val k1 = isNumerical(segmentResult.getWord(index + 1))
                                    if (k1 == 1) {
                                        var i2 = 0
                                        if (index >= 1) {
                                            i2 = index - 1
                                            if (segmentResult.getWord(i2).equals(tilda)) {
                                                i2-= 1
                                            }
                                        } else {
                                            i2 = index
                                        }
                                        val l2 = index + 2
                                        mergeWordsWithPOS_UN(i2, l2)
                                    }
                                }
                            } else if (s1.equals(questionMark) || s1.equals(star)) {
                                if (index >= 1) {
                                    var k3 = index
                                    var j4 = index
                                    if (index + 1 < length) {
                                        if (isLetterOrDigitWithUnderscore(segmentResult.getWord(index + 1))) {
                                            j4 = index + 1
                                        }
                                        if (isLetterOrDigitWithUnderscore(segmentResult.getWord(index - 1))) {
                                            k3 = index - 1
                                        }
                                        mergeWordsWithPOS_UN(k3, j4)
                                    } else if (isLetterOrDigitWithUnderscore(segmentResult.getWord(index - 1))) {
                                        mergeWordsWithPOS_UN(index - 1, index)
                                    }
                                } else if (index + 1 < length && isLetterOrDigitWithUnderscore(segmentResult.getWord(index + 1))) {
                                    mergeWordsWithPOS_UN(index, index + 1)
                                }
                            } else if (s1.equals(colon)) {
                                if (index > 0 && index + 1 < length && isAlphaNumericWithUnderScore(segmentResult.getWord(index + 1))) {
                                    mergeWordsWithPOS_UN(index - 1, index + 1)
                                }
                            } else if (s1.equals(underScore)) {
                                var j2 = 0
                                if (index > 1 && isAlphaNumericWithUnderScore(segmentResult.getWord(index - 1))) {
                                    j2 = index - 1
                                } else {
                                    j2 = index
                                }
                                var i3 = index
                                for (l3 <- index + 1 until length if (isAlphaNumericWithUnderScore(segmentResult.getWord(l3)))) {
                                    i3 = l3
                                }

                                if (i3 > j2) {
                                    mergeWordsWithPOS_UN(j2, i3)
                                }
                            } else if (s1.equals(slash)) {
                                var k2 = 0
                                if (index > 1 && isAlphaNumericWithUnderScore_Slash_Colon(segmentResult.getWord(index - 1))) {
                                    k2 = index - 1
                                } else {
                                    k2 = index
                                }
                                var j3 = index
                                for (i4 <- index + 1 until length if (isAlphaNumericWithUnderScore_Slash_Colon(segmentResult.getWord(i4)))) {
                                    j3 = i4
                                }

                                if (j3 > k2) {
                                    mergeWordsWithPOS_UN(k2, j3)
                                }
                            } else if (s1.equals(tilda) && index + 1 < length && isLetterOrDigitWithUnderscore(segmentResult.getWord(index + 1))) {
                                mergeWordsWithPOS_UN(index, index + 1)
                            }
                        }
                    }
                }
            }
        }
    }

    private def mergeWordsWithPOS_UN(firstWordIndex: Int, secondWordIndex: Int) {
        setWordIndexesAndPOSForMerge(firstWordIndex, secondWordIndex, posUN)
    }
    private val posUN = POSUtil.POS_UNKOWN
    private val posW = POSUtil.POS_W
    private val leftBrace = "["
    private val rightBrace = "]"
    private val questionMark = "?"
    private val star = "*"
    private val tilda = "~"
    private val underScore = "_"
    private val slash = "/"
    private val colon = ":"
}
