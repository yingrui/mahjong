package websiteschema.mpsegment.crf

import org.junit.Test
import websiteschema.mpsegment.Assertion._

class CRFCliqueTreeTest extends WithTestData {

  @Test
  def should_iterate_clique_and_calculate_probability {
    val model = new CRFModel
    val initialWeights = CRFUtils.empty2DArray(model.featuresCount, model.labelCount)

    val clique = CRFCliqueTree(doc, model, initialWeights)

    shouldBeEqual(-1.3862943611198906D, clique.condLogProb)
  }

}
