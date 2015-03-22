package websiteschema.mpsegment.neural

import org.junit.Test
import websiteschema.mpsegment.Assertion._
import websiteschema.mpsegment.math.Matrix

class SoftmaxActivationTest {

  @Test
  def should_return_activation {
    shouldBeEqual(Matrix(Array(1D)), Softmax().activate(Matrix(Array(1D))))

    shouldBeEqual(Matrix(Array(0.5D, 0.5D)), Softmax().activate(Matrix(Array(1D, 1D))))
  }

  @Test
  def should_return_activation_when_input_is_max_value {
    shouldBeEqual(Matrix(Array(1D, 0D)), Softmax().activate(Matrix(Array(Double.MaxValue, 1D))))
  }

  @Test
  def should_return_activation_when_input_is_min_value {
    shouldBeEqual(Matrix(Array(0D, 1D)), Softmax().activate(Matrix(Array(Double.MinValue, 1D))))
  }

//  @Test
//  def should_return_derivative {
//    shouldBeEqual(Matrix(Array(0D)), Softmax().getDerivative(Matrix(Array(1D))))
//  }
//
//  @Test
//  def should_return_derivative_when_input_is_max_value {
//    shouldBeEqual(Matrix(Array(0D)), Tangent().getDerivative(Matrix(Array(Double.MaxValue))))
//  }
//
//  @Test
//  def should_return_derivative_when_input_is_min_value {
//    shouldBeEqual(Matrix(Array(0D)), Tangent().getDerivative(Matrix(Array(Double.MinValue))))
//  }
}
