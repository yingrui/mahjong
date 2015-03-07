package websiteschema.mpsegment.performance

import junit.framework.Assert
import org.junit.Test
import websiteschema.mpsegment.core.SegmentWorker
import websiteschema.mpsegment.tools.accurary.{NerNameStatisticData, ErrorAnalyzer, SegmentAccuracy}
import websiteschema.mpsegment.tools.accurary.SegmentErrorType._

class AccuracyTest {

  val debug = false

  @Test
  def should_be_higher_than_93_percent_with_segment_minimum() {
    val segmentWorker = SegmentWorker(
        "separate.xingming -> true",
//        "segment.bigram -> word-bigram.dat",
        "minimize.word -> true"
      )
    val segmentAccuracy = new SegmentAccuracy("PFR-199801-utf-8.txt", segmentWorker)
    segmentAccuracy.checkSegmentAccuracy()
    println("Accuracy rate of segment is: " + segmentAccuracy.getAccuracyRate())
    println("There are " + segmentAccuracy.getWrong() + " errors and total expect word is " + segmentAccuracy.getTotalWords() + " when doing accuracy test.")

    println("There are " + segmentAccuracy.getErrorAnalyzer(UnknownWord).getErrorOccurTimes() + " errors because of new word.")
    println("There are " + segmentAccuracy.getErrorAnalyzer(NER_NR).getErrorOccurTimes() + " errors because of name recognition.")
    println("There are " + segmentAccuracy.getErrorAnalyzer(NER_NS).getErrorOccurTimes() + " errors because of place name recognition.")
    println("There are " + segmentAccuracy.getErrorAnalyzer(ContainDisambiguate).getErrorOccurTimes() + " errors because of contain disambiguate.")
    println("There are " + segmentAccuracy.getErrorAnalyzer(Other).getErrorOccurTimes() + " other errors")

    printDetails(segmentAccuracy)

    Assert.assertTrue(segmentAccuracy.getAccuracyRate() > 0.94021 * 0.99)
    Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(UnknownWord).getErrorOccurTimes() <= 23000 * 1.05)
    Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(NER_NR).getErrorOccurTimes() <= 3000 * 1.05)
    Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(NER_NS).getErrorOccurTimes() <= 3200 * 1.05)
    Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(ContainDisambiguate).getErrorOccurTimes() <= 33000 * 1.05)
    Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(Other).getErrorOccurTimes() <= 3800 * 1.05)
  }

  @Test
  def should_recognize_all_the_name_entities() {
    val segmentWorker = SegmentWorker(
        "separate.xingming -> true",
        "minimize.word -> true"
      )

    val segmentAccuracy = new SegmentAccuracy("test-pfr-corpus.txt", segmentWorker)
    segmentAccuracy.checkSegmentAccuracy()
    println("Accuracy rate of segment is: " + segmentAccuracy.getAccuracyRate())
    println("There are " + segmentAccuracy.getWrong() + " errors and total expect word is " + segmentAccuracy.getTotalWords() + " when doing accuracy test.")

    println("There are " + segmentAccuracy.getErrorAnalyzer(UnknownWord).getErrorOccurTimes() + " errors because of new word.")
    println("There are " + segmentAccuracy.getErrorAnalyzer(NER_NR).getErrorOccurTimes() + " errors because of name recognition.")
    println("There are " + segmentAccuracy.getErrorAnalyzer(NER_NS).getErrorOccurTimes() + " errors because of place name recognition.")
    println("There are " + segmentAccuracy.getErrorAnalyzer(ContainDisambiguate).getErrorOccurTimes() + " errors because of contain disambiguate.")
    println("There are " + segmentAccuracy.getErrorAnalyzer(Other).getErrorOccurTimes() + " other errors")

    printDetails(segmentAccuracy)

    Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(NER_NR).getErrorOccurTimes() <= 4)
  }

  @Test
  def should_be_higher_than_93_percent() {
    val segmentWorker = SegmentWorker("separate.xingming -> true")
    val segmentAccuracy = new SegmentAccuracy("PFR-199801-utf-8.txt", segmentWorker)
    segmentAccuracy.checkSegmentAccuracy()
    println("Accuracy rate of segment is: " + segmentAccuracy.getAccuracyRate())
    println("There are " + segmentAccuracy.getWrong() + " errors and total expect word is " + segmentAccuracy.getTotalWords() + " when doing accuracy test.")

    println("There are " + segmentAccuracy.getErrorAnalyzer(UnknownWord).getErrorOccurTimes() + " errors because of new word.")
    println("There are " + segmentAccuracy.getErrorAnalyzer(NER_NR).getErrorOccurTimes() + " errors because of name recognition.")
    println("There are " + segmentAccuracy.getErrorAnalyzer(NER_NS).getErrorOccurTimes() + " errors because of place name recognition.")
    println("There are " + segmentAccuracy.getErrorAnalyzer(ContainDisambiguate).getErrorOccurTimes() + " errors because of contain disambiguate.")
    println("There are " + segmentAccuracy.getErrorAnalyzer(Other).getErrorOccurTimes() + " other errors")

    printDetails(segmentAccuracy)

    Assert.assertTrue(segmentAccuracy.getAccuracyRate() > 0.94000 * 0.99)
    Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(UnknownWord).getErrorOccurTimes() <= 23000 * 1.05)
    Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(NER_NR).getErrorOccurTimes() <= 3000 * 1.05)
    Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(NER_NS).getErrorOccurTimes() <= 3200 * 1.05)
    Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(ContainDisambiguate).getErrorOccurTimes() <= 34000 * 1.05)
    Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(Other).getErrorOccurTimes() <= 3800 * 1.05)
  }

  private def printDetails(segmentAccuracy: SegmentAccuracy) {
    if (debug) {
      println("Possible " + segmentAccuracy.getErrorAnalyzer(UnknownWord).getWords().size + " new words")
      println("Total count: " + getWordsCount(segmentAccuracy.getErrorAnalyzer(UnknownWord)) + ", they are:")
      println(segmentAccuracy.getErrorAnalyzer(UnknownWord).getWords())
      println("Those " + segmentAccuracy.getErrorAnalyzer(ContainDisambiguate).getWords().size + " words maybe could delete from dictionary")
      println("Total count: " + getWordsCount(segmentAccuracy.getErrorAnalyzer(ContainDisambiguate)) + ", they are:")
      println(segmentAccuracy.getErrorAnalyzer(ContainDisambiguate).getWords())
    }
    NerNameStatisticData.print
  }

  private def getWordsCount(errorAnalyzer: ErrorAnalyzer): Int = {
    val words = errorAnalyzer.getWords()
    var count = 0
    for (word <- words.keys) {
      count += words(word)
    }
    return count
  }
}
