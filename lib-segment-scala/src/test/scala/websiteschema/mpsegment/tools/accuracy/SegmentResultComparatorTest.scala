package websiteschema.mpsegment.tools.accuracy

import org.junit.Assert
import org.junit.Test

import java.io.ByteArrayInputStream
import java.io.InputStream
import websiteschema.mpsegment.tools.PFRCorpusLoader
import websiteschema.mpsegment.core.Word
import websiteschema.mpsegment.tools.accurary.{SegmentResultComparator, SegmentResultCompareHook}

class SegmentResultComparatorTest {

  class StubHooker extends SegmentResultCompareHook {
    var errorCount = 0
    var words = List[Word]()

    override def errorWordHook = errorCount += 1

    override def correctWordHook(expectWord: Word, matchedWord: Word, expectWordIndex: Int, matchedWordIndex: Int) {
      words = words :+ matchedWord
    }

    override def foundError(expect: Word, word: Word, expectWordIndex: Int, errorWordIndex: Int) {
      words = words :+ word
    }
  }

  @Test
  def should_contains_one_error_compare_to_result1() {
    val expect = convertToSegmentResult("19980101-02-005-001/m  [国务院/nt  侨办/j]nt  发表/v  新年/t  贺词/n ")
    val actual = convertToSegmentResult("19980101-02-005-001/m  国务院/nt  侨/j 办/v  发表/v  新年/t  贺词/n ")

    val hooker = new StubHooker
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    Assert.assertEquals(1, hooker.errorCount)
  }

  @Test
  def should_contains_two_error_compare_to_result2() {
    val expect = convertToSegmentResult("19980101-02-005-001/m  国务院/nt  侨/j 办/v  发表/v  新年/t  贺词/n ")
    val actual = convertToSegmentResult("19980101-02-005-001/m  [国务院/nt  侨办/j]nt  发表/v  新年/t  贺词/n ")

    val hooker = new StubHooker
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    Assert.assertEquals(2, hooker.errorCount)
  }

  @Test
  def should_traverse_all_words_when_actual_words_is_longer_than_expected() {
    val expect = convertToSegmentResult("19980101-02-005-001/m  [国务院/nt  侨办/j]nt  发表/v  新年/t  贺词/n ")
    val actual = convertToSegmentResult("19980101-02-005-001/m  国务院/nt  侨/j 办/v  发表/v  新年/t  贺词/n ")

    val hooker = new StubHooker
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    Assert.assertEquals(actual.length, hooker.words.size)
    val joined: String = hooker.words.foldRight("") {
      (a, b) => a.name + b
    }
    Assert.assertEquals(actual.toOriginalString(), joined)
  }

  @Test
  def should_traverse_all_words_when_actual_results_is_shorter_than_expected() {
    val expect = convertToSegmentResult("19980101-02-005-001/m  国务院/nt  侨/j 办/v  发表/v  新年/t  贺词/n ")
    val actual = convertToSegmentResult("19980101-02-005-001/m  [国务院/nt  侨办/j]nt  发表/v  新年/t  贺词/n ")

    val hooker = new StubHooker
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    Assert.assertEquals(actual.length, hooker.words.size)
    val joined: String = hooker.words.foldRight("") {
      (a, b) => a.name + b
    }
    Assert.assertEquals(actual.toOriginalString(), joined)
  }

  @Test
  def should_traverse_all_words() {
    val expect = convertToSegmentResult("19980101-02-005-001/m  国务院/nt  侨办/j  发表/v  新年/t  贺词/n ")
    val actual = convertToSegmentResult("19980101-02-005-001/m  国务/n 院侨/j 办/v  发表/v  新年/t  贺词/n ")

    val hooker = new StubHooker
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    Assert.assertEquals(actual.length, hooker.words.size)
    val joined: String = hooker.words.foldRight("") {
      (a, b) => a.name + b
    }
    Assert.assertEquals(actual.toOriginalString(), joined)
  }

  private def convertToSegmentResult(text: String) = PFRCorpusLoader(convertToInputStream(text)).readLine()

  private def convertToInputStream(text: String): InputStream = new ByteArrayInputStream(text.getBytes("utf-8"))

}
