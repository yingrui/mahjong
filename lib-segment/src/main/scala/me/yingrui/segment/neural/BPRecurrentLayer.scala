package me.yingrui.segment.neural

import java.lang.Math._

import me.yingrui.segment.math.Matrix

class BPRecurrentLayer(val weight: Matrix, val wh: Matrix, val bias: Matrix, val immutable: Boolean) extends BackPropagationLayer {

  val neuronCount = weight.col
  val inputNeuronCount = weight.row
  val biasRow = inputNeuronCount

  val input = Matrix(1, inputNeuronCount)
  val hiddenLayerInput = Matrix(1, neuronCount)
  val output = Matrix(1, neuronCount)
  val error = Matrix(1, inputNeuronCount)
  val accumulateDelta = Matrix(inputNeuronCount, neuronCount)
  val accumulateBiasDelta = Matrix(1, neuronCount)
  val accumulateWeightHiddenLayerDelta = Matrix(neuronCount, neuronCount)

  val delta = Matrix(inputNeuronCount, neuronCount)
  val biasDelta = Matrix(1, neuronCount)
  val weightHiddenLayerDelta = Matrix(neuronCount, neuronCount)

  def layer = RecurrentLayer(weight, wh, bias)

  def size = layer.size

  def calculateDelta(actual: Matrix, error: Matrix): Matrix = {
    val delta = for (i <- 0 until error.col) yield {
      val err = error(0, i)
      val output = actual(0, i)
      err * Sigmoid().getDerivative(output)
    }
    Matrix(delta)
  }

  def computeOutput(in: Matrix) = {
    this.computeOutput(in, Matrix(1, neuronCount))
  }

  def computeOutput(in: Matrix, hs: Matrix) = {
    input := in
    hiddenLayerInput := hs
    output := layer.computeOutput(input, hiddenLayerInput)
    output
  }

  override def propagateError(err: Matrix): Matrix = {
    val delta = calculateDelta(output, err)

    accumulateDelta += input.T x delta
    accumulateWeightHiddenLayerDelta += hiddenLayerInput.T x delta
    accumulateBiasDelta += delta.row(0)
    error := delta x weight.T

    error
  }

  override def update(rate: Double, momentum: Double) {
    assert(rate > 1e-20, "rate must greater than 0.0")
    if(abs(momentum) > 1e-20) {
      delta := (accumulateDelta x rate) + (delta x momentum)
      biasDelta := (accumulateBiasDelta x rate) + (biasDelta x momentum)
      weightHiddenLayerDelta := (accumulateWeightHiddenLayerDelta x rate) + (weightHiddenLayerDelta x momentum)
    } else {
      delta := (accumulateDelta x rate)
      biasDelta := (accumulateBiasDelta x rate)
      weightHiddenLayerDelta := (accumulateWeightHiddenLayerDelta x rate)
    }

    weight += delta
    bias += biasDelta
    wh += weightHiddenLayerDelta

    cleanupAfterUpdate
  }

  private def cleanupAfterUpdate: Unit = {
    accumulateDelta.clear
    accumulateWeightHiddenLayerDelta.clear
    accumulateBiasDelta.clear
    error.clear
  }
}
