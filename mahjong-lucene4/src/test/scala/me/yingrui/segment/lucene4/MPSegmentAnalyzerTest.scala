package me.yingrui.segment.lucene4

import org.junit.{Assert, Test}
import java.io.StringReader
import org.apache.lucene.analysis.tokenattributes.{PositionIncrementAttribute, TypeAttribute, OffsetAttribute, CharTermAttribute}

class MPSegmentAnalyzerTest {

  @Test
  def should_tokenize_sentence {
    val analyzer = new MPSegmentAnalyzer
    val components = analyzer.createComponents("content", new StringReader("时间就是金钱"))
    val tokenStream = components.getTokenStream
    var terms = List[String]()
    var types = List[String]()
    var offsets = List[(Int, Int)]()
    var positions = List[Int]()
    while (tokenStream.incrementToken()) {
      terms = terms :+ tokenStream.getAttribute(classOf[CharTermAttribute]).toString
      val offset = tokenStream.getAttribute(classOf[OffsetAttribute])
      offsets = offsets :+ (offset.startOffset(), offset.endOffset())
      types = types :+ tokenStream.getAttribute(classOf[TypeAttribute]).`type`()
      positions = positions :+ tokenStream.getAttribute(classOf[PositionIncrementAttribute]).getPositionIncrement
    }
    Assert.assertEquals(List("时间", "就", "是", "金钱"), terms)
    Assert.assertEquals(List((0,2), (2,3), (3,4), (4,6)), offsets)
    Assert.assertEquals(List("N", "D", "V", "N"), types)
    Assert.assertEquals(List(1, 2, 3, 4), positions)
  }

  @Test
  def should_filter_space {
    val analyzer = new MPSegmentAnalyzer
    val components = analyzer.createComponents("content", new StringReader("Time is money"))
    val tokenStream = components.getTokenStream
    var terms = List[String]()
    var types = List[String]()
    var offsets = List[(Int, Int)]()
    var positions = List[Int]()
    while (tokenStream.incrementToken()) {
      terms = terms :+ tokenStream.getAttribute(classOf[CharTermAttribute]).toString
      val offset = tokenStream.getAttribute(classOf[OffsetAttribute])
      offsets = offsets :+ (offset.startOffset(), offset.endOffset())
      types = types :+ tokenStream.getAttribute(classOf[TypeAttribute]).`type`()
      positions = positions :+ tokenStream.getAttribute(classOf[PositionIncrementAttribute]).getPositionIncrement
    }
    Assert.assertEquals(List("TIME", "IS", "MONEY"), terms)
    Assert.assertEquals(List((0,4), (5,7), (8,13)), offsets)
    Assert.assertEquals(List("N", "R", "N"), types)
    Assert.assertEquals(List(1, 2, 3), positions)
  }

}
