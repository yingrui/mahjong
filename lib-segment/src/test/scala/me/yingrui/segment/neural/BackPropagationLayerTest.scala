package me.yingrui.segment.neural

import me.yingrui.segment.Assertion.shouldBeEqual
import me.yingrui.segment.math.Matrix
import org.junit.Assert.assertTrue
import org.junit.Test

class BackPropagationLayerTest {

  @Test
  def should_record_input_and_output {
    val layer = new BPSigmoidLayer(Matrix(2, 2, Array(1D, 1D, 1D, 1D)), Matrix(Array(1D, 1D)), false)
    val input = Matrix(Array(2D, 2D))
    val output = layer.computeOutput(input)
    shouldBeEqual(input, layer.input)
    shouldBeEqual(output, layer.output)
  }

  @Test
  def should_accumulate_error_delta {
    val layer = new BPSigmoidLayer(Matrix(3, 2, Array(1D, 1D, 1D, 1D, 1D, 1D, 1D, 1D)), Matrix(Array(1D, 1D)), false)
    layer.accumulateDelta.clear
    layer.input := Matrix(Array(2D, 2D, 2D))
    layer.output := Matrix(Array(0.9D, 0.9D))
    layer.propagateError(Matrix(Array(-1D, -1D)))
    val expectedAccumulateDelta = -0.17999999999999997D
    val expectedAccumulateBiasDelta = -0.08999999999999998D
    assertTrue(layer.accumulateDelta.flatten.forall(a => a == expectedAccumulateDelta))
    assertTrue(layer.accumulateBiasDelta.flatten.forall(a => a == expectedAccumulateBiasDelta))
  }
}
