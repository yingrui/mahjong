//package websiteschema.mpsegment.filter
//
//import org.junit.Assert
//import org.junit.Test
//import websiteschema.mpsegment.core.SegmentResult
//import websiteschema.mpsegment.dict.POSUtil
//
//class AbstractSegmentFilterTest {
//
//    @Test
//    def should_merge_two_word_with_POS_NR() {
//        var filter = new Filter()
//        var result = getSegmentResult()
//        filter.setSegmentResult(result)
//        filter.setWordIndexesAndPOSForMerge(1, 2, POSUtil.POS_NR)
//        filter.compactSegmentResult()
//        Assert.assertEquals("三丰", result.getWord(1))
//        Assert.assertEquals(POSUtil.POS_NR, result.getPOS(1))
//        Assert.assertEquals(3, result.length())
//    }
//
//    @Test
//    def should_merge_adjacent_words_with_POS_NR() {
//        var filter = new Filter()
//        var result = getSegmentResult()
//        filter.setSegmentResult(result)
//        filter.setWordIndexesAndPOSForMerge(1, 3, POSUtil.POS_NR)
//        filter.filtering()
//        Assert.assertEquals("三丰南", result.getWord(1))
//        Assert.assertEquals(POSUtil.POS_NR, result.getPOS(1))
//        Assert.assertEquals(2, result.length())
//    }
//
//    private def getSegmentResult() : SegmentResult = {
//        var result = new SegmentResult(4)
//        result.setWords(new Array[String]{"张", "三", "丰", "南"})
//        result.setPOSArray(new Array[Int]{POSUtil.POS_V, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_N})
//        result.setDomainTypes(new Array[Int]{0, 0, 0, 0})
//        result.setConcepts(new Array[String]{"N/A", "N/A", "N/A", "N/A"})
//        return result
//    }
//
//    class Filter extends AbstractSegmentFilter {
//        override def doFilter() {
//        }
//    }
//}
