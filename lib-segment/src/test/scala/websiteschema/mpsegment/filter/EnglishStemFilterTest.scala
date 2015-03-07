package websiteschema.mpsegment.filter

import org.junit.Assert
import org.junit.Test
import websiteschema.mpsegment.core.SegmentResult
import websiteschema.mpsegment.dict.POSUtil

class EnglishStemFilterTest {

  @Test
  def should_remove_white_space_when_filter_english_words() {
    val filter = new EnglishStemFilter(true)
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

  @Test
  def should_not_stem_english_words_when_stemming_is_false() {
    val filter = new EnglishStemFilter(false)
    val segmentResult = new SegmentResult(5)
    segmentResult.setWords(List[String]("She", " ", "likes", " ", "hunting").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    filter.setSegmentResult(segmentResult)
    filter.filtering()
    Assert.assertEquals("She", segmentResult.getWord(0))
    Assert.assertEquals("likes", segmentResult.getWord(1))
    Assert.assertEquals("hunting", segmentResult.getWord(2))
  }

}
