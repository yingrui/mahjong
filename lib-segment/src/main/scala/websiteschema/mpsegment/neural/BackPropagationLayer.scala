package websiteschema.mpsegment.neural

import websiteschema.mpsegment.math.Matrix

class BackPropagationLayer(var weight: Matrix) {

  val neuronCount = weight.col
  val inputNeuronCount = weight.row - 1
  val biasRow = inputNeuronCount

  var input = Matrix(1, inputNeuronCount)
  var output = Matrix(1, neuronCount)
  var errorDelta = Matrix(1, inputNeuronCount)
  var error = Matrix(1, inputNeuronCount)
  var accumulateDelta = Matrix(inputNeuronCount + 1, neuronCount)
  var delta = Matrix(inputNeuronCount + 1, neuronCount)

  def getLayer = Layer(weight)

  def computeOutput(in: Matrix) = {
    input = in
    output = getLayer.computeOutput(input)
    output
  }

  def calculateError(delta: Matrix): Matrix = {
    for (j <- 0 until neuronCount) {
      for (i <- 0 until inputNeuronCount) {
        accumulateDelta(i, j) = accumulateDelta(i, j) + delta(0, j) * input(0, i)
        error(0, i) = error(0, i) + weight(i, j) * delta(0, j)
      }
      accumulateDelta(biasRow, j) = accumulateDelta(biasRow, j) + delta(0, j)
    }

    (0 until inputNeuronCount).foreach{i => errorDelta(0, i) = calculateDelta(error(0, i), input(0, i))}

    errorDelta
  }

  def calculateDelta(err: Double, output: Double): Double = {
    err * Sigmoid.derivative(output)
  }

  def learn(rate: Double, momentum: Double) {
    delta = (accumulateDelta x rate) + (delta x momentum)
    weight = weight + delta
    accumulateDelta.clear
    error.clear
  }
}

object BackPropagationLayer {
  def apply(size: Int) = new BackPropagationLayer(Matrix.ramdomize(size + 1, size, -1D, 1D))

  def apply(size: Int, nextLayerSize: Int) = new BackPropagationLayer(Matrix.ramdomize(size + 1, nextLayerSize, -1D, 1D))
}
