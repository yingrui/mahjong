//package websiteschema.mpsegment.filter
//
//import org.junit.Assert
//import org.junit.Test
//import websiteschema.mpsegment.core.SegmentResult
//import websiteschema.mpsegment.dict.POSUtil
//
//class NumberFilterTest {
//
//    var filter = new NumberAndTimeFilter()
//
//    @Test
//    def should_merge_two_word_with_POS_M() {
//        var segmentResult = new SegmentResult(6)
//        segmentResult.setWords(new Array[String]{"一个", "几十", "万", "人口", "的", "社区"})
//        segmentResult.setPOSArray(new Array[Int]{POSUtil.POS_M, POSUtil.POS_M, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N})
//        segmentResult.setDomainTypes(new Array[Int]{0, 0, 0, 0, 0, 0})
//        segmentResult.setConcepts(new Array[String]{"N/A", "N/A", "N/A", "N/A", "N/A", "N/A"})
//
//        filter.setSegmentResult(segmentResult)
//        filter.filtering()
//        Assert.assertEquals("一个", segmentResult.getWord(0))
//        Assert.assertEquals("几十万", segmentResult.getWord(1))
//        Assert.assertEquals(POSUtil.POS_M, segmentResult.getPOS(0))
//        Assert.assertEquals(POSUtil.POS_M, segmentResult.getPOS(1))
//    }
//
//}
