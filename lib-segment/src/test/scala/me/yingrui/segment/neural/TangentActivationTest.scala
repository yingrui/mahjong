package me.yingrui.segment.neural

import org.junit.Assert.assertTrue
import org.junit.{Assert, Test}
import me.yingrui.segment.Assertion._
import me.yingrui.segment.math.Matrix

class TangentActivationTest {

  @Test
  def should_return_activation {
    shouldBeEqual(Matrix(Array(0D)), Tangent().activate(Matrix(Array(0D))))
  }

  @Test
  def should_return_activation_when_input_is_max_value {
    shouldBeEqual(Matrix(Array(1D)), Tangent().activate(Matrix(Array(100D))))
  }

  @Test
  def should_return_activation_when_input_is_min_value {
    shouldBeEqual(Matrix(Array(-1D)), Tangent().activate(Matrix(Array(-100D))))
  }

  @Test
  def should_return_derivative {
    shouldBeEqual(Matrix(Array(1D)), Tangent().getDerivative(Matrix(Array(0D))))
  }

  @Test
  def should_return_derivative_when_input_is_max_value {
    shouldBeEqual(Matrix(Array(0D)), Tangent().getDerivative(Matrix(Array(Double.MaxValue))))
  }

  @Test
  def should_return_derivative_when_input_is_min_value {
    shouldBeEqual(Matrix(Array(0D)), Tangent().getDerivative(Matrix(Array(Double.MinValue))))
  }
}
