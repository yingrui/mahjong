package me.yingrui.segment.neural

import me.yingrui.segment.math.Matrix

class BPRecurrentLayer(val weight: Matrix, val bias: Matrix, val immutable: Boolean) extends BPLayer {

  def layer = RecurrentLayer(weight, this.output)

  def size = layer.size

  def calculateDelta(actual: Matrix, error: Matrix): Matrix = {
    val delta = for (i <- 0 until error.col) yield {
      val err = error(0, i)
      val output = actual(0, i)
      err * Sigmoid().getDerivative(output)
    }
    Matrix(delta)
  }

}
