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

  @Test
  def should_throw_exception_when_matrix_form_is_not_match() {
    try {
      val m = Matrix(2, 2, List(1D, 2D, 1D, 2D)) + Matrix(3, 3, List(2D, 1D, 2D, 1D))
      Assert.fail()
    }
    catch {
      case _: Throwable =>
    }
  }

  @Test
  def should_return_all_elements_in_specified_row(){
    val m = Matrix(2, 3, List(1D, 1D, 1D, 2D, 2D, 2D))
    Assert.assertEquals(3, m.row(0).length)
    Assert.assertTrue(m.row(0).forall(doubleEqual(_, 1D)))
  }

  @Test
  def should_return_all_elements_in_specified_col(){
    val m = Matrix(2, 3, List(1D, 2D, 3D, 1D, 2D, 3D))
    val col: Array[Double] = m.col(0)
    Assert.assertEquals(2, col.length)
    Assert.assertTrue(col.forall(doubleEqual(_, 1D)))
  }

  @Test
  def should_return_right_value_when_multiply_number() {
    val m = Matrix(2, 2, List(1D, 1D, 1D, 1D)) x 4D
    Assert.assertTrue(m.flatten.forall(doubleEqual(_, 4D)))
  }

  @Test
  def should_return_right_value_when_multiply_matrices() {
    val m = Matrix(2, 2, List(1D, 1D, 2D, 2D))
    val n = Matrix(2, 2, List(1D, 1D, 2D, 2D))
    val matrix = m x n
    Assert.assertTrue(doubleEqual(3D, matrix(0,0)))
    Assert.assertTrue(doubleEqual(3D, matrix(0,1)))
    Assert.assertTrue(doubleEqual(6D, matrix(1,0)))
    Assert.assertTrue(doubleEqual(6D, matrix(1,1)))
  }

  def doubleEqual(d: Double, other: Double) = (d - other) < 0.00000001D && (d - other) > -0.00000001D
}

trait Matrix {
  def +(n: Double): Matrix

  def +(m: Matrix): Matrix

  def x(n: Double): Matrix

  def x(m: Matrix): Matrix

  def flatten: Array[Double]

  def row(i: Int): Array[Double]

  def col(i: Int): Array[Double]

  val row: Int
  val col: Int

  def apply(i: Int, j:Int): Double
}

object Matrix {
  def apply(row: Int, col: Int) = new DenseMatrix(row, col, new Array[Double](row * col))
  def apply(row: Int, col: Int, data: List[Double]) = new DenseMatrix(row, col, data.toArray)

  def arithmetic(data: Array[Double], other: Array[Double], op: (Double, Double) => Double): Array[Double] =
    (for (i <- 0 until data.length) yield op(data(i), other(i))).toArray
}

class DenseMatrix(val row: Int, val col: Int, data: Array[Double]) extends Matrix {

  def +(n: Double) = new DenseMatrix(row, col, data.map(_ + n))

  def +(m: Matrix) = {
    assert(row == m.row && col == m.col)
    new DenseMatrix(row, col, Matrix.arithmetic(flatten, m.flatten, (a,b) => a + b))
  }

  def x(n: Double) = new DenseMatrix(row, col, data.map(_ * n))

  def x(m: Matrix) = {
    assert(col == m.row)
    val result = Matrix(row, m.col)
    var index = 0;
    for (i <- 0 until row) {
      for (j <- 0 until m.col) {
        result.flatten(index) = Matrix.arithmetic(row(i), col(j), (a,b) => a*b).sum
        index += 1
      }
    }
    result
  }

  implicit def seqToArray(seq: Seq[Double]) = seq.toArray

  def row(i: Int): Array[Double] = for (index <- 0 until data.length; if (index < (i+1)*col) && index >= i * col) yield data(index)

  def col(i: Int): Array[Double] = for (index <- 0 until data.length; if (index % col == i)) yield data(index)

  def apply(i: Int, j:Int): Double = data(row * i + j)

  def flatten = data
}