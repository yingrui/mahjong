package me.yingrui.segment.neural

import me.yingrui.segment.math.Matrix

trait Layer {
  def weight: Matrix
  def bias: Matrix
  def size: Int
  def computeOutput(input: Matrix): Matrix
}

class SingleLayer(val weight: Matrix, val activation: Activation, val bias: Matrix, withBias: Boolean = true) extends Layer {
  assert(weight.col == bias.col)
  def size = weight.col

  def computeOutput(input: Matrix) = {
    activation activate compute(input) // for example: sigmoid(W * h + b)
  }

  private def compute(input: Matrix): Matrix = {
    assert(input.isVector && input.col == weight.row)
    if (withBias) {
      (input x weight) + bias // W * h + b
    } else {
      input x weight // W * h
    }
  }
}

object SigmoidLayer {
  def apply(weight: Matrix, bias: Matrix): Layer = new SingleLayer(weight, Sigmoid(), bias)
}