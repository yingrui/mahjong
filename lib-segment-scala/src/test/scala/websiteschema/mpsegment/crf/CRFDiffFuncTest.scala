package websiteschema.mpsegment.crf

import org.junit.Assert._
import org.junit.Test
import websiteschema.mpsegment.Assertion._


class CRFDiffFuncTest extends WithTestData {

  @Test
  def should_evaluate_value_at_given_initiative_input {
    val model = new CRFModel
    val corpus = new CRFCorpus(Array(doc), model)
    val initialWeights = new Array[Double](model.featuresCount * model.labelCount)
    val func = new CRFDiffFunc(corpus, model)
    val value = func.valueAt(initialWeights)

    shouldBeEqual(1.3862943611198906D, value)
  }

  @Test
  def should_evaluate_value_at_given_input {
    val model = new CRFModel
    val corpus = new CRFCorpus(Array(doc), model)
    val weights = Array[Double](
      0.05, -0.05, -0.05, 0.05, 0.05, -0.05, 0.05, -0.05, -0.05, 0.05,
      -0.05, 0.05, -0.05, 0.05, 0.05, -0.05, -0.05, 0.05, -0.05, 0.05,
      0.05, -0.05, -0.05, 0.05, 0.05, -0.05, -0.05, 0.05, 0.05, -0.05,
      0.0, 0.0, 0.05, -0.05, 0.0, 0.0, -0.05, 0.05, 0.05, -0.05, -0.05,
      0.05, 0.0, 0.0, -0.05, 0.05, 0.05, -0.05, 0.0, 0.0, 0.05, -0.05)
    assertEquals(52, weights.length)
    val func = new CRFDiffFunc(corpus, model)
    val value = func.valueAt(weights)

    shouldBeEqual(0.2862943611198907D, value)
  }

}
