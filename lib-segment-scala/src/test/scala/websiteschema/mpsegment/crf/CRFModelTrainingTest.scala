package websiteschema.mpsegment.crf

import org.junit.{Assert, Test}

class CRFModelTrainingTest extends WithTestData {

  @Test
  def should_train_a_model {
    val model = new CRFModel(30, 2, new FeatureRepository(true), new FeatureRepository(false))
    val corpus = new CRFCorpus(Array(doc), 30, 2, new FeatureRepository(true), new FeatureRepository(false))
    CRFModel.build(corpus)
  }

  @Test
  def should_train_a_model_from_file {
    val trainingText =
      """Hello  O
        |Jenny  PER
      """.stripMargin
    val corpus = CRFCorpus(populateTrainingFile(trainingText))
    val model = CRFModel.build(corpus)

    val classifier = new CRFViterbi(model)

    val result = classifier.calculateResult(corpus.docs(0).data)
    val path = result.getBestPath
    path.foreach(println(_))
    Assert.assertArrayEquals(label, path)
  }
}





