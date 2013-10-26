package websiteschema.mpsegment.filter

import ner.HmmNameFilter
import org.junit.Assert
import org.junit.Test
import websiteschema.mpsegment.core.SegmentResult
import websiteschema.mpsegment.dict.POSUtil
import websiteschema.mpsegment.conf.MPSegmentConfiguration

class HmmNameFilterTest {

  var filter = HmmNameFilter(MPSegmentConfiguration(Map("separate.xingming" -> "true")))

  @Test
  def should_recognize_xing_with_prefix() {
    val segmentResult = new SegmentResult(6)
    segmentResult.setWords(List[String]("老", "邱", "来", "到", "了", "社区").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_N, POSUtil.POS_N, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    filter.setSegmentResult(segmentResult)
    filter.filtering()
    Assert.assertEquals("老邱", segmentResult.getWord(0))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(0))
  }

  @Test
  def should_recognize_xing_with_suffix() {
    val segmentResult = new SegmentResult(6)
    segmentResult.setWords(List[String]("邱", "老", "来", "到", "了", "社区").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_N, POSUtil.POS_N, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    filter.setSegmentResult(segmentResult)
    filter.filtering()
    Assert.assertEquals("邱老", segmentResult.getWord(0))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(0))
  }

  @Test
  def should_recognize_xing_with_double_name() {
    val segmentResult = new SegmentResult(6)
    segmentResult.setWords(List[String]("张", "三", "丰", "到", "了", "社区").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_N, POSUtil.POS_N, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    filter.setSegmentResult(segmentResult)
    filter.filtering()
    Assert.assertEquals("张", segmentResult.getWord(0))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(0))
    Assert.assertEquals("三丰", segmentResult.getWord(1))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(1))
  }

  @Test
  def should_recognize_xing_with_double_character_word_as_name() {
    val segmentResult = new SegmentResult(6)
    segmentResult.setWords(List[String]("张", "朝阳", "来", "到", "了", "社区").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_N, POSUtil.POS_N, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    filter.setSegmentResult(segmentResult)
    filter.filtering()
    Assert.assertEquals("张", segmentResult.getWord(0))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(0))
    Assert.assertEquals("朝阳", segmentResult.getWord(1))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(1))
  }

  @Test
  def should_recognize_xing_with_single_name() {
    val segmentResult = new SegmentResult(6)
    segmentResult.setWords(List[String]("李", "鹏", "总理", "到", "了", "社区").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_N, POSUtil.POS_N, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    filter.setSegmentResult(segmentResult)
    filter.filtering()
    Assert.assertEquals("李", segmentResult.getWord(0))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(0))
    Assert.assertEquals("鹏", segmentResult.getWord(1))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(1))
  }

  @Test
  def should_recognize_word_contains_xing_and_name() {
    val segmentResult = new SegmentResult(6)
    segmentResult.setWords(List[String]("王国", "维", "来", "到", "了", "社区").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_N, POSUtil.POS_N, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    filter.setSegmentResult(segmentResult)
    filter.filtering()
    Assert.assertEquals("王", segmentResult.getWord(0))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(0))
    Assert.assertEquals("国维", segmentResult.getWord(1))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(1))
  }
}
