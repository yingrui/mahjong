package websiteschema.mpsegment.neural

import org.junit.Assert
import org.junit.Test
import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.core.GraphBuilder
import websiteschema.mpsegment.dict.DictionaryFactory

class HopfieldNetworkTest {

  @Test
  def should_return_as_same_as_input_pattern() {
    val network = new HopfieldNetwork(4)
    val trainPatterns = Array(
      Array(false, false, true, true),
      Array(true, true, false, false),
      Array(false, true, false, true),
      Array(true, false, true, false)
    )

    trainPatterns.foreach(network.train(_))

    trainPatterns.foreach(
      trainPattern => {
        val output = network.present(trainPattern)
        assertArrayEquals(trainPattern, output)
      })
    println(network)
  }

  def assertArrayEquals(expect: Array[Boolean], actual: Array[Boolean]) {
    0 until expect.length foreach ((i: Int) => Assert.assertEquals(expect(i), actual(i)))
  }
}
