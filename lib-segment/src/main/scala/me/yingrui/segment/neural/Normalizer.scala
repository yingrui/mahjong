package me.yingrui.segment.neural

import me.yingrui.segment.math.Matrix

class Normalizer(bases: Array[Double], ranges: Array[Double]) {

  assert(bases.length == ranges.length)

  private def normalizeReal(input: Double, base: Double, range: Double) =
    if (input.isNaN)
      1.0
    else if (range > 0.000001)
      (input - base) / range
    else
      input - base

  def normalize(input: Matrix): Matrix = {
    assert(input.isVector && input.col == bases.length)
    val output = (0 until input.col).map(i => {
      normalizeReal(input(0, i), bases(i), ranges(i))
    })
    Matrix(output)
  }

}
