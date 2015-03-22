package websiteschema.mpsegment

import org.junit.Assert._
import websiteschema.mpsegment.math.Matrix

object Assertion {

  def shouldBeEqual(expect: Double, actual: Double) {
    assertTrue(s"$expect is not equals to $actual", doubleEqual(expect, actual))
  }

  def shouldBeEqual(expect: Matrix, actual: Matrix) {
    assertEquals(expect.col, expect.col)
    assertEquals(expect.row, expect.row)

    val expectValues = expect.flatten
    val actualValues = actual.flatten

    (0 until expectValues.length).foreach(i => shouldBeEqual(expectValues(i), actualValues(i)))
  }

  def shouldBeEqual(expect: Double, array: Array[Double]) {
    assertTrue(array.forall(doubleEqual(expect, _)))
  }

  private def doubleEqual(d: Double, other: Double) = (d == other) || ((d - other) < 0.00000001D && (d - other) > -0.00000001D);

}
