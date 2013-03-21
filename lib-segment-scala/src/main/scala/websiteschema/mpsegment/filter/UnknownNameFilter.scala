///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package websiteschema.mpsegment.filter
//
//import websiteschema.mpsegment.concept.Concept
//import websiteschema.mpsegment.conf.MPSegmentConfiguration
//import websiteschema.mpsegment.dict.ChNameDictionary
//import websiteschema.mpsegment.dict.POSUtil
//import websiteschema.mpsegment.util.WordUtil
//
//import static websiteschema.mpsegment.util.WordUtil.isChineseJieCi
//import static websiteschema.mpsegment.util.WordUtil.isPos_P_C_U_W_UN
//
//class UnknownNameFilter extends AbstractSegmentFilter {
//
//    private static ChNameDictionary chNameDict = null
//    private static Double factor1 = 1.1180000000000001D
//    private static Double factor2 = 0.65000000000000002D
//    private static Double factor3 = 1.6299999999999999D
//    private static ForeignName foreignName = null
//    private var useChNameDict : Boolean = true
//    private var useForeignNameDict : Boolean = false
//    private var segmentResultLength : Int = 0
//    private var maxNameWordLength : Int = 5
//    private var nameStartIndex : Int = -1
//    private var nameEndIndex : Int = -1
//    private var hasPossibleFoundName : Boolean = false
//    private var wordIndex : Int = 0
//    private var numOfNameWordItem : Int = -1
//    private var config : MPSegmentConfiguration = null
//
//    public UnknownNameFilter(MPSegmentConfiguration config) {
//        this.config = config
//        initialize()
//    }
//
//    private synchronized void initialize() {
//        if (useChNameDict) {
//            if (chNameDict == null) {
//                chNameDict = new ChNameDictionary()
//                chNameDict.loadNameDict(config.getChNameDict())
//            }
//            if (useForeignNameDict && foreignName == null) {
//                foreignName = new ForeignName()
//                foreignName.loadNameWord()
//            }
//        }
//    }
//
//    private def reachTheEnd(wordIndex: Int) : Boolean = {
//        return wordIndex + 1 >= segmentResultLength
//    }
//
//    private def processPotentialName() {
//        if (nameEndIndex - nameStartIndex >= 1) {
//            var recognizedNameLength = recognizeNameWord()
//            if (numOfNameWordItem >= 2) {
//                wordIndex = nameStartIndex + recognizedNameLength
//            } else {
//                wordIndex = nameStartIndex + 1
//            }
//        }
//        markPositionImpossibleToBeName()
//    }
//
//    override def doFilter() {
//        if (useChNameDict && config.isChineseNameIdentify()) {
//            segmentResultLength = segmentResult.length()
//            markPositionImpossibleToBeName()
//
//            for (wordIndex = 0; wordIndex < segmentResultLength; wordIndex++) {
//                if (isWordConfirmed(wordIndex)) {
//                    continue
//                }
//                if (hasPossibleFoundName && (nameEndIndex - nameStartIndex >= maxNameWordLength || reachTheEnd(wordIndex))) {
//                    if (reachTheEnd(wordIndex)) {
//                        nameEndIndex = wordIndex
//                    }
//                    processPotentialName()
//                    if (wordIndex + 1 > segmentResultLength) {
//                        break
//                    }
//                }
//
//                if (segmentResult.getPOS(wordIndex) == POSUtil.POS_NR) {
//                    markPositionMaybeName()
//                } else {
//                    var isPos_P_C_U_W_UN = isPos_P_C_U_W_UN(segmentResult.getPOS(wordIndex))
//                    var isChineseJieCi = isChineseJieCi(segmentResult.getWord(wordIndex))
//                    if (hasPossibleFoundName) {
//                        if ((isPos_P_C_U_W_UN && !isChineseJieCi)
//                                || segmentResult.getWord(wordIndex).length() > 2) {
//                            processPotentialName()
//                        } else {
//                            nameEndIndex = wordIndex
//                            if (wordIndex + 1 == segmentResultLength && nameEndIndex - nameStartIndex >= 1) {
//                                assert (false)
//                                recognizeNameWord()
//                            }
//                        }
//                    } else if (segmentResult.getWord(wordIndex).length() == 1
//                            && !isPos_P_C_U_W_UN || isChineseJieCi) {
//                        markPositionMaybeName()
//                    }
//                }
//            }
//        }
//    }
//
//    private def markPositionMaybeName() {
//        if (nameStartIndex < 0) {
//            nameStartIndex = wordIndex
//            hasPossibleFoundName = true
//        } else {
//            nameEndIndex = wordIndex
//        }
//    }
//
//    private def markPositionImpossibleToBeName() {
//        hasPossibleFoundName = false
//        nameStartIndex = -1
//        nameEndIndex = -1
//    }
//
//    private def recognizeNameWord() : Int = {
//        recognizeNameWordBetween(nameStartIndex, nameEndIndex)
//        if (numOfNameWordItem > 0 && nameStartIndex > 0 && segmentResult.getPOS(nameStartIndex - 1) == POSUtil.POS_M) {
//            numOfNameWordItem = 0
//        }
//        if (numOfNameWordItem > 0) {
//            if (config.isXingMingSeparate()) {
//                if (numOfNameWordItem >= 3) {
//                    var numOfMingWord = numOfNameWordItem - 1
//                    setWordIndexesAndPOSForMerge(nameStartIndex + 1, nameStartIndex + numOfMingWord, POSUtil.POS_NR)
//                }
//                segmentResult.setPOS(nameStartIndex, POSUtil.POS_NR)
//                segmentResult.setConcept(nameStartIndex, Concept.UNKNOWN.getName())
//                segmentResult.setConcept(nameStartIndex + 1, Concept.UNKNOWN.getName())
//            } else if (numOfNameWordItem >= 2) {
//                setWordIndexesAndPOSForMerge(nameStartIndex, (nameStartIndex + numOfNameWordItem) - 1, POSUtil.POS_NR)
//                segmentResult.setConcept(nameStartIndex, Concept.UNKNOWN.getName())
//            }
//        } else if (useForeignNameDict) {
//            numOfNameWordItem = processForeignName(nameStartIndex, nameEndIndex)
//        }
//        return numOfNameWordItem
//    }
//
//    private def recognizeNameWordBetween(begin: Int, end: Int) : Int = {
//        var gap = (end - begin) + 1
//        numOfNameWordItem = -1
//        if (segmentResult.getWord(begin).length() > 2 || segmentResult.getWord(begin + 1).length() > 2) {
//            return numOfNameWordItem
//        }
//        if (segmentResult.getWord(begin + 1).length() == 1) {
//            if (gap >= 3) {
//                var d1 = chNameDict.computeLgLP2(segmentResult.getWord(begin), segmentResult.getWord(begin + 1))
//                if (d1 <= 0.0D && segmentResult.getWord(begin).length() == 2) {
//                    return getNumNR(begin)
//                }
//                var d4 = chNameDict.computeLgLP3(segmentResult.getWord(begin), segmentResult.getWord(begin + 1), segmentResult.getWord(begin + 2))
//                if (segmentResult.getWord(begin + 2).length() > 1) {
//                    d4 = 0.0D
//                }
//                if (d1 > 0.95999999999999996D) {
//                    d1 *= getRightBoundaryWordLP(segmentResult.getWord(begin + 2))
//                }
//                if (d4 > d1 && d4 > factor1) {
//                    numOfNameWordItem = 3
//                    if (isSpecialMingChar(begin + 2)) {
//                        var d5 = chNameDict.computeLgMing23(segmentResult.getWord(begin + 1), segmentResult.getWord(begin + 2))
//                        numOfNameWordItem = 2
//                        if (d5 > 1.1339999999999999D || d5 > 0.90000000000000002D && d4 > 1.6000000000000001D && d4 / d1 > 2D) {
//                            numOfNameWordItem = 3
//                        }
//                    } else if (wouldNotBeMingWithSpecialChar(begin + 1)) {
//                        numOfNameWordItem = -1
//                    }
//                } else if (d1 > d4 && d1 > factor2) {
//                    numOfNameWordItem = 2
//                    if (isSpecialMingChar(begin + 1)) {
//                        numOfNameWordItem = -1
//                    }
//                }
//            } else {
//                var d2 = chNameDict.computeLgLP2(segmentResult.getWord(begin), segmentResult.getWord(begin + 1))
//                if (d2 > factor2) {
//                    numOfNameWordItem = 2
//                    if (isSpecialMingChar(begin + 1)) {
//                        numOfNameWordItem = -1
//                    }
//                } else if (d2 <= 0.0D && segmentResult.getWord(begin).length() == 2) {
//                    numOfNameWordItem = getNumNR(begin)
//                }
//            }
//        } else if (segmentResult.getWord(begin + 1).length() == 2) {
//            var d3 = chNameDict.computeLgLP3(segmentResult.getWord(begin), segmentResult.getWord(begin + 1).substring(0, 1), segmentResult.getWord(begin + 1).substring(1, 2))
//            if (d3 > factor3) {
//                numOfNameWordItem = 2
//            }
//        }
//        return numOfNameWordItem
//    }
//
//    private def processForeignName(i1: Int, j1: Int) : Int = {
//        var l1 = -1
//        for (Int i2 = i1; i2 <= j1; i2++) {
//            if (!foreignName.isForiegnName(segmentResult.getWord(i2))) {
//                break
//            }
//            l1 = i2
//        }
//        return l1
//    }
//
//    private def getNumNR(i1: Int) : Int = {
//        var byte0 = -1
//        var s1 = segmentResult.getWord(i1).substring(0, 1)
//        var s2 = segmentResult.getWord(i1).substring(1, 2)
//        var s3 = segmentResult.getWord(i1 + 1)
//        var d2 = chNameDict.computeLgLP3_2(segmentResult.getWord(i1), segmentResult.getWord(i1 + 1))
//        var d1 = chNameDict.computeLgLP2(s1, s2)
//        if (d1 > 0.95999999999999996D) {
//            d1 *= getRightBoundaryWordLP(s3)
//        }
//        if (d2 > d1 && d2 > factor1) {
//            byte0 = 2
//            if (isSpecialMingChar(i1 + 1)) {
//                var d3 = chNameDict.computeLgMing23(s2, s3)
//                byte0 = -1
//                if (d3 > 1.1339999999999999D || d3 > 0.90000000000000002D && d2 > 1.6000000000000001D && d2 / d1 > 2D) {
//                    byte0 = 2
//                }
//            }
//        }
//        return byte0
//    }
//
//    private def getRightBoundaryWordLP(s1: String) : Double = {
//        var d1 = 1.0D + chNameDict.getRightBoundaryWordLP(s1)
//        return d1
//    }
//
//    private def isSpecialMingChar(wordIndex: Int) : Boolean = {
//        var word = segmentResult.getWord(wordIndex)
//        return WordUtil.isSpecialMingChar(word)
//    }
//
//    private def wouldNotBeMingWithSpecialChar(index: Int) : Boolean = {
//        var word = segmentResult.getWord(index)
//        if (word.equals("以") || word.equals("从")) {
//            var d1 = chNameDict.computeLgMing23(segmentResult.getWord(index), segmentResult.getWord(index + 1))
//            if (d1 < 0.92000000000000004D) {
//                return true
//            }
//        } else if (word.equals("得") || word.equals("为") || word.equals("向") || word.equals("自")) {
//            var d2 = chNameDict.computeLgMing23(segmentResult.getWord(index), segmentResult.getWord(index + 1))
//            if (d2 <= 0.93000000000000005D) {
//                return true
//            }
//        } else if (word.equals("则")) {
//            var d3 = chNameDict.computeLgMing23(segmentResult.getWord(index), segmentResult.getWord(index + 1))
//            if (d3 <= 0.80000000000000004D) {
//                return true
//            }
//        } else if (word.equals("如")) {
//            var d4 = chNameDict.computeLgMing23(segmentResult.getWord(index), segmentResult.getWord(index + 1))
//            if (d4 <= 1.0D) {
//                return true
//            }
//        }
//        return false
//    }
//}
