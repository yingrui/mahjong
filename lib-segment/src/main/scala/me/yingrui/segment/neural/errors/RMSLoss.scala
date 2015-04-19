package me.yingrui.segment.neural.errors

import java.lang.Math._
import me.yingrui.segment.math.Matrix

class RMSLoss extends Loss {

  private var setSize = 0D
  private var globalError = 0D

  def clear: Unit = {
    setSize = 0D
    globalError = 0D
  }

  def updateError(actual: Matrix, ideal: Matrix) {
    globalError += (ideal - actual).map(pow(_, 2D)).sum
    setSize += actual.col
  }

  private def getRootMeanSquare = Math.sqrt(globalError / setSize)

  def loss = getRootMeanSquare
}

