package websiteschema.mpsegment.math

trait Matrix {
  def +(n: Double): Matrix

  def +(m: Matrix): Matrix

  def -(n: Double): Matrix

  def -(m: Matrix): Matrix

  def x(n: Double): Matrix

  def x(m: Matrix): Matrix

  def *(m: Matrix): Double

  def /(n: Double): Matrix

  def T: Matrix

  def flatten: Array[Double]

  def row(i: Int): Array[Double]

  def col(i: Int): Array[Double]

  val row: Int
  val col: Int

  def isVector: Boolean

  def apply(i: Int, j: Int): Double

  def update(i: Int, j: Int, value: Double)
}

object Matrix {
  def apply(row: Int, col: Int) = new DenseMatrix(row, col, new Array[Double](row * col))

  def apply(size: Int, identity: Boolean = false) = {
    val m = new DenseMatrix(size, size, new Array[Double](size * size))
    if (identity) {
      0 until size foreach ((i: Int) => { m(i, i) = 1D })
    }
    m
  }

  def apply(data: Array[Double]) = new DenseMatrix(1, data.length, data)

  def apply(row: Int, col: Int, data: Array[Double]) = new DenseMatrix(row, col, data)

  def apply(row: Int, col: Int, data: Array[Boolean]) = new DenseMatrix(row, col, data.map(b => if (b) 1D else -1D))

  def arithmetic(data: Array[Double], other: Array[Double], op: (Double, Double) => Double): Array[Double] =
    (for (i <- 0 until data.length) yield op(data(i), other(i))).toArray
}

class DenseMatrix(val row: Int, val col: Int, data: Array[Double]) extends Matrix {

  def +(n: Double) = new DenseMatrix(row, col, data.map(_ + n))

  def +(m: Matrix) = {
    assert(row == m.row && col == m.col)
    new DenseMatrix(row, col, Matrix.arithmetic(flatten, m.flatten, (a, b) => a + b))
  }

  def -(n: Double) = new DenseMatrix(row, col, data.map(_ - n))

  def -(m: Matrix) = {
    assert(row == m.row && col == m.col)
    new DenseMatrix(row, col, Matrix.arithmetic(flatten, m.flatten, (a, b) => a - b))
  }

  def x(n: Double) = new DenseMatrix(row, col, data.map(_ * n))

  def x(m: Matrix) = {
    assert(col == m.row)
    val result = Matrix(row, m.col)
    for (i <- 0 until row) {
      for (j <- 0 until m.col) {
        result(i, j) = Matrix.arithmetic(row(i), m.col(j), (a, b) => a * b).sum
      }
    }
    result
  }

  def *(m: Matrix) = {
    assert(row == 1 && m.row == 1)
    Matrix.arithmetic(row(0), m.row(0), (a, b) => a * b).sum
  }

  def /(n: Double) = new DenseMatrix(row, col, data.map(_ / n))

  def T = {
    val inverse = new Array[Double](data.length)
    for (i <- 0 until row) {
      for (j <- 0 until col) {
        inverse(row * j + i) = apply(i, j)
      }
    }
    new DenseMatrix(col, row, inverse)
  }

  implicit def seqToArray(seq: Seq[Double]) = seq.toArray

  def row(i: Int): Array[Double] = for (index <- i * col until (i + 1) * col) yield data(index)

  def col(i: Int): Array[Double] = for (index <- 0 until data.length; if (index % col == i)) yield data(index)

  def apply(i: Int, j: Int): Double = data(col * i + j)

  def update(i: Int, j: Int, value: Double) {
    data(col * i + j) = value
  }

  def flatten = data

  def isVector = row == 1

  override def toString: String = (for (i <- 0 until row) yield {
    row(i) mkString ", "
  }) mkString "\n"
}