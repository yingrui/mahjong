package websiteschema.mpsegment.neural

import org.junit.Assert
import org.junit.Test

class ErrorCaculatorTest {

  @Test
  def should_return_root_mean_square_error() {
    val actual = Array(1D, 2D, 3D, 5D)
    val ideal = Array(1D, 2D, 3D, 4D)

    val error = new ErrorCaculator()
    error.updateError(actual, ideal)
    val rms = error.getRootMeanSquare

    Assert.assertTrue(rms - 0.5D > -0.0000001 && rms - 0.5D < 0.0000001)
  }

  def assertArrayEquals(expect: Array[Boolean], actual: Array[Boolean]) {
    0 until expect.length foreach ((i: Int) => Assert.assertEquals(expect(i), actual(i)))
  }
}
