package me.yingrui.segment.neural

import me.yingrui.segment.math.Matrix

class BPRecurrentSigmoidLayer(val weight: Matrix, val bias: Matrix) extends BPLayer {

  def layer = RecurrentLayer.sigmoid(weight, bias, this.output)

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
