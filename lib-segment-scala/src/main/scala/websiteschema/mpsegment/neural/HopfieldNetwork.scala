package websiteschema.mpsegment.neural

import websiteschema.mpsegment.math.Matrix

class HopfieldNetwork(val size: Int) {

  private var weightMatrix = Matrix(size)
  private val identity = Matrix(size, true)

  private def update(weight: Matrix): Unit = {
    weightMatrix = weight
  }

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
    val weight = input.T x input

    weight -= identity
    weightMatrix += weight
  }

  override def toString() = weightMatrix.toString
}

object HopfieldNetwork {

  def apply(weight: Matrix): HopfieldNetwork = {
    val network = new HopfieldNetwork(weight.row)
    network.update(weight)
    network
  }
}