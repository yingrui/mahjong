package websiteschema.mpsegment.neural

import websiteschema.mpsegment.math.Matrix

trait Layer {
  def weight: Matrix
  def size: Int
  def computeOutput(input: Matrix): Matrix
}

class SingleLayer(val weight: Matrix, val activation: Activation) extends Layer {

  def size = weight.col

  def computeOutput(input: Matrix) = {
    assert(input.isVector && input.col == (weight.row - 1)) // b = weight(weight.row - 1)
    val output = Matrix(input.flatten ++ Array(1D)) x weight // W * h + b
    activation.activate(output) // by default: sigmoid(W * h + b)
  }
}

object SigmoidLayer {
  def apply(weight: Matrix): Layer = new SingleLayer(weight, Sigmoid())
}
