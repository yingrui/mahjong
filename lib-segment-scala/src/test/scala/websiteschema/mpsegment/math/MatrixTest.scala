package websiteschema.mpsegment.math

import org.junit.{Assert, Test}

class MatrixTest {

  @Test
  def should_return_matrix_size() {
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
    val m = Matrix(2, 2, Array(1D, 2D, 1D, 2D)) + Matrix(2, 2, Array(2D, 1D, 2D, 1D))
    Assert.assertTrue(m.flatten.forall(doubleEqual(_, 3D)))
  }

  @Test
  def should_return_right_value_when_subtract_number_to_matrix() {
    val m = Matrix(2, 2) - 1
    Assert.assertTrue(m.flatten.forall(doubleEqual(_, -1D)))
  }

  @Test
  def should_return_right_value_when_subtract_other_matrix() {
    val m = Matrix(2, 2, Array(3D, 2D, 3D, 2D)) - Matrix(2, 2, Array(2D, 1D, 2D, 1D))
    Assert.assertTrue(m.flatten.forall(doubleEqual(_, 1D)))
  }

  @Test
  def should_return_right_value_when_divide_number_to_matrix() {
    val m = Matrix(2, 2, Array(10D, 10D, 10D, 10D)) / 10
    Assert.assertTrue(m.flatten.forall(doubleEqual(_, 1D)))
  }

  @Test
  def should_throw_exception_when_matrix_form_is_not_match() {
    try {
      val m = Matrix(2, 2, Array(1D, 2D, 1D, 2D)) + Matrix(3, 3, Array(2D, 1D, 2D, 1D))
      Assert.fail()
    }
    catch {
      case _: Throwable =>
    }
  }

  @Test
  def should_return_all_elements_in_specified_row() {
    val m = Matrix(2, 3, Array(1D, 1D, 1D, 2D, 2D, 2D))
    Assert.assertEquals(3, m.row(0).length)
    Assert.assertTrue(m.row(0).forall(doubleEqual(_, 1D)))
  }

  @Test
  def should_return_all_elements_in_specified_col() {
    val m = Matrix(2, 3, Array(1D, 2D, 3D, 1D, 2D, 3D))
    val col: Array[Double] = m.col(0)
    Assert.assertEquals(2, col.length)
    Assert.assertTrue(col.forall(doubleEqual(_, 1D)))
  }

  @Test
  def should_return_right_value_when_multiply_number() {
    val m = Matrix(2, 2, Array(1D, 1D, 1D, 1D)) x 4D
    Assert.assertTrue(m.flatten.forall(doubleEqual(_, 4D)))
  }

  @Test
  def should_return_right_value_when_dot_product_two_matrix() {
    val dotProductResult = Matrix(1, 4, Array(1D, 1D, 1D, 1D)) * Matrix(1, 4, Array(1D, 1D, 1D, 1D))
    shouldBeEqual(4D, dotProductResult)
  }

  @Test
  def should_enable_apply_method_to_assign_value() {
    val m = Matrix(2, 2, Array(1D, 1D, 2D, 2D))
    shouldBeEqual(1D, m(0,0))
    shouldBeEqual(1D, m(0,1))
    m(0,1) = 2D
    shouldBeEqual(2D, m(0,1))
  }

  @Test
  def should_return_right_value_when_multiply_matrices() {
    val m = Matrix(2, 2, Array(1D, 1D, 2D, 2D))
    val n = Matrix(2, 2, Array(1D, 1D, 2D, 2D))
    val matrix = m x n
    shouldBeEqual(3D, matrix(0, 0))
    shouldBeEqual(3D, matrix(0, 1))
    shouldBeEqual(6D, matrix(1, 0))
    shouldBeEqual(6D, matrix(1, 1))
  }

  @Test
  def should_transpose_matrix() {
    val m = Matrix(2, 3, Array(1D, 2D, 3D, 4D, 5D, 6D))
    val n = m.T
    shouldBeEqual(1D, n(0, 0))
    shouldBeEqual(4D, n(0, 1))
    shouldBeEqual(2D, n(1, 0))
    shouldBeEqual(5D, n(1, 1))
    shouldBeEqual(3D, n(2, 0))
    shouldBeEqual(6D, n(2, 1))
  }

  @Test
  def should_return_true_if_this_matrix_is_a_vector() {
    val m = Matrix(1,2, Array(1D, 2D))
    Assert.assertTrue(m.isVector)
    val n = Matrix(2,2, Array(1D, 2D, 1D, 2D))
    Assert.assertFalse(n.isVector)
  }

  private def shouldBeEqual(d1: Double, d2: Double) {
    Assert.assertTrue(doubleEqual(d1, d2))
  }

  def doubleEqual(d: Double, other: Double) = (d - other) < 0.00000001D && (d - other) > -0.00000001D
}

