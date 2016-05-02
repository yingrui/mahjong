package me.yingrui.segment.neural

import me.yingrui.segment.math.Matrix

class RecurrentLayer(val weight: Matrix, val bias: Matrix, val output: Matrix) extends Layer {

  def size = weight.col

  def computeOutput(input: Matrix) = {
    Sigmoid() activate compute(input) // for example: sigmoid(W * h + b)
  }

  private def compute(input: Matrix): Matrix = {
    assert(input.isVector && input.col == weight.row)
    (input x weight + output) / 2.0 // W * h + S(t-1)
  }
}

object RecurrentLayer {

  def apply(weight: Matrix, output: Matrix) = new RecurrentLayer(weight, Matrix(1, weight.col), output)
}