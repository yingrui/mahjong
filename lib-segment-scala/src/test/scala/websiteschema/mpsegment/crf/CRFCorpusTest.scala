package websiteschema.mpsegment.crf

import org.junit.Test
import websiteschema.mpsegment.Assertion._

class CRFCorpusTest extends WithTestData {

  @Test
  def should_calculate_expected_probability {
    val model = new CRFModel
    val corpus = new CRFCorpus(Array(doc), model)
    val occurrence = corpus.getFeatureOccurrence

    val feature0Y0Occurrence = occurrence(0)(0)
    val feature0Y1Occurrence = occurrence(0)(1)
    shouldBeEqual(1.0D, feature0Y0Occurrence)
    shouldBeEqual(0.0D, feature0Y1Occurrence)

    val feature15Y0Occurrence = occurrence(15)(0)
    val feature15Y1Occurrence = occurrence(15)(1)
    shouldBeEqual(1.0D, feature15Y0Occurrence)
    shouldBeEqual(1.0D, feature15Y1Occurrence)
  }

}
