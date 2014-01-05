package websiteschema.mpsegment.hmm

import org.junit.Test
import org.junit.Assert._
import websiteschema.mpsegment.Assertion._
import java.util

class EmissionTest {

  @Test
  def should_get_emission_prob_by_state_and_observe {
    val emission = new Emission
    emission.setProb(1, 2, 0.9)
    shouldBeEqual(0.9, emission.getProb(1, 2))
  }

  @Test
  def should_build_emission_prob {
    val count = 10
    val data = Map(1 -> Map(1 -> count))
    val emission = Emission(data)
    shouldBeEqual(count.toDouble / (count.toDouble + 1.0), emission.getProb(1, 1))
  }

  @Test
  def should_get_states_by_observe {
    val data = Map(1 -> Map(1 -> 10))
    val states = Emission(data).getStateProbByObserve(1)
    assertEquals(1, states.size())
    assert(states.contains(1), "States of observe 1 must contains state 1")
  }

  @Test
  def should_get_default_states_when_observe_is_unknown {
    val data = Map(1 -> Map(1 -> 10))
    val list = new util.ArrayList[Int]()
    list.add(1)
    val emission = Emission(data, () => list)
    val states = emission.getStateProbByObserve(2)
    assertEquals(1, states.size())
    assert(states.contains(1), "No matter what observe is, the states must contains 1")
  }
}
