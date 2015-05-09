package me.yingrui.segment.math

import java.util.Random

import org.nd4j.linalg.factory.Nd4j._
import org.scalatest.{FunSuite, Matchers}

class NDMatrixTest extends FunSuite with Matchers {

  test("matrix properties : row, col, apply(i, j), update(i, j)") {
    val m = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))
    m.row should be(2)
    m.col should be(2)

    m(0, 0) should be(1D)
    m(0, 1) should be(2D)
    m(1, 0) should be(3D)
    m(1, 1) should be(4D)

    verifyUpdateElements(m)
  }

  test("matrix properties : flatten, row(i), col(j)") {
    val m = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))
    m.row(0).flatten should be (Array(1D, 2D))
    m.row(1).flatten should be (Array(3D, 4D))

    m.col(0).flatten should be (Array(1D, 3D))
    m.col(1).flatten should be (Array(2D, 4D))

    verifyUpdateElements(m.row(0))
    verifyUpdateElements(m.row(1))
    verifyUpdateElements(m.col(0))
    verifyUpdateElements(m.col(1))
  }

  test("matrix properties : sum") {
    val m = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))

    m.sum should be (10D)
    m.row(0).sum should be (3D)
    m.row(1).sum should be (7D)
    m.col(0).sum should be (4D)
    m.col(1).sum should be (6D)
  }

  test("add : matrix + double -> matrix") {
    val data = create(Array(Array(1D, 2D), Array(3D, 4D)))
    val m = new NDMatrix(data)

    val n = m + 1D
    n.row should be (m.row)
    n.col should be (m.col)

    m(0, 0) should be (1D +- 1e-20)

    n(0, 0) should be (2D +- 1e-20)
    n(0, 1) should be (3D +- 1e-20)
    n(1, 0) should be (4D +- 1e-20)
    n(1, 1) should be (5D +- 1e-20)
  }

  test("add : matrix + matrix -> matrix") {
    val m = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))
    val n = new NDMatrix(create(Array(Array(4D, 3D), Array(2D, 1D))))

    val x = m + n
    m(0, 0) should be (1D)
    x(0, 0) should be (5D)
    x(0, 1) should be (5D)
    x(1, 0) should be (5D)
    x(1, 1) should be (5D)
  }

  test("add : matrix += matrix") {
    val m = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))
    val n = new NDMatrix(create(Array(Array(4D, 3D), Array(2D, 1D))))

    m += n
    n(0, 0) should be (4D)
    m(0, 0) should be (5D)
    m(0, 1) should be (5D)
    m(1, 0) should be (5D)
    m(1, 1) should be (5D)
  }

  test("sub : matrix - double -> matrix") {
    val data = create(Array(Array(1D, 2D), Array(3D, 4D)))
    val m = new NDMatrix(data)

    val n = m - 1D
    n.row should be (m.row)
    n.col should be (m.col)

    m(0, 0) should be (1D)
    n(0, 0) should be (0D)
    n(0, 1) should be (1D)
    n(1, 0) should be (2D)
    n(1, 1) should be (3D)
  }

  test("sub : matrix - matrix -> matrix") {
    val m = new NDMatrix(create(Array(Array(2D, 3D), Array(4D, 5D))))
    val n = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))

    val x = m - n
    n(0, 0) should be (1D)
    x(0, 0) should be (1D)
    x(0, 1) should be (1D)
    x(1, 0) should be (1D)
    x(1, 1) should be (1D)
  }

  test("sub : matrix -= matrix") {
    val m = new NDMatrix(create(Array(Array(2D, 3D), Array(4D, 5D))))
    val n = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))

    m -= n
    n(0, 0) should be (1D)
    m(0, 0) should be (1D)
    m(0, 1) should be (1D)
    m(1, 0) should be (1D)
    m(1, 1) should be (1D)
  }

  test("div : matrix / double -> matrix") {
    val m = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))

    val factor = 5.0D
    val n = m / factor
    m(0, 0) should be (1D)
    n(0, 0) should be (m(0, 0) / factor)
    n(0, 1) should be (m(0, 1) / factor)
    n(1, 0) should be (m(1, 0) / factor)
    n(1, 1) should be (m(1, 1) / factor)
  }

  test("mul : matrix x double -> matrix") {
    val m = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))

    val factor = 5.0D
    val n = m x factor
    m(0, 0) should be (1D)
    n(0, 0) should be (m(0, 0) * factor)
    n(0, 1) should be (m(0, 1) * factor)
    n(1, 0) should be (m(1, 0) * factor)
    n(1, 1) should be (m(1, 1) * factor)
  }

  test("mul : matrix *= double") {
    val m = new NDMatrix(create(Array(Array(1D, 1D), Array(1D, 1D))))

    val factor = 11D
    m *= factor
    m(0, 0) should be (factor)
    m(0, 1) should be (factor)
    m(1, 0) should be (factor)
    m(1, 1) should be (factor)
  }

  test("mul : matrix x matrix -> matrix") {
    val m = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))
    val n = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))

    val x = m x n
    x(0, 0) should be (7D)
    x(0, 1) should be (10D)
    x(1, 0) should be (15D)
    x(1, 1) should be (22D)

    val y = m.row(0) x m.row(0).T
    y(0, 0) should be (5D)
  }

  test("mul : matrix % matrix -> matrix") {
    val m = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))

    val x = m % m
    x(0, 0) should be (1D)
    x(0, 1) should be (4D)
    x(1, 0) should be (9D)
    x(1, 1) should be (16D)
  }

  test("div : matrix / matrix -> matrix") {
    val m = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))

    val x = m / m
    x(0, 0) should be (1D)
    x(0, 1) should be (1D)
    x(1, 0) should be (1D)
    x(1, 1) should be (1D)
  }

  test("mul : matrix * matrix -> double") {
    val m = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))
    m * m should be (30D)
  }

  test("transpose : matrix.T") {
    val m = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))
    val t = m.T
    t(0, 0) should be (1D)
    t(0, 1) should be (3D)
    t(1, 0) should be (2D)
    t(1, 1) should be (4D)

    m(0, 0) should be (1D)
    m(0, 1) should be (2D)
    m(1, 0) should be (3D)
    m(1, 1) should be (4D)

    val t1 = m.row(0).T
    t1(0, 0) should be (1D)
    t1(1, 0) should be (2D)
  }

  test("isVector : m.row") {
    val m = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))
    m.row(0).isVector shouldBe true
    m.row(1).isVector shouldBe true
  }

  test("isColumnVector") {
    val m = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))
    m.col(0).isColumnVector shouldBe true
    m.col(1).isColumnVector shouldBe true
  }

  test("clear matrix") {
    val m = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))
    m.clear
    m(0, 0) should be (0D)
    m(0, 1) should be (0D)
    m(1, 0) should be (0D)
    m(1, 1) should be (0D)
  }

  test("assign matrix") {
    val m = new NDMatrix(create(Array(Array(1D, 2D), Array(3D, 4D))))
    val n = new NDMatrix(create(Array(Array(1D, 1D), Array(1D, 1D))))
    n := m
    n(0, 0) should be (1D)
    n(0, 1) should be (2D)
    n(1, 0) should be (3D)
    n(1, 1) should be (4D)
  }

  test("map by given method") {
    val m = Matrix(1,2, Array(2D, 2D))
    val n = m.map(d => d + 1D)
    n.flatten should be (Array(3D, 3D))
  }

  private def verifyUpdateElements(m: Matrix) {
    for (i <- 0 until m.row; j <- 0 until m.col) {
      val value = new Random().nextDouble()
      m(i, j) = value
      m(i, j) should be(value)
    }
  }

}

