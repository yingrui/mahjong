package me.yingrui.segment.neural.errors

import me.yingrui.segment.math.Matrix

trait Loss {
  def updateError(actual: Matrix, ideal: Matrix): Unit
  def loss: Double
  def clear: Unit
}
