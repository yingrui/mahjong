package websiteschema.mpsegment.core

import org.junit.Assert
import org.junit.Test
import websiteschema.mpsegment.dict.POSUtil

class SegmentResultTest {

  @Test
  def should_merge_two_words_into_one_word() {
    val segmentResult = new SegmentResult(6)
    segmentResult.setWords(List("一个", "几十", "万", "人口", "的", "社区").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_M, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List("N/A", "N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    segmentResult.merge(1, 2, POSUtil.POS_M)
    Assert.assertEquals("几十万", segmentResult.getWord(1))
    Assert.assertNull(segmentResult(2))
    Assert.assertEquals(6, segmentResult.length())
    segmentResult.compact()
    Assert.assertEquals(5, segmentResult.length())
    Assert.assertNotNull(segmentResult(2))
    Assert.assertEquals("人口", segmentResult.getWord(2))
  }

  @Test
  def should_separate_word_to_two_words() {
    val segmentResult = new SegmentResult(6)
    segmentResult.setWords(List("一个", "几十", "万", "人口", "的", "社区").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_M, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List("N/A", "N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    segmentResult.separate(0, 1, POSUtil.POS_M, POSUtil.POS_Q)
    Assert.assertEquals("一", segmentResult.getWord(0))
    Assert.assertEquals(POSUtil.POS_M, segmentResult.getPOS(0))
    Assert.assertEquals("N/A", segmentResult.getConcept(0))
    Assert.assertEquals("个", segmentResult.getWord(1))
    Assert.assertEquals(POSUtil.POS_Q, segmentResult.getPOS(1))
    Assert.assertEquals("N/A", segmentResult.getConcept(1))
  }

  @Test
  def should_merge_pinyin_of_Chinese_words_correctly_when_merge_words() {
    val segmentResult = new SegmentResult(6)
    segmentResult.setWords(List("一个", "几十", "万", "人口", "的", "社区").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_M, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0, 0).toArray)
    segmentResult.setPinyin(List("yi'ge", "ji'shi", "wan", "ren'kou", "de", "she'qu").toArray)
    segmentResult.setConcepts(List("N/A", "N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    segmentResult.merge(1, 2, POSUtil.POS_M)
    Assert.assertEquals("ji'shi'wan", segmentResult.getPinyin(1))
  }

  @Test
  def should_merge_pinyin_of_other_words_correctly_when_merge_words() {
    val segmentResult = new SegmentResult(4)
    segmentResult.setWords(List("1个", "a", "b", "c").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_M, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0).toArray)
    segmentResult.setPinyin(List("yi'ge", "a", "b", "c").toArray)
    segmentResult.setConcepts(List("N/A", "N/A", "N/A", "N/A").toArray)

    segmentResult.merge(1, 3, POSUtil.POS_UNKOWN)
    Assert.assertEquals("abc", segmentResult.getPinyin(1))
  }

  @Test
  def should_merge_pinyin_of_date_words_correctly_when_merge_words() {
    val segmentResult = new SegmentResult(2)
    segmentResult.setWords(List("１", "２日").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_M, POSUtil.POS_T).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0).toArray)
    segmentResult.setPinyin(List("１", "２'ri").toArray)
    segmentResult.setConcepts(List("N/A", "N/A").toArray)

    segmentResult.merge(0, 1, POSUtil.POS_T)
    Assert.assertEquals("１２'ri", segmentResult.getPinyin(0))
  }

  @Test
  def should_merge_multi_words_by_two_steps() {
    val segmentResult = new SegmentResult(6)
    segmentResult.setWords(List("一个", "几十", "万", "人口", "的", "社区").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List("N/A", "N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    segmentResult.merge(0, 1, POSUtil.POS_M)
    segmentResult.merge(1, 2, POSUtil.POS_M)
    Assert.assertEquals("一个几十万", segmentResult.getWord(0))
    Assert.assertNull(segmentResult(1))
    Assert.assertNull(segmentResult(2))
    Assert.assertEquals(6, segmentResult.length())
    segmentResult.compact()
    Assert.assertEquals(4, segmentResult.length())
    Assert.assertNotNull(segmentResult(1))
    Assert.assertEquals("人口", segmentResult.getWord(1))
  }

  @Test
  def should_merge_multi_words() {
    val segmentResult = new SegmentResult(12)
    segmentResult.setWords(List("（", "一", "九", "九", "七", "年", "十二月", "三", "十", "一", "日", "）").toArray)
    segmentResult.setPOSArray(List[Int](POSUtil.POS_UNKOWN, POSUtil.POS_M, POSUtil.POS_M, POSUtil.POS_M, POSUtil.POS_M, POSUtil.POS_Q, POSUtil.POS_T, POSUtil.POS_M, POSUtil.POS_M, POSUtil.POS_M, POSUtil.POS_Q, POSUtil.POS_UNKOWN).toArray)
    segmentResult.setDomainTypes(List[Int](0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0).toArray)
    segmentResult.setConcepts(List("N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A").toArray)

    segmentResult.merge(1, 4, POSUtil.POS_M)
    segmentResult.merge(4, 5, POSUtil.POS_T)
    segmentResult.merge(7, 9, POSUtil.POS_M)
    segmentResult.merge(9, 10, POSUtil.POS_T)
    segmentResult.compact()
    println(segmentResult)
  }

}
