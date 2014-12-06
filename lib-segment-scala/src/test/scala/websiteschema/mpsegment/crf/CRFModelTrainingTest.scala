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

    var total = 0
    var correctCount = 0
    for(i <- 0 until corpus.docs.length) {
      val result = classifier.calculateResult(corpus.docs(i).data)
      val path = result.getBestPath
      path.foreach(println(_))
      total += path.length
      for(index <- 0 until path.length) {
        val label = corpus.docs(i).label(index)
        correctCount += (if(label == path(index)) 1 else 0)
      }
    }
    println(correctCount.toDouble / total.toDouble)
    Assert.assertEquals(correctCount, total)
  }
}





