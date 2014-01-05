package websiteschema.mpsegment.hmm

import org.junit.Test
import websiteschema.mpsegment.Assertion

class EmissionTest {

  @Test
  def should_get_states_by_observe {
    val count = 10
    val data = Map(1 -> Map(1 -> count))
    val emission = Emission(data)
    Assertion.shouldBeEqual(count.toDouble / (count.toDouble + 1.0), emission.getProb(1, 1))
  }
}
