package me.yingrui.segment.neural

import org.junit.Test
import me.yingrui.segment.Assertion
import me.yingrui.segment.math.Matrix

class NormalizerTest {

  @Test
  def should_normalize_vector {
    val normalizer = new Normalizer(Array(-0.7), Array(5.0))
    val input = Matrix(Array(1.8))
    val output = normalizer.normalize(input)
    Assertion.shouldBeEqual(output(0, 0), 0.5)
  }

  @Test
  def should_not_consider_the_range_when_it_is_zero {
    val normalizer = new Normalizer(Array(-0.7), Array(0.0))
    val input = Matrix(Array(1.8))
    val output = normalizer.normalize(input)
    Assertion.shouldBeEqual(output(0, 0), 2.5)
  }

  @Test
  def should_return_one_when_it_is_NaN {
    val normalizer = new Normalizer(Array(-0.7), Array(5.0))
    val input = Matrix(Array(Double.NaN))
    val output = normalizer.normalize(input)
    Assertion.shouldBeEqual(output(0, 0), 1.0)
  }

}
