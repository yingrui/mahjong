package websiteschema.mpsegment.core

import websiteschema.mpsegment.dict.POSUtil
import org.junit.Test
import org.junit.Assert._

class SegmentResultMergeTest {

  @Test
  def should_merge_two_words_into_one_word() {
    val segmentResult = SegmentResult(
      Array("一个", "几十", "万", "人口", "的", "社区"),
      Array(POSUtil.POS_M, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N)
    )

    segmentResult.merge(1, 2, POSUtil.POS_M)
    assertEquals("几十万", segmentResult.getWord(1))
    assertNull(segmentResult(2))
    assertEquals(6, segmentResult.length())

    segmentResult.compact()
    assertEquals(5, segmentResult.length())

    assertEquals(2, segmentResult.getWordStartAt(1))
    assertEquals(5, segmentResult.getWordEndAt(1))

    assertNotNull(segmentResult(2))
    assertEquals("几十万", segmentResult.getWord(1))
    assertEquals("人口", segmentResult.getWord(2))
  }

  @Test
  def should_separate_word_to_two_words() {
    val segmentResult = SegmentResult(
      Array("一个", "几十", "万", "人口", "的", "社区"),
      Array(POSUtil.POS_M, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N)
    )

    segmentResult.separate(0, 1, POSUtil.POS_M, POSUtil.POS_Q)

    assertEquals("一", segmentResult.getWord(0))
    assertEquals(0, segmentResult.getWordStartAt(0))
    assertEquals(1, segmentResult.getWordEndAt(0))

    assertEquals(POSUtil.POS_M, segmentResult.getPOS(0))
    assertEquals("N/A", segmentResult.getConcept(0))
    assertEquals("个", segmentResult.getWord(1))
    assertEquals(1, segmentResult.getWordStartAt(1))
    assertEquals(2, segmentResult.getWordEndAt(1))
    assertEquals(POSUtil.POS_Q, segmentResult.getPOS(1))
    assertEquals("N/A", segmentResult.getConcept(1))
  }

  @Test
  def should_adjust_adjacent_words() {
    val segmentResult = SegmentResult(
      Array("王国", "维", "是", "名人"),
      Array(POSUtil.POS_NR, POSUtil.POS_NR, POSUtil.POS_V, POSUtil.POS_N)
    )
    val wordIndex = 0
    val from = 1
    segmentResult.adjustAdjacentWords(wordIndex, from)
    assertEquals(4, segmentResult.length())

    assertEquals("王", segmentResult.getWord(0))
    assertEquals(0, segmentResult.getWordStartAt(0))
    assertEquals(1, segmentResult.getWordEndAt(0))

    assertEquals("国维", segmentResult.getWord(1))
    assertEquals(1, segmentResult.getWordStartAt(1))
    assertEquals(3, segmentResult.getWordEndAt(1))
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
    assertEquals("ji'shi'wan", segmentResult.getPinyin(1))
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
    assertEquals("abc", segmentResult.getPinyin(1))
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
    assertEquals("１２'ri", segmentResult.getPinyin(0))
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
    assertEquals("一个几十万", segmentResult.getWord(0))
    assertNull(segmentResult(1))
    assertNull(segmentResult(2))
    assertEquals(6, segmentResult.length())
    segmentResult.compact()
    assertEquals(4, segmentResult.length())
    assertNotNull(segmentResult(1))
    assertEquals("人口", segmentResult.getWord(1))
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