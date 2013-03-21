//package websiteschema.mpsegment.filter
//
//import websiteschema.mpsegment.lang.en.PorterStemmer
//import websiteschema.mpsegment.util.CharCheckUtil
//
///**
// * EnglishStemFilter should not be used in search engine. <br/>
// * Because this filter will delete white space characters,<br/>
// * which many search engines already took it into account.
// */
//class EnglishStemFilter extends AbstractSegmentFilter {
//    var porterStemmer = new PorterStemmer()
//
//    override def doFilter() {
//        var length = segmentResult.length()
//        for (Int index = 0; index < length; index++) {
//            var word = segmentResult.getWord(index)
//            if (CharCheckUtil.isEnglish(word)) {
//                segmentResult.setWord(index, porterStemmer.stem(word))
//                deleteNextSpaceWord(index, length)
//            }
//        }
//    }
//
//    private def deleteNextSpaceWord(index: Int, length: Int) {
//        for (Int i = index + 1; i < length; i++) {
//            if (CharCheckUtil.isWhiteSpace(segmentResult.getWord(i))) {
//                deleteWordAt(i)
//            } else {
//                break
//            }
//        }
//    }
//}
