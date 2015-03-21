package websiteschema.mpsegment.neural

import org.junit.{Assert, Test}
import websiteschema.mpsegment.Assertion._
import websiteschema.mpsegment.math.Matrix

class NeuralNetworkTest {

  @Test
  def should_add_layer {
    val network = NeuralNetwork()

    Assert.assertEquals(0, network.layers.length)
    network.add(SigmoidLayer(Matrix(3, 3)))
    Assert.assertEquals(1, network.layers.length)
  }

  @Test
  def should_throw_exception_when_add_unmatched_layer {
    val network = NeuralNetwork()
    network.add(SigmoidLayer(Matrix(3, 3)))

    try {
      network.add(SigmoidLayer(Matrix(2, 2)))
      Assert.fail()
    }catch{
      case _: Throwable =>
    }
    Assert.assertEquals(1, network.layers.length)
  }

  @Test
  def should_return_output {
    val network = NeuralNetwork()

    network.add {
      val weight = Matrix(3, 2, Array(1D, 1D, 1D, 1D, 1D, 1D))
      new Layer(weight, Sigmoid())
    }

    val output = network.computeOutput(Matrix(1, 2, Array(1D, 1D)))

    shouldBeEqual(0.9525741268224334D, output.flatten)
  }

  /**
   *   1.0,1.0, 1
   *   0.0,0.0, 1
   *   1.0,0.0, 0
   *   0.0,1.0, 0
   *
   *   Linear Node 0
   *     Inputs    Weights
   *     Threshold    -1.1533724270371335
   *     Node 1    2.5394328382825613
   *     Node 2    2.667905523862483
   *     Node 3    -2.3616353555237
   *   Sigmoid Node 1
   *     Inputs    Weights
   *     Threshold    -1.2199904711652545
   *     Attrib x    0.6194204962409525
   *     Attrib y    2.410309843243667
   *   Sigmoid Node 2
   *     Inputs    Weights
   *     Threshold    -2.9040646026001595
   *     Attrib x    -3.1551184611975587
   *     Attrib y    -1.7339878537490587
   *   Sigmoid Node 3
   *     Inputs    Weights
   *     Threshold    -3.2956521855466483
   *     Attrib x    -2.974330562739181
   *     Attrib y    1.9148157930552836
   */
  @Test
  def should_classify_xor {
    val network = NeuralNetwork()
    network.add(new Layer(Matrix(3, 3, Array(0.61, -3.15, -2.97,
                                             2.41, -1.73, 1.91,
                                             -1.21, -2.90, -3.29)),
                Sigmoid()))
    network.add(new Layer(Matrix(4, 1, Array(2.53, 2.66, -2.36, -1.15)), Sigmoid()))

    println(network.computeOutput(Matrix(Array(1D, 0D))))
    println(network.computeOutput(Matrix(Array(0D, 1D))))
    println(network.computeOutput(Matrix(Array(1D, 1D))))
    println(network.computeOutput(Matrix(Array(0D, 0D))))
  }


}

