package me.yingrui.segment.lucene5

import java.io.StringReader

import org.apache.lucene.analysis.tokenattributes.{CharTermAttribute, OffsetAttribute, PositionIncrementAttribute}
import org.junit.{Assert, Test}

class MPSegmentAnalyzerTest {

  @Test
  def should_tokenize_sentence {
    val analyzer = new MPSegmentAnalyzer
    val reader = new StringReader("时间就是金钱")
    val tokenStream = analyzer.tokenStream("content", reader)
    tokenStream.reset()

    var terms = List[String]()
    var offsets = List[(Int, Int)]()
    var positions = List[Int]()
    while (tokenStream.incrementToken()) {
      terms = terms :+ tokenStream.getAttribute(classOf[CharTermAttribute]).toString
      val offset = tokenStream.getAttribute(classOf[OffsetAttribute])
      offsets = offsets :+ (offset.startOffset(), offset.endOffset())
      positions = positions :+ tokenStream.getAttribute(classOf[PositionIncrementAttribute]).getPositionIncrement
    }
    Assert.assertEquals(List("时间", "就", "是", "金钱"), terms)
    Assert.assertEquals(List((0,2), (2,3), (3,4), (4,6)), offsets)
    Assert.assertEquals(List(1, 2, 3, 4), positions)
  }
}
