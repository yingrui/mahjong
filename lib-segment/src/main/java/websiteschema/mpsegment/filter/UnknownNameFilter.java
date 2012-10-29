/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.dict.POSUtil;
import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.util.WordUtil;
import websiteschema.mpsegment.dict.ChNameDictionary;
import static websiteschema.mpsegment.util.WordUtil.*;

/**
 *
 * @author ray
 */
public class UnknownNameFilter extends AbstractSegmentFilter {

    private static ChNameDictionary chNameDict = null;
    private static double factor1 = 1.1180000000000001D;
    private static double factor2 = 0.65000000000000002D;
    private static double factor3 = 1.6299999999999999D;
    private static int posNR = POSUtil.POS_NR;
    private static int posM = POSUtil.POS_M;
    private static ForeignName foreignName = null;
    private boolean useChNameDict = true;
    private boolean useForeignNameDict = false;
    private int segmentResultLength = 0;
    private int maxNameWordLength = 5;
    private int nameStartIndex = -1;
    private int nameEndIndex = -1;
    private boolean hasPossibleFoundName = false;
    private int wordIndex = 0;
    private int numOfNameWordItem = -1;

    public UnknownNameFilter() {
        initialize();
    }

    private synchronized void initialize() {
        if (useChNameDict) {
            if (chNameDict == null) {
                chNameDict = new ChNameDictionary();
                chNameDict.loadNameDict(MPSegmentConfiguration.getINSTANCE().getChNameDict());
            }
            if (useForeignNameDict && foreignName == null) {
                foreignName = new ForeignName();
                foreignName.loadNameWord();
            }
        }
    }

    private boolean isWordConfirmed(int wordIndex) {
        return wordPosIndexes[wordIndex] > 0;
    }

    private boolean reachTheEnd(int wordIndex) {
        return wordIndex + 1 >= segmentResultLength;
    }

    private void processPotentialName() {
        if (nameEndIndex - nameStartIndex >= 1) {
            int recognizedNameLength = recognizeNameWord();
            if (numOfNameWordItem >= 2) {
                wordIndex = nameStartIndex + recognizedNameLength;
            } else {
                wordIndex = nameStartIndex + 1;
            }
        }
        markPositionImpossibleToBeName();
    }

    @Override
    public void doFilter() {
        if (useChNameDict && MPSegmentConfiguration.getINSTANCE().isChineseNameIdentify()) {
            segmentResultLength = segmentResult.length();
            markPositionImpossibleToBeName();

            for (wordIndex = 0; wordIndex < segmentResultLength; wordIndex++) {
                if (isWordConfirmed(wordIndex)) {
                    continue;
                }
                if (hasPossibleFoundName && (nameEndIndex - nameStartIndex >= maxNameWordLength || reachTheEnd(wordIndex))) {
                    if (reachTheEnd(wordIndex)) {
                        nameEndIndex = wordIndex;
                    }
                    processPotentialName();
                    if (wordIndex + 1 > segmentResultLength) {
                        break;
                    }
                }

                if (segmentResult.getPOS(wordIndex) == POSUtil.POS_NR) {
                    markPositionMaybeName();
                } else {
                    boolean isPos_P_C_U_W_UN = isPos_P_C_U_W_UN(segmentResult.getPOS(wordIndex));
                    boolean isChineseJieCi = isChineseJieCi(segmentResult.getWord(wordIndex));
                    if (hasPossibleFoundName) {
                        if ((isPos_P_C_U_W_UN && !isChineseJieCi)
                                || segmentResult.getWord(wordIndex).length() > 2) {
                            processPotentialName();
                        } else {
                            nameEndIndex = wordIndex;
                            if (wordIndex + 1 == segmentResultLength && nameEndIndex - nameStartIndex >= 1) {
                                assert (false);
                                recognizeNameWord();
                            }
                        }
                    } else if (segmentResult.getWord(wordIndex).length() == 1
                            && !isPos_P_C_U_W_UN || isChineseJieCi) {
                        markPositionMaybeName();
                    }
                }
            }
        }
    }

    private void markPositionMaybeName() {
        if (nameStartIndex < 0) {
            nameStartIndex = wordIndex;
            hasPossibleFoundName = true;
        } else {
            nameEndIndex = wordIndex;
        }
    }

    private void markPositionImpossibleToBeName() {
        hasPossibleFoundName = false;
        nameStartIndex = -1;
        nameEndIndex = -1;
    }

    private int recognizeNameWord() {
        recognizeNameWordBetween(nameStartIndex, nameEndIndex);
        if (numOfNameWordItem > 0 && nameStartIndex > 0 && segmentResult.getPOS(nameStartIndex - 1) == posM) {
            numOfNameWordItem = 0;
        }
        if (numOfNameWordItem > 0) {
            if (MPSegmentConfiguration.getINSTANCE().isXingMingSeparate()) {
                if (numOfNameWordItem >= 3) {
                    mergeWordsWithPOS(nameStartIndex + 1, nameStartIndex + 2, posNR);
                }
            } else if (numOfNameWordItem >= 2) {
                mergeWordsWithPOS(nameStartIndex, (nameStartIndex + numOfNameWordItem) - 1, posNR);
            }
        } else if (useForeignNameDict) {
            numOfNameWordItem = processForeignName(nameStartIndex, nameEndIndex);
        }
        return numOfNameWordItem;
    }

