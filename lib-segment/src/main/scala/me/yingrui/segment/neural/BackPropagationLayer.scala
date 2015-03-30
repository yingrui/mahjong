package me.yingrui.segment.neural

import java.lang.Math.abs

import me.yingrui.segment.math.Matrix

trait BackPropagationLayer extends Layer {

  def update(rate: Double, momentum: Double)

  def propagateError(delta: Matrix): Matrix

  def calculateDelta(actual: Matrix, error: Matrix): Matrix

  def layer: Layer
}

trait BPLayer extends BackPropagationLayer {
  val neuronCount = weight.col
  val inputNeuronCount = weight.row
  val biasRow = inputNeuronCount

  val input = Matrix(1, inputNeuronCount)
  val output = Matrix(1, neuronCount)
  val errorDelta = Matrix(1, inputNeuronCount)
  val error = Matrix(1, inputNeuronCount)
  val accumulateDelta = Matrix(inputNeuronCount, neuronCount)
  val accumulateBiasDelta = Matrix(1, neuronCount)
  val delta = Matrix(inputNeuronCount, neuronCount)
  val biasDelta = Matrix(1, neuronCount)

  def computeOutput(in: Matrix) = {
    input := in
    output := layer.computeOutput(input)
    output
  }

  def propagateError(delta: Matrix): Matrix = {

//    accumulateDelta += input.T x delta
//    error += delta x weight.T

    for (j <- 0 until neuronCount) {
      for (i <- 0 until inputNeuronCount) {
        accumulateDelta(i, j) = accumulateDelta(i, j) + delta(0, j) * input(0, i)
        error(0, i) = error(0, i) + weight(i, j) * delta(0, j)
      }
      accumulateBiasDelta(0, j) = accumulateBiasDelta(0, j) + delta(0, j)
    }

    errorDelta := calculateDelta(input, error)

    errorDelta
  }

  def update(rate: Double, momentum: Double) {
    assert(rate > 1e-20, "rate must greater than 0.0")
    if(abs(momentum) > 1e-20) {
      delta := (accumulateDelta x rate) + (delta x momentum)
      biasDelta := (accumulateBiasDelta x rate) + (biasDelta x momentum)
    } else {
      delta := (accumulateDelta x rate)
      biasDelta := (accumulateBiasDelta x rate)
    }

    weight += delta
    bias += biasDelta

    clearAfterUpdate
  }

  private def clearAfterUpdate: Unit = {
    accumulateDelta.clear
    accumulateBiasDelta.clear
    error.clear
  }
}

class BPSigmoidLayer(val weight: Matrix, val bias: Matrix) extends BPLayer {

  def layer = SigmoidLayer(weight, bias)

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

}

object BackPropagationLayer {
  def apply(size: Int) = new BPSigmoidLayer(Matrix.randomize(size, size, -1D, 1D), Matrix.randomize(1, size, -1D, 1D))

  def apply(size: Int, nextLayerSize: Int) = new BPSigmoidLayer(Matrix.randomize(size, nextLayerSize, -1D, 1D), Matrix.randomize(1, nextLayerSize, -1D, 1D))
}
