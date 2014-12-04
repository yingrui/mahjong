package websiteschema.mpsegment.crf

import org.junit.{Assert, Test}
import websiteschema.mpsegment.Assertion._

class CRFCorpusTest extends WithTestData {

  @Test
  def should_calculate_expected_probability {
    val model = new CRFModel(30, 2, new FeatureRepository(true), new FeatureRepository(false))
    val corpus = new CRFCorpus(Array(doc), model.featuresCount, model.labelCount, new FeatureRepository(true), new FeatureRepository(false))
    val occurrence = corpus.Ehat

    val feature0Y0Occurrence = occurrence(0)(0)
    val feature0Y1Occurrence = occurrence(0)(1)
    shouldBeEqual(1.0D, feature0Y0Occurrence)
    shouldBeEqual(0.0D, feature0Y1Occurrence)

    val feature15Y0Occurrence = occurrence(15)(0)
    val feature15Y1Occurrence = occurrence(15)(1)
    shouldBeEqual(1.0D, feature15Y0Occurrence)
    shouldBeEqual(1.0D, feature15Y1Occurrence)
  }

  @Test
  def should_parse_train_file {
    val trainingText =
      """Hello  O
        |Jenny  PER
      """.stripMargin
    val corpus = CRFCorpus(populateTrainingFile(trainingText))

    Assert.assertEquals(1, corpus.docs.length)

    Assert.assertTrue(corpus.featureRepository.contains("Hello"))
    Assert.assertTrue(corpus.featureRepository.contains("Jenny"))
    Assert.assertTrue(corpus.featureRepository.contains("label0+1"))
    Assert.assertTrue(corpus.featureRepository.contains("label0+0"))
    Assert.assertTrue(corpus.featureRepository.contains("label1+0"))
    Assert.assertTrue(corpus.featureRepository.contains("label1+1"))

    Assert.assertTrue(corpus.labelRepository.contains("O"))
    Assert.assertTrue(corpus.labelRepository.contains("PER"))
  }
}
