package me.yingrui.segment.math

trait Matrix {
  def +(n: Double): Matrix

  def +(m: Matrix): Matrix

  def +=(m: Matrix): Unit

  def -(n: Double): Matrix

  def -(m: Matrix): Matrix

  def -=(m: Matrix): Unit

  def x(n: Double): Matrix
  def x(m: Matrix): Matrix
  def %(m: Matrix): Matrix
  def *=(n: Double): Unit

  def *(m: Matrix): Double

  def /(n: Double): Matrix

  def T: Matrix

  def flatten: Array[Double]

  def row(i: Int): Matrix

  def col(i: Int): Matrix

  val row: Int
  val col: Int

  def isVector: Boolean

  def isColumnVector: Boolean

  def clear: Unit

  def apply(i: Int, j: Int): Double

  def update(i: Int, j: Int, value: Double)

  def :=(other: Matrix): Unit

  def sum: Double

  def map(compute: (Double) => Double): Matrix
}

object Matrix {

  private val defaultMatrixBuilder: MatrixBuilder = new NDMatrixBuilder()
  //  private val defaultMatrixBuilder: MatrixBuilder = new DenseMatrixBuilder()
  private var matrixBuilder: MatrixBuilder = defaultMatrixBuilder

  def setMatrixBuilder(builder: MatrixBuilder) {
    matrixBuilder = builder
  }

  def restoreMatrixBuilder {
    matrixBuilder = defaultMatrixBuilder
  }

  def vector(d: Double*): Matrix = matrixBuilder.vector(d)

  def apply(row: Int, col: Int): Matrix = matrixBuilder.apply(row, col)

  def apply(size: Int, identity: Boolean = false): Matrix = matrixBuilder.apply(size, identity)

  def apply(data: Array[Double]): Matrix = matrixBuilder.apply(data)

  def apply(data: Array[Array[Double]]): Matrix = matrixBuilder.apply(data)

  def apply(data: Seq[Double]): Matrix = matrixBuilder.apply(data)

  def apply(row: Int, col: Int, data: Array[Double]): Matrix = matrixBuilder.apply(row, col, data)

  def apply(row: Int, col: Int, data: Array[Boolean]): Matrix = matrixBuilder.applyBoolean(row, col, data)

  def randomize(row: Int, col: Int, min: Double, max: Double) = matrixBuilder.randomize(row, col, min, max)

  def randomize(row: Int, col: Int): Matrix = matrixBuilder.randomize(row, col, 1e-4D, 0.1D)


  implicit class RichMatrix(matrix: Matrix) {

    def equalsTo(other: Matrix): Boolean = equals(matrix, other)

    private def equals(m: Matrix, n: Matrix): Boolean = {
      m.row == n.row && m.col == n.col && doubleArrayEquals(m.flatten, n.flatten)
    }

    private def doubleArrayEquals(data: Array[Double], other: Array[Double]): Boolean = {
      if (data.length == other.length) {
        val index = (0 until data.length).find(i => data(i) - other(i) > 0.000000001D || data(i) - other(i) < -0.000000001D)
        index match {
          case None => true
          case _ => false
        }
      } else {
        false
      }
    }
  }
}

