package websiteschema.mpsegment.neural

import org.junit.Test
import websiteschema.mpsegment.Assertion._
import websiteschema.mpsegment.math.Matrix

class SigmoidActivationTest {

  @Test
  def should_return_activation {
    shouldBeEqual(Matrix(Array(0.5D)), Sigmoid().activate(Matrix(Array(0D))))
  }

  @Test
  def should_return_activation_when_input_is_max_value {
    shouldBeEqual(Matrix(Array(1D)), Sigmoid().activate(Matrix(Array(Double.MaxValue))))
  }

  @Test
  def should_return_activation_when_input_is_min_value {
    shouldBeEqual(Matrix(Array(0D)), Sigmoid().activate(Matrix(Array(Double.MinValue))))
  }

  @Test
  def should_return_derivative {
    shouldBeEqual(Matrix(Array(0D)), Sigmoid().getDerivative(Matrix(Array(1D))))
    shouldBeEqual(Matrix(Array(0D)), Sigmoid().getDerivative(Matrix(Array(0D))))
  }

  @Test
  def should_return_derivative_when_input_is_max_value {
    shouldBeEqual(Matrix(Array(-1E20)), Sigmoid().getDerivative(Matrix(Array(Double.MaxValue))))
  }

  @Test
  def should_return_derivative_when_input_is_min_value {
    shouldBeEqual(Matrix(Array(-1E20)), Sigmoid().getDerivative(Matrix(Array(Double.MinValue))))
  }
}

