package websiteschema.mpsegment.neural

import websiteschema.mpsegment.math.Matrix
import collection.mutable.ListBuffer

object BackPropagation {
  def apply(inputSize: Int, outputSize: Int, rate: Double, momentum: Double): BackPropagation = new BackPropagation(inputSize, outputSize, rate, momentum)

  def apply(inputSize: Int, outputSize: Int): BackPropagation = {
    new BackPropagation(inputSize, outputSize, 1.0D, 0.0D)
  }

  var debug: Boolean = false
}

class BackPropagation(val inputSize: Int, val outputSize: Int, val rate: Double, val momentum: Double) extends Train {

  private var error = 0D
  private var network = new NeuralNetwork()
  private val inputArray = ListBuffer[Matrix]()
  private val idealArray = ListBuffer[Matrix]()
  private var layers = List[BackPropagationLayer]()
  private var neuronSizePerLayer = List[Int](inputSize)

  def addSample(input: Matrix, ideal: Matrix) {
    assert(input.isVector && input.col == inputSize)
    assert(ideal.isVector && ideal.col == outputSize)
    inputArray += input
    idealArray += ideal
  }

  def addLayer(neuronSize: Int) {
    neuronSizePerLayer = neuronSizePerLayer ++ List(neuronSize)
  }

  def getError = error

  def getNetwork = network

  def takeARound(iteration: Int): Unit = {
    initLayers

    for (it <- 1 to iteration) {
      for (i <- 0 until inputArray.length) {
        val output = network.computeOutput(inputArray(i))
        computeError(output, idealArray(i))
        layers.foreach(_.learn(rate, momentum))
      }
      error = recalculateError
      if (BackPropagation.debug && it % 100 == 0) {
        println("Cycles Left: " + (iteration - it) + ", Error: " + error);
      }
    }
  }

  def recalculateError: Double = {
    val network = new NeuralNetwork
    layers.foreach(network.add)

    val errorCalculator = new ErrorCalculator
    for(i <- 0 until idealArray.length) {
      val actual = network.computeOutput(inputArray(i))
      errorCalculator.updateError(actual.flatten, idealArray(i).flatten)
    }
    errorCalculator.getRootMeanSquare
  }

  def computeError(actual: Matrix, ideal: Matrix) {
    val error = ideal - actual
    val errorDelta = Matrix(for(i <- 0 until error.col) yield {layers.last.calculateDelta(error(0, i), actual(0, i))})

    layers.foldRight(errorDelta)((layer, delta) => layer.calculateError(delta))
  }

  private def initLayers {
    if (layers.length == 0) {
      neuronSizePerLayer = neuronSizePerLayer ++ List(outputSize)
      for(i <- 1 until neuronSizePerLayer.length) {
        layers = layers ++ List(BackPropagationLayer(neuronSizePerLayer(i - 1), neuronSizePerLayer(i)))
      }

      network = new NeuralNetwork
      layers.foreach(bp => network.add(bp))
    }
  }
}
