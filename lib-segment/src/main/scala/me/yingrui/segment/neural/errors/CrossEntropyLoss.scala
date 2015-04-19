package me.yingrui.segment.neural.errors

import java.lang.Math._
import me.yingrui.segment.math.Matrix

class CrossEntropyLoss extends Loss {
  private var cost = 0D
  private var setSize = 0D

  def clear: Unit = {
    setSize = 0D
    cost = 0D
  }

  def updateError(actual: Matrix, ideal: Matrix) {
    cost -= (ideal % actual.map(ele => log(ele))).sum
    setSize += 1D
  }

  def loss = cost /setSize
}
