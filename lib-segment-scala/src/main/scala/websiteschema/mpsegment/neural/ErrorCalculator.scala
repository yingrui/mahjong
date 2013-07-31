package websiteschema.mpsegment.neural

import websiteschema.mpsegment.math.Matrix

class ErrorCalculator {

  var setSize = 0D
  var globalError = 0D

  def updateError(actual: Array[Double], ideal: Array[Double]) {
    globalError += Matrix.arithmetic(actual, ideal, (a, i) => Math.pow(i - a, 2D)).sum
    setSize += actual.size
  }

  def getRootMeanSquare = Math.sqrt(globalError / setSize)
}
