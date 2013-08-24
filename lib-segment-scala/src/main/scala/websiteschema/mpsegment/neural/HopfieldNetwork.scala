package websiteschema.mpsegment.neural

import websiteschema.mpsegment.math.Matrix

class HopfieldNetwork(val size: Int) {

  private var weightMatrix = Matrix(size)

  def present(pattern: Array[Boolean]): Array[Boolean] = {
    assert(pattern.length == size)
    val input = Matrix(1, size, pattern)
    (for (i <- 0 until size) yield {
      weightMatrix.col(i) * input > 0
    }).toArray
  }

  def train(pattern: Array[Boolean]) {
    assert(pattern.length == size)
    val input = Matrix(1, size, pattern)
    val transposeInput = input.T
    val weight = transposeInput x input
    val identity = Matrix(size, true)

    val m = weight - identity
    weightMatrix = weightMatrix + m
  }

  override def toString() = weightMatrix.toString
}
