package websiteschema.mpsegment.filter

import org.junit.Assert
import org.junit.Test
import websiteschema.mpsegment.core.SegmentResult
import websiteschema.mpsegment.dict.POSUtil

class AbstractSegmentFilterTest {

    @Test
    def should_merge_two_word_with_POS_NR() {
        val filter = new Filter()
        val result = getSegmentResult()
        filter.setSegmentResult(result)
        filter.setWordIndexesAndPOSForMerge(1, 2, POSUtil.POS_NR)
        filter.compactSegmentResult()
        Assert.assertEquals("三丰", result.getWord(1))
        Assert.assertEquals(POSUtil.POS_NR, result.getPOS(1))
        Assert.assertEquals(3, result.length())
    }

    @Test
    def should_merge_adjacent_words_with_POS_NR() {
        val filter = new Filter()
        val result = getSegmentResult()
        filter.setSegmentResult(result)
        filter.setWordIndexesAndPOSForMerge(1, 3, POSUtil.POS_NR)
        filter.filtering()
        Assert.assertEquals("三丰南", result.getWord(1))
        Assert.assertEquals(POSUtil.POS_NR, result.getPOS(1))
        Assert.assertEquals(2, result.length())
    }

    private def getSegmentResult() : SegmentResult = {
        val result = new SegmentResult(4)
        result.setWords(List[String]("张", "三", "丰", "南").toArray)
        result.setPOSArray(List[Int](POSUtil.POS_V, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_N).toArray)
        result.setDomainTypes(List[Int](0, 0, 0, 0).toArray)
        result.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A").toArray)
        return result
    }

    class Filter extends AbstractSegmentFilter {
        override def doFilter() {
        }
    }
}
