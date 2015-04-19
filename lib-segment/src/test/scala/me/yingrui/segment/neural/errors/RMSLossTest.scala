package me.yingrui.segment.neural.errors

import me.yingrui.segment.math.Matrix
import org.junit.{Assert, Test}

class RMSLossTest {

  @Test
  def should_return_root_mean_square_error() {
    val actual = Array(1D, 2D, 3D, 5D)
    val ideal = Array(1D, 2D, 3D, 4D)

    val error = new RMSLoss()
    error.updateError(Matrix(actual), Matrix(ideal))
    val rms = error.loss

    Assert.assertTrue(rms - 0.5D > -0.0000001 && rms - 0.5D < 0.0000001)
  }

  def assertArrayEquals(expect: Array[Boolean], actual: Array[Boolean]) {
    0 until expect.length foreach ((i: Int) => Assert.assertEquals(expect(i), actual(i)))
  }
}
