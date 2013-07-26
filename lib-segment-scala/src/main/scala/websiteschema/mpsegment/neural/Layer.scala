package websiteschema.mpsegment.neural

import websiteschema.mpsegment.math.Matrix

object Layer {
  def apply(weight: Matrix) = new Layer(weight, Sigmoid.activation)
}

class Layer(val weight: Matrix, val activation: (Double) => Double) {

  def size = weight.col

  def computeOutput(input: Matrix) = {
    assert(input.isVector && input.col == weight.row)
    Matrix.map(input x weight, activation)
  }
}