package websiteschema.mpsegment.neural

import org.junit.{Assert, Test}
import websiteschema.mpsegment.Assertion._

class SigmoidActivationTest {

  @Test
  def should_return_activation() {
    shouldBeEqual(0.5D, Sigmoid.activation(0D))
    Assert.assertTrue(Sigmoid.activation(100D) <= 1D)
    Assert.assertTrue(Sigmoid.activation(-100D) >= 0D)
  }

  @Test
  def should_return_derivative() {
    shouldBeEqual(0D, Sigmoid.derivative(0D))
    shouldBeEqual(Double.NegativeInfinity, Sigmoid.derivative(Double.MaxValue))
    shouldBeEqual(Double.NegativeInfinity, Sigmoid.derivative(Double.MinValue))
  }
}

