package me.yingrui.segment.neural.errors

import me.yingrui.segment.math.Matrix

class CrossEntropyLoss extends Loss {
  private var cost = 0D
  private var setSize = 0D

  def clear: Unit = {
    setSize = 0D
    cost = 0D
  }

  private def log(x: Double): Double = {
    if (x < 1E-100) {
      -230D
    } else {
      Math.log(x)
    }
  }

  def updateError(actual: Matrix, ideal: Matrix) {
    val matrix = ideal % actual.map(log(_))
    val sum = matrix.sum
    cost -= sum
    setSize += 1D
  }

  def loss = {
    val l = cost / (setSize + 1D)
    l
  }
}
