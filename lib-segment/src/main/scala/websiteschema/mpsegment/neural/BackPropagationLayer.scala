package websiteschema.mpsegment.neural

import websiteschema.mpsegment.math.Matrix

trait BackPropagationLayer extends Layer {

  def update(rate: Double, momentum: Double)

  def propagateError(delta: Matrix): Matrix

  def calculateDelta(actual: Matrix, error: Matrix): Matrix

  def layer: Layer
}

trait BPLayer extends BackPropagationLayer {
  val neuronCount = weight.col
  val inputNeuronCount = weight.row - 1
  val biasRow = inputNeuronCount

  var input = Matrix(1, inputNeuronCount)
  var output = Matrix(1, neuronCount)
  var errorDelta = Matrix(1, inputNeuronCount)
  var error = Matrix(1, inputNeuronCount)
  var accumulateDelta = Matrix(inputNeuronCount + 1, neuronCount)
  var delta = Matrix(inputNeuronCount + 1, neuronCount)

  def computeOutput(in: Matrix) = {
    input = in
    output = layer.computeOutput(input)
    output
  }

  def propagateError(delta: Matrix): Matrix = {
    for (j <- 0 until neuronCount) {
      for (i <- 0 until inputNeuronCount) {
        accumulateDelta(i, j) = accumulateDelta(i, j) + delta(0, j) * input(0, i)
        error(0, i) = error(0, i) + weight(i, j) * delta(0, j)
      }
      accumulateDelta(biasRow, j) = accumulateDelta(biasRow, j) + delta(0, j)
    }

    errorDelta := calculateDelta(input, error)

    errorDelta
  }
}

class BPSigmoidLayer(var weight: Matrix) extends BPLayer {

  def layer = SigmoidLayer(weight)

  def size = layer.size

  private def calculateDelta(err: Double, output: Double): Double = {
    err * Sigmoid().getDerivative(output)
  }

  def calculateDelta(actual: Matrix, error: Matrix): Matrix = {
    val delta = for (i <- 0 until error.col) yield {
      calculateDelta(error(0, i), actual(0, i))
    }
    Matrix(delta)
  }

  def update(rate: Double, momentum: Double) {
    delta = (accumulateDelta x rate) + (delta x momentum)
    weight = weight + delta
    accumulateDelta.clear
    error.clear
  }
}

object BackPropagationLayer {
  def apply(size: Int) = new BPSigmoidLayer(Matrix.ramdomize(size + 1, size, -1D, 1D))

  def apply(size: Int, nextLayerSize: Int) = new BPSigmoidLayer(Matrix.ramdomize(size + 1, nextLayerSize, -1D, 1D))
}
