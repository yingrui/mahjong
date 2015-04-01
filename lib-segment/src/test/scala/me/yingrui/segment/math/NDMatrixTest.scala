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
    n.col should be (n.col)

    m(0, 0) should be (1D +- 1e-20)

    n(0, 0) should be (2D +- 1e-20)
    n(0, 1) should be (3D +- 1e-20)
    n(1, 0) should be (4D +- 1e-20)
    n(1, 1) should be (5D +- 1e-20)
  }

  private def verifyUpdateElements(m: Matrix) {
    for (i <- 0 until m.row; j <- 0 until m.col) {
      val value = new Random().nextDouble()
      m(i, j) = value
      m(i, j) should be(value)
    }
  }

}

