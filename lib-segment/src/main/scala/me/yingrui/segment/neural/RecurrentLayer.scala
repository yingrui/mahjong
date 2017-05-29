package me.yingrui.segment.neural

import me.yingrui.segment.math.Matrix

class RecurrentLayer(val weight: Matrix, val wh: Matrix, val bias: Matrix) extends Layer {

  def size = weight.col

  override def computeOutput(input: Matrix) = {
    throw new NoSuchMethodException()
  }

  def computeOutput(input: Matrix, hs: Matrix) = {
    Sigmoid() activate compute(input, hs) // for example: sigmoid(W * h + b)
  }

  private def compute(input: Matrix, hs: Matrix): Matrix = {
    assert(input.isVector && input.col == weight.row)
    input x weight + hs x wh + bias
  }
}

object RecurrentLayer {

  def apply(weight: Matrix, wh: Matrix, bias: Matrix) = new RecurrentLayer(weight, wh, bias)
}