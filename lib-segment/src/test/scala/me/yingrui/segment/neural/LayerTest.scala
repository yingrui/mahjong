package me.yingrui.segment.neural

import org.junit.{Assert, Test}
import me.yingrui.segment.Assertion._
import me.yingrui.segment.math.Matrix

class LayerTest {

  @Test
  def should_return_number_of_neural {
    val layer = SigmoidLayer(Matrix(3, 2))
    Assert.assertEquals(2, layer.size)
  }

  @Test
  def should_return_output {
    val weight = Matrix(3, 2, Array(1D, 1D, 1D, 1D, 1D, 1D))
    val layer = new SingleLayer(weight, Sigmoid())

    val output = layer.computeOutput(Matrix(Array(1D, 1D)))
    println(output)
    shouldBeEqual(0.9525741268224334D, output.flatten)
  }


}

