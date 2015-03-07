package websiteschema.mpsegment.core

import org.junit.Assert._
import org.junit.Test

class SegmentResultTest {

  @Test
  def should_return_original_index_of_word {
    val segmentResult = SegmentWorker(
      "segment.lang.en = true",
      "segment.lang.en.stemming = true").segment("我们，He loves us！")
    println(segmentResult)
    assertEquals(0, segmentResult.getWordStartAt(0))
    assertEquals(2, segmentResult.getWordStartAt(1))
    assertEquals(3, segmentResult.getWordStartAt(2))
    assertEquals(6, segmentResult.getWordStartAt(3))
    assertEquals(12, segmentResult.getWordStartAt(4))
    assertEquals(14, segmentResult.getWordStartAt(5))

    assertEquals(2, segmentResult.getWordEndAt(0))
    assertEquals(3, segmentResult.getWordEndAt(1))
    assertEquals(5, segmentResult.getWordEndAt(2))
    assertEquals(11, segmentResult.getWordEndAt(3))
    assertEquals(14, segmentResult.getWordEndAt(4))
    assertEquals(15, segmentResult.getWordEndAt(5))
  }
}
