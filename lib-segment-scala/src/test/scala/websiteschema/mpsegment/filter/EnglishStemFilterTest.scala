package websiteschema.mpsegment.filter

import org.junit.Assert
import org.junit.Test
import websiteschema.mpsegment.core.SegmentResult
import websiteschema.mpsegment.dict.POSUtil

class EnglishStemFilterTest {

    var filter = new EnglishStemFilter()

    @Test
    def should_merge_two_reduplicating_words() {
        val segmentResult = new SegmentResult(5)
        segmentResult.setWords(List[String]("She", " ", "likes", " ", "hunting").toArray)
        segmentResult.setPOSArray(List[Int](POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN).toArray)
        segmentResult.setDomainTypes(List[Int](0, 0, 0, 0).toArray)
        segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A", "N/A").toArray)

        filter.setSegmentResult(segmentResult)
        filter.filtering()
        Assert.assertEquals("like", segmentResult.getWord(1))
        Assert.assertEquals("hunt", segmentResult.getWord(2))
    }

}
