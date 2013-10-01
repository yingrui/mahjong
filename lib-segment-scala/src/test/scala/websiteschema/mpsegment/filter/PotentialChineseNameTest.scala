package websiteschema.mpsegment.filter

import ner.{RecognizerCreator, NameEntityRecognizeResult, NameEntityRecognizer}
import org.junit.Assert
import org.junit.Test
import websiteschema.mpsegment.core.SegmentResult
import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.tools.PFRCorpusLoader

class PotentialChineseNameTest {

  var startWithXing = false
  var isForeignName = false
  var potentialWords = List[(Int, Int)]()
  class FakeChineseNameRecognizer(val segmentResult: SegmentResult) extends NameEntityRecognizer {
    def recognizeNameWordBetween(begin: Int, end: Int): NameEntityRecognizeResult = {
      potentialWords = potentialWords :+ (begin, end)
      val wordCount = end - begin + 1
      new NameEntityRecognizeResult(if(wordCount > 3) 3 else wordCount, startWithXing, isForeignName)
    }
  }

  class FakeRecognizerCreator extends RecognizerCreator {
    def create(segmentResult: SegmentResult): NameEntityRecognizer = new FakeChineseNameRecognizer(segmentResult)
  }

  val corpusLoader = PFRCorpusLoader()
  val configuration: MPSegmentConfiguration = MPSegmentConfiguration(Map("separate.xingming" -> "true"))
  var filter = new ChineseNameFilter(configuration, new FakeRecognizerCreator(), 4)

  @Test
  def should_start_with_xing() {
    val segmentResult = corpusLoader.buildSegmentResult("19980103-02-003-016/m 当/p 王/nr 菲/nr  和/v  李/nr  亚/nr  鹏/nr")

    filtering(segmentResult)

    Assert.assertEquals(1, potentialWords(0)._1)
  }

  @Test
  def should_only_contains_single_characters() {
    val segmentResult = corpusLoader.buildSegmentResult("19980103-02-003-016/m  王/nr 菲/nr  和平/v  李/nr  亚/nr  鹏/nr")

    filtering(segmentResult)

    Assert.assertEquals(0, potentialWords(0)._1)
    Assert.assertEquals(1, potentialWords(0)._2)
  }

  @Test
  def should_only_contains_Chinese_characters() {
    val segmentResult = corpusLoader.buildSegmentResult("19980103-02-003-016/m  王/nr 菲/nr  ,/w  李/nr  亚/nr  鹏/nr")

    filtering(segmentResult)

    Assert.assertEquals(0, potentialWords(0)._1)
    Assert.assertEquals(1, potentialWords(0)._2)
  }

  @Test
  def should_only_contains_up_to_four_characters() {
    val segmentResult = corpusLoader.buildSegmentResult("19980103-02-003-016/m  王/nr  菲/nr  李/nr  亚/nr  鹏/nr")

    filtering(segmentResult)

    Assert.assertEquals(0, potentialWords(0)._1)
    Assert.assertEquals(3, potentialWords(0)._2)
  }

  @Test
  def should_continue_with_the_last_recognized_name() {
    val segmentResult = corpusLoader.buildSegmentResult("19980103-02-003-016/m  当/p  李/nr  亚/nr  鹏/nr  王/nr  菲/nr")

    filtering(segmentResult)

    Assert.assertEquals(1, potentialWords(0)._1)
    Assert.assertEquals(4, potentialWords(0)._2)

    Assert.assertEquals(4, potentialWords(1)._1)
    Assert.assertEquals(5, potentialWords(1)._2)
  }

  def filtering(segmentResult: SegmentResult) {
    filter.setSegmentResult(segmentResult)
    filter.doFilter()
    filter.compactSegmentResult()
  }
}
