package websiteschema.mpsegment.neural

import org.junit.{Assert, Test}
import websiteschema.mpsegment.math.Matrix

class BackPropagationLayerTest {

  @Test
  def should_record_input_and_output {
    val layer = new BackPropagationLayer(Matrix(3, 2, Array(1D, 1D, 1D, 1D, 1D, 1D)))
    val input = Matrix(Array(2D, 2D))
    val output = layer.computeOutput(input)
    Assert.assertTrue(input == layer.input)
    Assert.assertTrue(output == layer.output)
  }

  @Test
  def should_accumulate_error_delta {
    val layer = new BackPropagationLayer(Matrix(4, 2, Array(1D, 1D, 1D, 1D, 1D, 1D, 1D, 1D)))
    layer.accumulateDelta.clear
    layer.input = Matrix(Array(2D, 2D, 2D))
    layer.calculateError(Matrix(Array(-6D, -6D)))
    val expect = Matrix(4, 2, Array(-12D, -12D, -12D, -12D, -12D, -12D, -6D, -6D))
    println(layer.accumulateDelta)
    Assert.assertTrue(expect == layer.accumulateDelta)
  }
}
