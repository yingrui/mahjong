package me.yingrui.segment.neural

import java.lang.Math.pow

import me.yingrui.segment.math.Matrix

class ErrorCalculator {

  private var setSize = 0D
  private var globalError = 0D

  def updateError(actual: Matrix, ideal: Matrix) {
    globalError += (ideal - actual).map(pow(_, 2D)).sum
    setSize += actual.col
  }

  private def getRootMeanSquare = Math.sqrt(globalError / setSize)

  def loss = getRootMeanSquare
}
