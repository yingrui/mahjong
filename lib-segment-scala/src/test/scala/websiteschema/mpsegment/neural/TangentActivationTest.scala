package websiteschema.mpsegment.neural

import org.junit.{Assert, Test}
import websiteschema.mpsegment.Assertion._

class TangentActivationTest {

  @Test
  def should_return_activation {
    shouldBeEqual(0D, Tangent.activation(0D))
    Assert.assertTrue(Tangent.activation(100D) <= 1D)
    Assert.assertTrue(Tangent.activation(-100D) >= -1D)
  }

  @Test
  def should_return_derivative() {
    shouldBeEqual(1D, Tangent.derivative(0D))
  }
}
