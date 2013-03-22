package websiteschema.mpsegment.filter

import org.junit.Assert
import org.junit.Test
import websiteschema.mpsegment.core.SegmentResult
import websiteschema.mpsegment.dict.POSUtil

class ReduplicatingFilterTest {

    var filter = new ReduplicatingFilter()

    @Test
    def should_merge_two_reduplicating_words() {
        val segmentResult = new SegmentResult(4)
        segmentResult.setWords(List[String]("一", "件", "件", "物品").toArray)
        segmentResult.setPOSArray(List[Int](POSUtil.POS_M, POSUtil.POS_A, POSUtil.POS_A, POSUtil.POS_N).toArray)
        segmentResult.setDomainTypes(List[Int](0, 0, 0, 0).toArray)
        segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A").toArray)

        filter.setSegmentResult(segmentResult)
        filter.filtering()
        Assert.assertEquals("一件件", segmentResult.getWord(0))
        Assert.assertEquals(POSUtil.POS_A, segmentResult.getPOS(0))
    }

    @Test
    def should_merge_three_reduplicating_words() {
        var segmentResult = new SegmentResult(3)
        segmentResult.setWords(List[String]("件", "件", "物品").toArray)
        segmentResult.setPOSArray(List[Int](POSUtil.POS_M, POSUtil.POS_M, POSUtil.POS_N).toArray)
        segmentResult.setDomainTypes(List[Int](0, 0, 0).toArray)
        segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A").toArray)

        filter.setSegmentResult(segmentResult)
        filter.filtering()
        Assert.assertEquals("件件", segmentResult.getWord(0))
        Assert.assertEquals(POSUtil.POS_M, segmentResult.getPOS(0))
    }

}
