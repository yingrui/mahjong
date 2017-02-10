package me.yingrui.segment.filter

import me.yingrui.segment.core.SegmentResult
import me.yingrui.segment.dict.POSUtil
import org.junit.{Assert, Test}

class AbstractSegmentFilterTest {

  @Test
  def should_merge_two_word_with_POS_NR() {
    val filter = new Filter()
    val result = getSegmentResult()
    val oldLength = result.length()
    filter.setSegmentResult(result)
    filter.setWordIndexesAndPOSForMerge(1, 2, POSUtil.POS_NR)
    filter.compactSegmentResult()
    Assert.assertEquals("三丰", result.getWord(1))
    Assert.assertEquals(POSUtil.POS_NR, result.getPOS(1))
    Assert.assertEquals(1, oldLength - result.length())
  }

  @Test
  def should_merge_adjacent_words_with_POS_NR() {
    val filter = new Filter()
    val result = getSegmentResult()
    val oldLength = result.length()
    filter.setSegmentResult(result)
    filter.setWordIndexesAndPOSForMerge(1, 3, POSUtil.POS_NR)
    filter.filtering()
    Assert.assertEquals("三丰南", result.getWord(1))
    Assert.assertEquals(POSUtil.POS_NR, result.getPOS(1))
    Assert.assertEquals(2, oldLength - result.length())
  }

  @Test
  def should_separate_word_to_two_words_with_specified_part_of_speech() {
    val filter = new Filter()
    val result = getSegmentResult()
    filter.setSegmentResult(result)
    filter.separateWordAt(4, POSUtil.POS_NR, POSUtil.POS_NR)
    filter.filtering()
    Assert.assertEquals("霸", result.getWord(4))
    Assert.assertEquals("天", result.getWord(5))
    Assert.assertEquals(POSUtil.POS_NR, result.getPOS(4))
    Assert.assertEquals(POSUtil.POS_NR, result.getPOS(5))
  }



  @Test
  def should_separate_word_and_then_merge_other_word() {
    val filter = new Filter()
    val result = getSegmentResult()
    filter.setSegmentResult(result)
    filter.separateWordAt(4, POSUtil.POS_NR, POSUtil.POS_NR)
    filter.setWordIndexesAndPOSForMerge(5, 6, POSUtil.POS_NR)
    filter.filtering()
    Assert.assertEquals("霸", result.getWord(4))
    Assert.assertEquals("天", result.getWord(5))
    Assert.assertEquals(POSUtil.POS_NR, result.getPOS(4))
    Assert.assertEquals(POSUtil.POS_NR, result.getPOS(5))

    Assert.assertEquals("高峰", result.getWord(6))
    Assert.assertEquals(POSUtil.POS_NR, result.getPOS(6))
  }

  private def getSegmentResult(): SegmentResult = {
    val result = new SegmentResult(7)
    result.setWords(List[String]("张", "三", "丰", "南", "霸天", "高", "峰").toArray)
    result.setPOSArray(List[Int](POSUtil.POS_V, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_N, POSUtil.POS_N, POSUtil.POS_N, POSUtil.POS_N).toArray)
    result.setDomainTypes(List[Int](0, 0, 0, 0, 0, 0, 0).toArray)
    result.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A").toArray)
    result
  }

  class Filter extends AbstractSegmentFilter {
    override def doFilter() {
    }
  }

}
