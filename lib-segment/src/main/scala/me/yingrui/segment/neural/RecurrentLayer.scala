package me.yingrui.segment.neural

import me.yingrui.segment.math.Matrix

class RecurrentLayer(val weight: Matrix, val activation: Activation, val bias: Matrix, val output: Matrix, withBias: Boolean = true) extends Layer {
  assert(weight.col == bias.col)
  assert(output.col == bias.col && output.isVector)
  def size = weight.col

  def computeOutput(input: Matrix) = {
    activation activate compute(input) // for example: sigmoid(W * h + b)
  }

  private def compute(input: Matrix): Matrix = {
    assert(input.isVector && input.col == weight.row)
    if (withBias) {
      (input x weight) + bias + output // W * h + b
    } else {
      input x weight + output// W * h
    }
  }
}

object RecurrentLayer {

  def sigmoid(weight: Matrix, bias: Matrix, output: Matrix): RecurrentLayer = new RecurrentLayer(weight, Sigmoid(), bias, output, true)
}