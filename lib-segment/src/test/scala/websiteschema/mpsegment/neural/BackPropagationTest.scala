package websiteschema.mpsegment.neural

import org.junit.{Assert, Test}
import websiteschema.mpsegment.math.Matrix
import websiteschema.mpsegment.Assertion

class BackPropagationTest {

  @Test
  def should_train_a_xor_classifier {
    BackPropagation.debug = true;
    val trainer = createBackPropagationTrainer
    trainer.addLayer(3)

    trainer takeARound 10000

    println("network:")
    println(trainer.getNetwork)

    val network = trainer.getNetwork
    println("output: " + network.computeOutput(Matrix.vector(1D, 1D)))
    println("output: " + network.computeOutput(Matrix.vector(0D, 0D)))
    println("output: " + network.computeOutput(Matrix.vector(1D, 0D)))
    println("output: " + network.computeOutput(Matrix.vector(0D, 1D)))

    Assert.assertTrue((1D - computeOutput(network, Matrix.vector(1D, 1D))) < 0.1)
    Assert.assertTrue((1D - computeOutput(network, Matrix.vector(0D, 0D))) < 0.1)
    Assert.assertTrue((computeOutput(network, Matrix.vector(1D, 0D)) - 0D) < 0.1)
    Assert.assertTrue((computeOutput(network, Matrix.vector(0D, 1D)) - 0D) < 0.1)
    println("Precision rate: " + trainer.testWithTrainSet)
    Assertion.shouldBeEqual(1.0, trainer.testWithTrainSet)
  }

  private def computeOutput(network: NeuralNetwork, input: Matrix): Double = network.computeOutput(input)(0, 0)

  def createBackPropagationTrainer: BackPropagation = {
    val trainer = BackPropagation(2, 1)
    trainer.addSample(Matrix(Array(1D, 1D)), Matrix(Array(1D)))
    trainer.addSample(Matrix(Array(1D, 0D)), Matrix(Array(0D)))
    trainer.addSample(Matrix(Array(0D, 0D)), Matrix(Array(1D)))
    trainer.addSample(Matrix(Array(0D, 1D)), Matrix(Array(0D)))

    trainer
  }
}
