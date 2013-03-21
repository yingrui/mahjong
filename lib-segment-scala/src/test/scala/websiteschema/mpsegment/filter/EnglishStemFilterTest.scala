//package websiteschema.mpsegment.filter;
//
//import org.junit.Assert;
//import org.junit.Test;
//import websiteschema.mpsegment.core.SegmentResult;
//import websiteschema.mpsegment.dict.POSUtil;
//
//class EnglishStemFilterTest {
//
//    var filter = new EnglishStemFilter()
//
//    @Test
//    def should_merge_two_reduplicating_words() {
//        var segmentResult = new SegmentResult(5)
//        segmentResult.setWords(new Array[String]{"She", " ", "likes", " ", "hunting"});
//        segmentResult.setPOSArray(new Array[Int]{POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN});
//        segmentResult.setDomainTypes(new Array[Int]{0, 0, 0, 0});
//        segmentResult.setConcepts(new Array[String]{"N/A", "N/A", "N/A", "N/A", "N/A"});
//
//        filter.setSegmentResult(segmentResult);
//        filter.filtering();
//        Assert.assertEquals("like", segmentResult.getWord(1));
//        Assert.assertEquals("hunt", segmentResult.getWord(2));
//    }
//
//}
