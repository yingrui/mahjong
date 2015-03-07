package websiteschema.mpsegment.neural

import org.junit.{Assert, Test}
import websiteschema.mpsegment.math.Matrix
import websiteschema.mpsegment.Assertion

class LayerTest {

  @Test
  def should_return_number_of_neural {
    val layer = Layer(Matrix(3, 2))
    Assert.assertEquals(2, layer.size)
  }

  @Test
  def should_return_output {
    val weight = Matrix(3, 2, Array(1D, 1D, 1D, 1D, 1D, 1D))
    val layer = new Layer(weight, x => x + 1D)

    val output = layer.computeOutput(Matrix(Array(1D, 1D)))
    println(output)
    Assertion.shouldBeEqual(4D, output.flatten)
  }


}

