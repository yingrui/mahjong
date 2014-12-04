package websiteschema.mpsegment.crf

import org.junit.{Assert, Test}

class CRFModelTrainingTest extends WithTestData {

  @Test
  def should_train_a_model {
    val corpus = new CRFCorpus(Array(doc), featureRepository, labelRepository)

    val m = CRFModel.build(corpus)

    val file = java.io.File.createTempFile("crf-model", ".dat")

    println(file.getAbsolutePath)
    file.deleteOnExit()
    CRFModel.save(m, file.getAbsolutePath)

    val model = CRFModel.apply(file.getAbsolutePath)
    val classifier = new CRFViterbi(model)

    val result = classifier.calculateResult(testData)
    val path = result.getBestPath
    path.foreach(println(_))
  }

  @Test
  def should_train_a_model_from_file {
    val trainingText =
      """Hello  O
        |Jenny  PER
        |,  O
        |This O
        |is O
        |Ben PER
        |
        |Ben PER
        |This O
        |is O
        |Jenny  PER
      """.stripMargin
    val corpus = CRFCorpus(populateTrainingFile(trainingText))
    val model = CRFModel.build(corpus)

    val classifier = new CRFViterbi(model)

    val result = classifier.calculateResult(corpus.docs(0).data)
    val path = result.getBestPath
    path.foreach(println(_))
    Assert.assertArrayEquals(Array(0, 1, 0, 0, 0, 1), path)
  }
}





