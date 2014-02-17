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
  def should_recognize_xing_with_prefix {
    val segmentResult = new SegmentResult(6)
    segmentResult.setWords(List[String]("老", "张", "来", "到", "了", "社区").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_N, POSUtil.POS_N, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    filter.setSegmentResult(segmentResult)
    filter.filtering()
    Assert.assertEquals("老张", segmentResult.getWord(0))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(0))
  }

  @Test
  def should_recognize_xing_with_suffix {
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
  def should_recognize_xing_with_double_name {
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
  def should_recognize_xing_with_double_character_word_as_name {
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
  def should_recognize_xing_with_single_name {
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
  def should_recognize_word_contains_xing_and_name {
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

  @Test
  def should_recognize_normal_word_composite_of_xing_and_name {
    val segmentResult = new SegmentResult(5)
    segmentResult.setWords(List[String]("主席", "汪洋", "发表", "了", "社区").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_N, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    filter.setSegmentResult(segmentResult)
    filter.filtering()
    Assert.assertEquals("汪", segmentResult.getWord(1))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(1))
    Assert.assertEquals("洋", segmentResult.getWord(2))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(2))
  }

  @Test
  def should_recognize_last_word_overlap_with_xing {
    val segmentResult = new SegmentResult(5)
    segmentResult.setWords(List[String]("昨天", "同江", "泽民", "主席", "进行").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_N, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    filter.setSegmentResult(segmentResult)
    filter.filtering()
    Assert.assertEquals("同", segmentResult.getWord(1))
    Assert.assertEquals("江", segmentResult.getWord(2))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(2))
  }

  @Test
  def should_recognize_last_word_overlap_with_xing_and_have_double_names {
    val segmentResult = new SegmentResult(5)
    segmentResult.setWords(List[String]("对白", "晓", "燕", "绑架", "案").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_N, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    filter.setSegmentResult(segmentResult)
    filter.filtering()
    Assert.assertEquals("白", segmentResult.getWord(1))
    Assert.assertEquals("晓燕", segmentResult.getWord(2))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(1))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(2))
  }

  @Test
  def should_recognize_overlap_with_next_word {
    val segmentResult = new SegmentResult(5)
    segmentResult.setWords(List[String]("石", "宝", "良家", "的", "炕上").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_N, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    filter.setSegmentResult(segmentResult)
    filter.filtering()
    Assert.assertEquals("石", segmentResult.getWord(0))
    Assert.assertEquals("宝良", segmentResult.getWord(1))
    Assert.assertEquals("家", segmentResult.getWord(2))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(0))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(1))
  }

  @Test
  def should_recognize_overlap_with_next_word_when_there_is_single_name {
    val segmentResult = new SegmentResult(5)
    segmentResult.setWords(List[String]("石", "良家", "的", "炕上", "。").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_N, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    filter.setSegmentResult(segmentResult)
    filter.filtering()
    Assert.assertEquals("石", segmentResult.getWord(0))
    Assert.assertEquals("良", segmentResult.getWord(1))
    Assert.assertEquals("家", segmentResult.getWord(2))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(0))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(1))
  }

  @Test
  def should_recognize_foreign_name_fu_luo_si {
    val segmentResult = new SegmentResult(5)
    segmentResult.setWords(List[String]("福", "诺", "斯", "在", "该所").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_N, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    filter.setSegmentResult(segmentResult)
    filter.filtering()
    Assert.assertEquals("福诺斯", segmentResult.getWord(0))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(0))
  }

  @Test
  def should_recognize_foreign_name_sai_fu {
    val segmentResult = new SegmentResult(2)
    segmentResult.setWords(List[String]("塞", "夫").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_N, POSUtil.POS_M).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0).toArray)
    segmentResult.setConcepts(List[String]("N/A", "N/A").toArray)

    filter.setSegmentResult(segmentResult)
    filter.filtering()
    Assert.assertEquals("塞夫", segmentResult.getWord(0))
    Assert.assertEquals(POSUtil.POS_NR, segmentResult.getPOS(0))
  }

  @Test
  def should_recognize_foreign_name_sai_fu_1 {
    val segmentResult = new SegmentResult(3)
    segmentResult.setWords(List[String]("冯", "英", "睿").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_NR, POSUtil.POS_NR, POSUtil.POS_NR).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0).toArray)
    segmentResult.setConcepts(List[String]("N/A", "N/A", "N/A").toArray)

    filter.setSegmentResult(segmentResult)
    filter.filtering()
    println(segmentResult)
  }

  @Test
  def should_return_possible_appendix_states {
//    val appendixStates = HmmNameFilter.getAppendixStates("和")
//    assert(appendixStates.contains("B"), "和 is one of xing")
  }
}
