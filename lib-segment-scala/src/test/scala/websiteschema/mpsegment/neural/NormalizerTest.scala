package websiteschema.mpsegment.neural

import org.junit.Test
import websiteschema.mpsegment.Assertion
import websiteschema.mpsegment.math.Matrix

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

}
