package me.yingrui.segment.neural

import org.junit.{Assert, Test}
import me.yingrui.segment.math.Matrix
import me.yingrui.segment.Assertion.shouldBeEqual

class BackPropagationLayerTest {

  @Test
  def should_record_input_and_output {
    val layer = new BPSigmoidLayer(Matrix(2, 2, Array(1D, 1D, 1D, 1D)), Matrix(Array(1D, 1D)))
    val input = Matrix(Array(2D, 2D))
    val output = layer.computeOutput(input)
    shouldBeEqual(input, layer.input)
    shouldBeEqual(output, layer.output)
  }

  @Test
  def should_accumulate_error_delta {
    val layer = new BPSigmoidLayer(Matrix(3, 2, Array(1D, 1D, 1D, 1D, 1D, 1D, 1D, 1D)), Matrix(Array(1D, 1D)))
    layer.accumulateDelta.clear
    layer.input := Matrix(Array(2D, 2D, 2D))
    layer.propagateError(Matrix(Array(-6D, -6D)))
    val expect = Matrix(3, 2, Array(-12D, -12D, -12D, -12D, -12D, -12D))
    println(layer.accumulateDelta)
    Assert.assertTrue(expect == layer.accumulateDelta)
    Assert.assertTrue(Matrix(Array(-6D, -6D)) == layer.accumulateBiasDelta)
  }
}
