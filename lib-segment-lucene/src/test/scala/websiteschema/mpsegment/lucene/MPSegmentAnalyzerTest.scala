package websiteschema.mpsegment.lucene

import org.junit.{Assert, Test}
import java.io.StringReader
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute

class MPSegmentAnalyzerTest {

  @Test
  def should_tokenize_sentence {
    val analyzer = new MPSegmentAnalyzer
    val components = analyzer.createComponents("content", new StringReader("时间就是金钱"))
    val tokenStream = components.getTokenStream
    var terms = List[String]()
    while (tokenStream.incrementToken()) {
      terms = terms :+ tokenStream.getAttribute(classOf[CharTermAttribute]).toString
    }
    Assert.assertEquals(List("时间", "就", "是", "金钱"), terms)
  }

}
