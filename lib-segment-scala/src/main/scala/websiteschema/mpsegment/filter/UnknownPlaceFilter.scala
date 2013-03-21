///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package websiteschema.mpsegment.filter;
//
//import websiteschema.mpsegment.dict.POSUtil;
//
///**
// *
// * @author ray
// */
//class UnknownPlaceFilter extends AbstractSegmentFilter {
//
//    private static String adminLevels = "省市县区乡镇村旗州";
//    private var recognizeDiMing : Boolean = true;
//
//    override def doFilter() {
//        if (recognizeDiMing) {
//            var length = segmentResult.length()
//            for (Int wordI = 0; wordI < length; wordI++) {
//                if (isNotMarked(wordI)) {
//                    var pos = segmentResult.getPOS(wordI)
//                    if (pos == POSUtil.POS_NS && wordI + 1 < length) {
//                        var word = segmentResult.getWord(wordI)
//                        if (segmentResult.getWord(wordI + 1).length() == 1 && adminLevels.indexOf(segmentResult.getWord(wordI + 1)) > 0 && word.lastIndexOf(segmentResult.getWord(wordI + 1)) != length - 1) {
//                            setWordIndexesAndPOSForMerge(wordI, wordI + 1, POSUtil.POS_NS);
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
