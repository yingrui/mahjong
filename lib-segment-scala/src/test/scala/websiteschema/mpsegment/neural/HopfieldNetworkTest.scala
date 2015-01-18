package websiteschema.mpsegment.neural

import org.junit.Assert
import org.junit.Test
import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.core.GraphBuilder
import websiteschema.mpsegment.dict.DictionaryFactory
import websiteschema.mpsegment.math.Matrix

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

  @Test
  def should_be_able_to_initiated_by_specified_weights {
    val network = HopfieldNetwork(Matrix(Array(Array[Double](0, 0, 0, -4),
                                               Array[Double](0, 0, -4, 0),
                                               Array[Double](0, -4, 0, 0),
                                               Array[Double](-4, 0, 0, 0))))

    val dataSet = Array(
      Array(false, false, true, true),
      Array(true, true, false, false),
      Array(false, true, false, true),
      Array(true, false, true, false)
    )

    dataSet.foreach(
      testData => {
        val output = network.present(testData)
        assertArrayEquals(testData, output)
      })
  }

  def assertArrayEquals(expect: Array[Boolean], actual: Array[Boolean]) {
    0 until expect.length foreach ((i: Int) => Assert.assertEquals(expect(i), actual(i)))
  }
}
