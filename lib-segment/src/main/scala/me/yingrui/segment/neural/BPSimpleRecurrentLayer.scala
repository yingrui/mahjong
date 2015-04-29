package me.yingrui.segment.neural

import me.yingrui.segment.math.Matrix

class BPSimpleRecurrentLayer(val weight: Matrix, val bias: Matrix) extends BPLayer {

  def layer = SimpleRecurrentLayer(weight, this.output)

  def size = layer.size

  def calculateDelta(actual: Matrix, error: Matrix): Matrix = error

}
