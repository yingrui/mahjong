package websiteschema.mpsegment.neural

import websiteschema.mpsegment.math.Matrix

class ErrorCalculator {

  private var setSize = 0D
  private var globalError = 0D

  def updateError(actual: Array[Double], ideal: Array[Double]) {
    globalError += Matrix.arithmetic(actual, ideal, (a, i) => Math.pow(i - a, 2D)).sum
    setSize += actual.size
  }

  private def getRootMeanSquare = Math.sqrt(globalError / setSize)

  def loss = getRootMeanSquare
}
