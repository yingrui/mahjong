package websiteschema.mpsegment

import org.junit.Assert

object Assertion {

  def shouldBeEqual(expect: Double, actual: Double) {
    Assert.assertTrue(doubleEqual(expect, actual))
  }

  def shouldBeEqual(expect: Double, array: Array[Double]) {
    Assert.assertTrue(array.forall(doubleEqual(expect, _)))
  }

  private def doubleEqual(d: Double, other: Double) = (d == other) || ((d - other) < 0.00000001D && (d - other) > -0.00000001D);

}
