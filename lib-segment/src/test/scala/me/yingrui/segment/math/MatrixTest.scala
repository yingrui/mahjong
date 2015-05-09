package me.yingrui.segment.math

import me.yingrui.segment.Assertion._
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}

class MatrixTest extends FunSpec with Matchers with BeforeAndAfter {

  after {
    Matrix.restoreMatrixBuilder
  }

  describe("No matter different matrix builder, a matrix") {
    val matrixBuilders = Array[MatrixBuilder](new DenseMatrixBuilder(), new NDMatrixBuilder())

    it("should_return_matrix_size") {
      matrixBuilders.foreach(builder => {Matrix.setMatrixBuilder(builder)
        val m = Matrix(2, 2)
        m.flatten.length shouldBe 4
        shouldBeEqual(0D, m.flatten)
      })
    }

    it("should_return_right_value_when_add_number_to_matrix") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(2, 2) + 1
        shouldBeEqual(1D, m.flatten)
      })
    }

    it("should_return_right_value_when_add_other_matrix") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(2, 2, Array(1D, 2D, 1D, 2D)) + Matrix(2, 2, Array(2D, 1D, 2D, 1D))
        shouldBeEqual(3D, m.flatten)
      })
    }

    it("should_return_right_value_when_subtract_number_to_matrix") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(2, 2) - 1
        shouldBeEqual(-1D, m.flatten)
      })
    }

    it("should_return_right_value_when_subtract_other_matrix") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(2, 2, Array(3D, 2D, 3D, 2D)) - Matrix(2, 2, Array(2D, 1D, 2D, 1D))
        shouldBeEqual(1D, m.flatten)
      })
    }

    it("should_return_right_value_when_divide_number_to_matrix") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(2, 2, Array(10D, 10D, 10D, 10D)) / 10
        shouldBeEqual(1D, m.flatten)
      })
    }

    it("should_return_right_value_when_divide_matrix_to_matrix") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(2, 2, Array(1D, 2D, 3D, 4D)) / Matrix(2, 2, Array(1D, 2D, 3D, 4D))
        shouldBeEqual(1D, m.flatten)
      })
    }

    it("should_throw_exception_when_matrix_form_is_not_match") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        try {
          val m = Matrix(2, 2, Array(1D, 2D, 1D, 2D)) + Matrix(3, 3, Array(2D, 1D, 2D, 1D))
          fail()
        }
        catch {
          case _: Throwable =>
        }
      })
    }

    it("should_return_all_elements_in_specified_row") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(Array(Array(1D, 1D, 1D), Array(2D, 2D, 2D)))
        m.row(0).col shouldBe 3
        shouldBeEqual(1D, m.row(0).flatten)
      })
    }

    it("should_return_all_elements_in_specified_col") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(Array(Array(1D, 2D, 3D), Array(1D, 2D, 3D)))
        m.col(0).row shouldBe 2
        shouldBeEqual(1D, m.col(0).flatten)
      })
    }

    it("should_return_right_value_when_multiply_number") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(2, 2, Array(1D, 1D, 1D, 1D)) x 4D
        shouldBeEqual(4D, m.flatten)
      })
    }

    it("should_return_right_value_when_dot_product_two_matrix") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val dotProductResult = Matrix(1, 4, Array(1D, 1D, 1D, 1D)) * Matrix(1, 4, Array(1D, 1D, 1D, 1D))
        shouldBeEqual(4D, dotProductResult)
      })
    }

    it("should_enable_apply_method_to_assign_value") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(Array(Array(1D, 1D), Array(2D, 2D)))
        shouldBeEqual(1D, m(0, 0))
        shouldBeEqual(1D, m(0, 1))
        m(0, 1) = 2D
        shouldBeEqual(2D, m(0, 1))
      })
    }

    it("should_return_right_value_when_multiply_matrices") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(Array(Array(1D, 1D), Array(2D, 2D)))
        val n = Matrix(Array(Array(1D, 1D), Array(2D, 2D)))
        val matrix = m x n
        shouldBeEqual(3D, matrix(0, 0))
        shouldBeEqual(3D, matrix(0, 1))
        shouldBeEqual(6D, matrix(1, 0))
        shouldBeEqual(6D, matrix(1, 1))
      })
    }

    it("should_transpose_matrix") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(Array(Array(1D, 2D, 3D), Array(4D, 5D, 6D)))
        val n = m.T
        shouldBeEqual(1D, n(0, 0))
        shouldBeEqual(4D, n(0, 1))
        shouldBeEqual(2D, n(1, 0))
        shouldBeEqual(5D, n(1, 1))
        shouldBeEqual(3D, n(2, 0))
        shouldBeEqual(6D, n(2, 1))
      })
    }

    it("should_return_true_if_this_matrix_is_a_vector") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(1, 2, Array(1D, 2D))
        m.isVector shouldBe true
        val n = Matrix(2, 2, Array(1D, 2D, 1D, 2D))
        n.isVector shouldBe false
      })
    }

    it("should_return_mapped_matrix") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(1, 2, Array(2D, 2D))
        val n = m.map(d => d + 1D)
        shouldBeEqual(3D, n.flatten)
      })
    }

    it("should_return_true_when_compare_two_matrix") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(Array(Array(2D, 2D), Array(3D, 3D)))
        val n = Matrix(Array(Array(2D, 2D), Array(3D, 3D)))
        m equalsTo n shouldBe true
      })
    }

    it("should_return_false_when_compare_two_matrix") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(2, 2, Array(2D, 2D, 3D, 3D))
        val n = Matrix(2, 2, Array(2D, 2D, 3D, 4D))
        m equalsTo n shouldBe false
      })
    }

    it("should_clear_matrix") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(2, 2, Array(2D, 2D, 3D, 3D))
        m.clear
        shouldBeEqual(0D, m.flatten)
      })
    }

    it("should_return_sum_of_all_elements") {
      matrixBuilders.foreach(builder => {
        Matrix.setMatrixBuilder(builder)
        val m = Matrix(2, 2, Array(2D, 2D, 3D, 3D))
        shouldBeEqual(10D, m.sum)
      })
    }
  }
}

