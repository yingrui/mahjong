package me.yingrui.segment.crf

import org.junit.{Assert, Test}
import me.yingrui.segment.Assertion._
import me.yingrui.segment.TestHelper._

class CRFCorpusTest extends WithTestData {

  @Test
  def should_calculate_expected_probability {
    val corpus = new CRFCorpus(Array(doc), featureRepository, labelRepository)
    val occurrence = corpus.occurrence

    val feature0Y0Occurrence = occurrence(0, 0)
    val feature0Y1Occurrence = occurrence(0, 1)
    shouldBeEqual(1.0D, feature0Y0Occurrence)
    shouldBeEqual(0.0D, feature0Y1Occurrence)

    val feature15Y0Occurrence = occurrence(15, 0)
    val feature15Y1Occurrence = occurrence(15, 1)
    shouldBeEqual(1.0D, feature15Y0Occurrence)
    shouldBeEqual(1.0D, feature15Y1Occurrence)
  }

  @Test
  def should_parse_train_file {
    val trainingText =
      """Hello  O
        |Jenny  PER
        |!      O
      """.stripMargin
    val corpus = CRFCorpus(createTempFile(trainingText))

    Assert.assertEquals(1, corpus.docs.length)

    Assert.assertEquals(19, corpus.featureRepository.size)
    Assert.assertTrue(corpus.featureRepository.contains("Hello"))
    Assert.assertTrue(corpus.featureRepository.contains("Jenny"))
    Assert.assertTrue(corpus.featureRepository.contains("!"))

    Assert.assertTrue(corpus.featureRepository.contains("label0"))
    Assert.assertTrue(corpus.featureRepository.contains("label1"))

    Assert.assertTrue(corpus.featureRepository.contains("n1->!"))
    Assert.assertTrue(corpus.featureRepository.contains("n1->Jenny"))
    Assert.assertTrue(corpus.featureRepository.contains("n2->!"))

    Assert.assertTrue(corpus.featureRepository.contains("p1->Hello"))
    Assert.assertTrue(corpus.featureRepository.contains("p1->Jenny"))
    Assert.assertTrue(corpus.featureRepository.contains("p2->Hello"))

    Assert.assertTrue(corpus.featureRepository.contains("pcn-word->Hello-Jenny-!"))

    Assert.assertTrue(corpus.featureRepository.contains("pc-word->Jenny-!"))
    Assert.assertTrue(corpus.featureRepository.contains("pc-word->Hello-Jenny"))
    Assert.assertTrue(corpus.featureRepository.contains("p2p1c-word->Hello-Jenny-!"))

    Assert.assertTrue(corpus.featureRepository.contains("cn-word->Jenny-!"))
    Assert.assertTrue(corpus.featureRepository.contains("cn-word->Hello-Jenny"))
    Assert.assertTrue(corpus.featureRepository.contains("cn1n2-word->Hello-Jenny-!"))

    Assert.assertTrue(corpus.labelRepository.contains("O"))
    Assert.assertTrue(corpus.labelRepository.contains("PER"))
  }
}
