package websiteschema.mpsegment.math

import org.junit.{Assert, Test}
import collection.immutable.IndexedSeq

class MatrixTest {
  @Test
  def should_get_element_number_in_matrix() {
    val m = Matrix(2, 2)
    Assert.assertEquals(4, m.flatten.length)
    Assert.assertTrue(m.flatten.forall(doubleEqual(_, 0D)))
  }

  @Test
  def should_return_right_value_when_add_number_to_matrix() {
    val m = Matrix(2, 2) + 1
    Assert.assertTrue(m.flatten.forall(doubleEqual(_, 1D)))
  }

  @Test
  def should_return_right_value_when_add_other_matrix() {
    val m = Matrix(2, 2, List(1D, 2D, 1D, 2D)) + Matrix(2, 2, List(2D, 1D, 2D, 1D))
    Assert.assertTrue(m.flatten.forall(doubleEqual(_, 3D)))
  }

  def doubleEqual(d: Double, other: Double) = (d - other) < 0.00000001D && (d - other) > -0.00000001D
}

trait Matrix {
  def +(n: Int): Matrix

  def +(m: Matrix): Matrix

  def flatten: Array[Double]
}

object Matrix {
  def apply(row: Int, col: Int) = new DenseMatrix(row, col, new Array[Double](row * col))
  def apply(row: Int, col: Int, data: List[Double]) = new DenseMatrix(row, col, data.toArray)

  def arithmetic(data: Array[Double], other: Array[Double], op: (Double, Double) => Double): Array[Double] =
    (for (i <- 0 until data.length) yield op(data(i), other(i))).toArray
}

class DenseMatrix(row: Int, col: Int, data: Array[Double]) extends Matrix {

  def +(n: Int) = new DenseMatrix(row, col, data.map(_ + n))

  def +(m: Matrix) = new DenseMatrix(row, col, Matrix.arithmetic(flatten, m.flatten, (a,b) => a + b))

  def flatten = data

}