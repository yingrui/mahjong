package me.yingrui.segment.neural

import me.yingrui.segment.math.Matrix

trait Layer {
  def weight: Matrix
  def size: Int
  def computeOutput(input: Matrix): Matrix
}

class SingleLayer(val weight: Matrix, val activation: Activation, withBias: Boolean = true) extends Layer {

  def size = weight.col

  def computeOutput(input: Matrix) = {
    activation activate compute(input) // for example: sigmoid(W * h + b)
  }

  private def compute(input: Matrix): Matrix = {
    if (withBias) {
      assert(input.isVector && input.col == (weight.row - 1)) // b = weight(weight.row - 1)
      Matrix(input.flatten ++ Array(1D)) x weight // W * h + b
    } else {
      assert(input.isVector && input.col == weight.row) // b = weight(weight.row)
      input x weight // W * h
    }
  }
}

object SigmoidLayer {
  def apply(weight: Matrix): Layer = new SingleLayer(weight, Sigmoid())
}

object SoftmaxLayer {

  class BPSoftmaxLayer(var weight: Matrix) extends BPLayer {

    def layer = new SingleLayer(weight, Softmax(), false)

    def size = layer.size

    def calculateDelta(actual: Matrix, error: Matrix): Matrix = {
      throw new RuntimeException("Not implemented")
    }

    def update(rate: Double, momentum: Double) {
      delta = (accumulateDelta x rate) + (delta x momentum)
      weight = weight + delta
      accumulateDelta.clear
      error.clear
    }
  }

  def apply(weight: Matrix): BPLayer = new BPSoftmaxLayer(weight)
}
