package websiteschema.mpsegment.neural

import websiteschema.mpsegment.math.Matrix
import collection.mutable.ListBuffer

trait Train {

  def getError: Double

  def getNetwork: NeuralNetwork

  def takeARound: Unit

}

class BackPropagation(inputSize: Int, outputSize: Int) extends Train {

  private var error = 0D;
  private var network = new NeuralNetwork()
  private val inputArray = ListBuffer[Matrix]()
  private val idealArray = ListBuffer[Matrix]()
  private var hiddenLayerSize = 0
  private var layers = List[BackPropagationLayer]()

  def addSample(input: Matrix, ideal: Matrix) {
    assert(input.isVector && input.col == inputSize)
    assert(ideal.isVector && ideal.col == outputSize)
    inputArray += input
    idealArray += ideal
  }

  def setHiddenLayerSize(size: Int) {
    hiddenLayerSize = size
  }

  def getError = error

  def getNetwork = network

  def takeARound: Unit = {
    initLayers

    network = new NeuralNetwork
    layers.foreach(bp => network.add(bp))
    for (i <- 0 until inputArray.length) {
      val output = network.computeOutput(inputArray(i))
      computeError(output, idealArray(i))
    }

  }

  def computeError(actual: Matrix, ideal: Matrix) {

  }

  private def initLayers {
    if (layers.length == 0) {
      val hiddenLayer = for (i <- 0 until hiddenLayerSize) yield {BackPropagationLayer(inputSize)}
      layers = List(BackPropagationLayer(inputSize)) ++ hiddenLayer.toList ++ List(BackPropagationLayer(outputSize))
    }
  }
}

class BackPropagationLayer(size: Int, var weight: Matrix) {

  var input = Matrix(1, size)
  var output = Matrix(1, size)
  var delta = Matrix(1, size)
  var errorDelta = Matrix(1, size)
  var error = Matrix(1, size)
  var accMatrixDelta = Matrix(weight.row, weight.col)

  def getLayer = Layer(weight)

  def computeOutput(in: Matrix) = {
    input = in
    output = getLayer.computeOutput(input)
    output
  }


  def learn(ideal: Matrix): Matrix = {
    error = ideal - output
    errorDelta = Matrix(for(i <- 0 until error.col) yield {calculateDelta(error(0, i), output(0, i))})

    for (j <- 0 until size) {
      for (i <- 0 until size) {
        accMatrixDelta(i, j) = accMatrixDelta(i, j) + errorDelta(0, j) * input(0, i)

      }
    }

    throw new UnsupportedOperationException
  }

  def calculateDelta(err: Double, output: Double): Double = {
    err * Sigmoid.derivative(output)
  }
}

object BackPropagationLayer {
  def apply(size: Int) = new BackPropagationLayer(size, Matrix.ramdomize(size, size, -1D, 1D))
}