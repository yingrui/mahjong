package me.yingrui.segment.neural

import me.yingrui.segment.math.Matrix

class SimpleRecurrentLayer(val weight: Matrix, val bias: Matrix, val output: Matrix) extends Layer {

  def size = weight.col

  def computeOutput(input: Matrix) = {
    compute(input) // for example: sigmoid(W * h + b)
  }

  private def compute(input: Matrix): Matrix = {
    assert(input.isVector && input.col == weight.row)
    val sum = input.sum
    (input x weight + output) / sum // W * h + S(t-1)
  }
}

object SimpleRecurrentLayer {

  def apply(weight: Matrix, output: Matrix) = new SimpleRecurrentLayer(weight, Matrix(1, weight.col), output)
}