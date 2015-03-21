package websiteschema.mpsegment.neural

import org.junit.{Assert, Test}
import websiteschema.mpsegment.Assertion._

class SigmoidActivationTest {

  @Test
  def should_return_activation() {
    shouldBeEqual(0.5D, Sigmoid().activation(0D))
    Assert.assertTrue(Sigmoid().activation(Double.MaxValue) <= 1D)
    Assert.assertTrue(Sigmoid().activation(Double.MinValue) >= 0D)
  }

  @Test
  def should_return_derivative() {
    shouldBeEqual(0D, Sigmoid().derivative(0D))
    shouldBeEqual(0D, Sigmoid().derivative(1D))
    shouldBeEqual(-1E20, Sigmoid().derivative(Double.MaxValue))
    shouldBeEqual(-1E20, Sigmoid().derivative(Double.MinValue))
  }
}

