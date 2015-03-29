package me.yingrui.segment.graph

import org.junit.Assert
import org.junit.Test
import me.yingrui.segment.tools.WordBigramBuilder
import java.io.File

class WordBigramTest {

  @Test
  def should_save_and_load_model() {
    val builder = new WordBigramBuilder()
    builder.train("src/test/resources/test-pfr-corpus.txt")
    builder.build()
    builder.save("test.dat")
    val wordBigram = WordBigram(builder.getTrie(), builder.getNodeRepository())
    val prob = wordBigram.getProbability("好", "地")

    val bigram = WordBigram("test.dat")
    val prob1 = bigram.getProbability("好", "地")
    Assert.assertTrue(prob1 - prob < 0.0000001D)

    val prob2 = bigram.getProbability("non", "exists")
    Assert.assertTrue(prob2 < 0.00000000000001D)
    (new File("test.dat")).deleteOnExit()
  }
}
