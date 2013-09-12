package websiteschema.mpsegment.neural

import websiteschema.mpsegment.math.Matrix

class Normalizer(bases: Array[Double], ranges: Array[Double]) {

  assert(bases.length == ranges.length)

  def normalizeReal(input: Double, base: Double, range: Double) = if (range > 0.000001) (input - base) / range else input - base

  def normalize(input: Matrix): Matrix = {
    assert(input.isVector && input.col == bases.length)

    val data = input.flatten
    val output = for (i <- 0 until data.length) yield {
      normalizeReal(data(i), bases(i), ranges(i))
    }
    Matrix(output)
  }

}
