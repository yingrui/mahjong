package me.yingrui.segment.crf

import org.junit.Test
import me.yingrui.segment.Assertion.shouldBeEqual
import me.yingrui.segment.math.Matrix

class LBFGSTest {

  class F extends Function {

    private var currentX: Matrix = null

    def valueAt(x: Matrix): Double = {
      currentX = x
      x.flatten.map(x_i => x_i * x_i).sum
    }

    def derivative: Matrix = currentX x 2
  }

  @Test
  def should {
    val optimizedPoint = LBFGS(Matrix(Array(2.0, 3.0))).search(new F())
    shouldBeEqual(0D, optimizedPoint.flatten)
  }

}
