package me.yingrui.segment.crf

import org.junit.{Assert, Test}
import me.yingrui.segment.TestHelper._

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
    val labelRepository = new FeatureRepository(false)
    val corpus = CRFCorpus(createTempFile(trainingText), true, true, new FeatureRepository(true), labelRepository)
    val model = CRFModel.build(corpus)

    val classifier = new CRFClassifier(model)

    var total = 0
    var correctCount = 0
    for(i <- 0 until corpus.docs.length) {
      val input = corpus.docs(i).rowData.map(str => str.substring(0, str.indexOf(" ")))
      val path = classifier.findBestLabels(input)
      path.foreach(println(_))
      total += path.length
      for(index <- 0 until path.length) {
        val label = corpus.docs(i).label(index)
        correctCount += (if(label == labelRepository.getFeatureId(path(index))) 1 else 0)
      }
    }
    println(correctCount.toDouble / total.toDouble)
    Assert.assertEquals(correctCount, total)
  }
}





