package websiteschema.mpsegment.neural

import org.junit.{Assert, Test}
import websiteschema.mpsegment.math.Matrix
import websiteschema.mpsegment.Assertion

class NeuralNetworkTest {

  @Test
  def should_add_layer {
    val network = NeuralNetwork()

    Assert.assertEquals(0, network.layers.length)
    network.add(Layer(Matrix(3, 3)))
    Assert.assertEquals(1, network.layers.length)
  }

  @Test
  def should_throw_exception_when_add_unmatched_layer {
    val network = NeuralNetwork()
    network.add(Layer(Matrix(3, 3)))

    try {
      network.add(Layer(Matrix(2, 2)))
      Assert.fail()
    }catch{
      case _: Throwable =>
    }
    Assert.assertEquals(1, network.layers.length)
  }

  @Test
  def should_return_output {
    val network = NeuralNetwork()
    network.add(layer)

    val output = network.computeOutput(Matrix(1, 2, Array(1D, 1D)))

    Assertion.shouldBeEqual(3D, output.flatten)
  }

  def layer = {
    val weight = Matrix(2, 2, Array(1D, 1D, 1D, 1D))
    new Layer(weight, x => x + 1D)
  }
}

