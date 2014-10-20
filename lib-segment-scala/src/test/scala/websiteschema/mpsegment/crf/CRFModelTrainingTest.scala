package websiteschema.mpsegment.crf

import org.junit.Test
import org.junit.Assert._
import websiteschema.mpsegment.Assertion._

trait WithTestData {
  // data = int[X][Window][Features]
  val data = Array(
    Array(Array(7, 10, 0, 12, 14, 15, 16, 17, 2, 19, 3, 21, 23, 24, 25)),
    Array(Array(8, 9, 11, 13, 15, 1, 17, 18, 20, 21, 4, 22, 5, 24, 6))
  )

  val label = Array[Int](0, 1)
  val doc = new CRFDocument(data, label)
}

class CRFModelTrainingTest extends WithTestData {

  @Test
  def should_train_a_model {
    val model = new CRFModel
    CRFModel.build(new CRFCorpus(Array(doc), model))

    shouldBeEqual(0.0D, model.weights(0))
  }

}

class CRFDiffFuncTest extends WithTestData {

  @Test
  def should_evaluate_value_at_given_input {
    val model = new CRFModel
    val corpus = new CRFCorpus(Array(doc), model)
    val initialWeights = new Array[Double](model.featuresCount * model.classesCount)
    val func = new CRFDiffFunc(corpus, model)
    val value = func.valueAt(initialWeights)

    shouldBeEqual(1.3862943611198906D, value)
  }

}

class CRFCliqueTreeTest extends WithTestData {

  @Test
  def should_iterate_clique_and_calculate_probability {
    val model = new CRFModel
    val initialWeights = CRFUtils.empty2DArray(model.featuresCount, model.classesCount)

    val clique = CRFCliqueTree(doc, model, initialWeights)

    shouldBeEqual(-1.3862943611198906D, clique.condLogProb)
  }

}

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