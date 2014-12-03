package websiteschema.mpsegment.crf

import java.io.FileWriter

import org.junit.Test
import websiteschema.mpsegment.Assertion._

import scala.io.Source

class CRFCorpusTest extends WithTestData {

  @Test
  def should_calculate_expected_probability {
    val model = new CRFModel
    val corpus = new CRFCorpus(Array(doc), model.featuresCount, model.labelCount)
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
    println(trainingText)
  }

  private def populateTrainingFile(text: String) = {
    val file = java.io.File.createTempFile("crf-corpus", "txt")
    file.deleteOnExit()
    val output = new FileWriter(file)
    output.write(text)
    output.close()
    file.getAbsolutePath
  }
}
