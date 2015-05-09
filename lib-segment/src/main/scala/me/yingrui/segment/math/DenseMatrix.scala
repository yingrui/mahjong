package me.yingrui.segment.math

class DenseMatrix(val row: Int, val col: Int, data: Array[Double]) extends Matrix {

  def +(n: Double) = new DenseMatrix(row, col, data.map(_ + n))

  def +(m: Matrix) = {
    assert(row == m.row && col == m.col)
    new DenseMatrix(row, col, arithmetic(flatten, m.flatten, (a, b) => a + b))
  }

  def +=(m: Matrix): Unit = {
    assert(row == m.row && col == m.col)
    (0 until data.length).foreach(i => data(i) += m.flatten(i))
  }

  def -(n: Double) = new DenseMatrix(row, col, data.map(_ - n))

  def -(m: Matrix) = {
    assert(row == m.row && col == m.col)
    new DenseMatrix(row, col, arithmetic(flatten, m.flatten, (a, b) => a - b))
  }

  def -=(m: Matrix): Unit = {
    assert(row == m.row && col == m.col)
    (0 until data.length).foreach(i => data(i) -= m.flatten(i))
  }

  def x(n: Double) = new DenseMatrix(row, col, data.map(_ * n))

  def *= (n: Double) {
    (0 until data.length).foreach(i => data(i) *= n)
  }

  def x(m: Matrix) = {
    assert(col == m.row)
    val result = Matrix(row, m.col)
    for (i <- 0 until row) {
      for (j <- 0 until m.col) {
        result(i, j) = arithmetic(row(i).flatten, m.col(j).flatten, (a, b) => a * b).sum
      }
    }
    result
  }

  def %(m: Matrix) = {
    assert(col == m.col && row == m.row)
    Matrix(row, col, arithmetic(flatten, m.flatten, (a, b) => a * b))
  }

  def *(m: Matrix) = arithmetic(flatten, m.flatten, (a, b) => a * b).sum

  def /(n: Double) = new DenseMatrix(row, col, data.map(_ / n))

  def /(m: Matrix) = {
    assert(col == m.col && row == m.row)
    Matrix(row, col, arithmetic(flatten, m.flatten, (a, b) => a / b))
  }

  def T = {
    val inverse = new Array[Double](data.length)
    for (i <- 0 until row) {
      for (j <- 0 until col) {
        inverse(row * j + i) = apply(i, j)
      }
    }
    new DenseMatrix(col, row, inverse)
  }

  def row(i: Int): Matrix = new DenseMatrix(1, col, (for (index <- i * col until (i + 1) * col) yield data(index)).toArray)

  def col(i: Int): Matrix = new DenseMatrix(row, 1, (for (index <- 0 until data.length; if (index % col == i)) yield data(index)).toArray)

  def apply(i: Int, j: Int): Double = data(col * i + j)

  def update(i: Int, j: Int, value: Double) {
    data(col * i + j) = value
  }

  def flatten = data

  def isVector = row == 1

  def isColumnVector = col == 1

  def clear {
    (0 until data.length).foreach(data(_) = 0D)
  }

  def sum: Double = data.sum

  def map(compute: (Double) => Double): Matrix = new DenseMatrix(row, col, data.map(compute))

  override def :=(other: Matrix): Unit = Array.copy(other.flatten, 0, data, 0, data.length)

  override def toString: String = (for (i <- 0 until row) yield {
    row(i).flatten mkString ", "
  }) mkString "\n"

  private def arithmetic(data: Array[Double], other: Array[Double], op: (Double, Double) => Double): Array[Double] =
    (for (i <- 0 until data.length) yield op(data(i), other(i))).toArray

}