    private int recognizeNameWordBetween(int begin, int end) {
        int gap = (end - begin) + 1;
        numOfNameWordItem = -1;
        if (segmentResult.getWord(begin).length() > 2 || segmentResult.getWord(begin + 1).length() > 2) {
            return numOfNameWordItem;
        }
        if (segmentResult.getWord(begin + 1).length() == 1) {
            if (gap >= 3) {
                double d1 = chNameDict.computeLgLP2(segmentResult.getWord(begin), segmentResult.getWord(begin + 1));
                if (d1 <= 0.0D && segmentResult.getWord(begin).length() == 2) {
                    return getNumNR(begin);
                }
                double d4 = chNameDict.computeLgLP3(segmentResult.getWord(begin), segmentResult.getWord(begin + 1), segmentResult.getWord(begin + 2));
                if (segmentResult.getWord(begin + 2).length() > 1) {
                    d4 = 0.0D;
                }
                if (d1 > 0.95999999999999996D) {
                    d1 *= getRightBoundaryWordLP(segmentResult.getWord(begin + 2));
                }
                if (d4 > d1 && d4 > factor1) {
                    numOfNameWordItem = 3;
                    if (isSpecialMingChar(begin + 2)) {
                        double d5 = chNameDict.computeLgMing23(segmentResult.getWord(begin + 1), segmentResult.getWord(begin + 2));
                        numOfNameWordItem = 2;
                        if (d5 > 1.1339999999999999D || d5 > 0.90000000000000002D && d4 > 1.6000000000000001D && d4 / d1 > 2D) {
                            numOfNameWordItem = 3;
                        }
                    } else if (processSpecialMingChar(begin + 1)) {
                        numOfNameWordItem = -1;
                    }
                } else if (d1 > d4 && d1 > factor2) {
                    numOfNameWordItem = 2;
                    if (isSpecialMingChar(begin + 1)) {
                        numOfNameWordItem = -1;
                    }
                }
            } else {
                double d2 = chNameDict.computeLgLP2(segmentResult.getWord(begin), segmentResult.getWord(begin + 1));
                if (d2 > factor2) {
                    numOfNameWordItem = 2;
                    if (isSpecialMingChar(begin + 1)) {
                        numOfNameWordItem = -1;
                    }
                } else if (d2 <= 0.0D && segmentResult.getWord(begin).length() == 2) {
                    numOfNameWordItem = getNumNR(begin);
                }
            }
        } else if (segmentResult.getWord(begin + 1).length() == 2) {
            double d3 = chNameDict.computeLgLP3(segmentResult.getWord(begin), segmentResult.getWord(begin + 1).substring(0, 1), segmentResult.getWord(begin + 1).substring(1, 2));
            if (d3 > factor3) {
                numOfNameWordItem = 2;
            }
        }
        return numOfNameWordItem;
    }

    private int processForeignName(int i1, int j1) {
        int l1 = -1;
        for (int i2 = i1; i2 <= j1; i2++) {
            if (!foreignName.isForiegnName(segmentResult.getWord(i2))) {
                break;
            }
            l1 = i2;
        }
        return l1;
    }

    private int getNumNR(int i1) {
        byte byte0 = -1;
        String s1 = segmentResult.getWord(i1).substring(0, 1);
        String s2 = segmentResult.getWord(i1).substring(1, 2);
        String s3 = segmentResult.getWord(i1 + 1);
        double d2 = chNameDict.computeLgLP3_2(segmentResult.getWord(i1), segmentResult.getWord(i1 + 1));
        double d1 = chNameDict.computeLgLP2(s1, s2);
        if (d1 > 0.95999999999999996D) {
            d1 *= getRightBoundaryWordLP(s3);
        }
        if (d2 > d1 && d2 > factor1) {
            byte0 = 2;
            if (isSpecialMingChar(i1 + 1)) {
                double d3 = chNameDict.computeLgMing23(s2, s3);
                byte0 = -1;
                if (d3 > 1.1339999999999999D || d3 > 0.90000000000000002D && d2 > 1.6000000000000001D && d2 / d1 > 2D) {
                    byte0 = 2;
                }
            }
        }
        return byte0;
    }

    private double getRightBoundaryWordLP(String s1) {
        double d1 = 1.0D + chNameDict.getRightBoundaryWordLP(s1);
        return d1;
    }

    private boolean isSpecialMingChar(int wordIndex) {
        String word = segmentResult.getWord(wordIndex);
        return WordUtil.isSpecialMingChar(word);
    }

    private boolean processSpecialMingChar(int i1) {
        String s1 = segmentResult.getWord(i1);
        if (s1.equals("以") || s1.equals("从")) {
            double d1 = chNameDict.computeLgMing23(segmentResult.getWord(i1), segmentResult.getWord(i1 + 1));
            if (d1 < 0.92000000000000004D) {
                return true;
            }
        } else if (s1.equals("得") || s1.equals("为") || s1.equals("向") || s1.equals("自")) {
            double d2 = chNameDict.computeLgMing23(segmentResult.getWord(i1), segmentResult.getWord(i1 + 1));
            if (d2 <= 0.93000000000000005D) {
                return true;
            }
        } else if (s1.equals("则")) {
            double d3 = chNameDict.computeLgMing23(segmentResult.getWord(i1), segmentResult.getWord(i1 + 1));
            if (d3 <= 0.80000000000000004D) {
                return true;
            }
        } else if (s1.equals("如")) {
            double d4 = chNameDict.computeLgMing23(segmentResult.getWord(i1), segmentResult.getWord(i1 + 1));
            if (d4 <= 1.0D) {
                return true;
            }
        }
        return false;
    }
}
