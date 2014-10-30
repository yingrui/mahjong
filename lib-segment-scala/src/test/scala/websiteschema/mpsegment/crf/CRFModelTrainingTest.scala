package websiteschema.mpsegment.crf

import org.junit.Test
import websiteschema.mpsegment.Assertion._

trait WithTestData {
  // data = int[X][Features]
  val data = Array(
    Array(7, 10, 0, 12, 14, 15, 16, 17, 2, 19, 3, 21, 23, 24, 25),
    Array(8, 9, 11, 13, 15, 1, 17, 18, 20, 21, 4, 22, 5, 24, 6)
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





