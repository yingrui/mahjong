package websiteschema.mpsegment.neural

import org.junit.{Assert, Test}
import websiteschema.mpsegment.Assertion._
import websiteschema.mpsegment.math.Matrix

class BackPropagationLayerTest {

  @Test
  def should_record_input {
    val layer = new BackPropagationLayer(2, Matrix(2, 2, Array(1D, 1D, 1D, 1D)))
    val output = layer.computeOutput(Matrix(Array(2D, 2D)))

  }

  @Test
  def should_return_derivative() {
    shouldBeEqual(1D, Tangent.derivative(0D))
  }
}
