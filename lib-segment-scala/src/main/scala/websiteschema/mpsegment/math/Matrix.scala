package websiteschema.mpsegment.math

trait Matrix {
  def +(n: Double): Matrix

  def +(m: Matrix): Matrix

  def +=(m: Matrix): Unit

  def -(n: Double): Matrix

  def -(m: Matrix): Matrix

  def x(n: Double): Matrix
  def *=(n: Double): Unit

  def x(m: Matrix): Matrix

  def *(m: Matrix): Double

  def /(n: Double): Matrix

  def T: Matrix

  def flatten: Array[Double]

  def row(i: Int): Matrix

  def col(i: Int): Matrix

  val row: Int
  val col: Int

  def isVector: Boolean

  def clear: Unit

  def apply(i: Int, j: Int): Double

  def update(i: Int, j: Int, value: Double)

  def :=(other: Matrix): Unit
  def :=(other: Array[Double]): Unit
}

object Matrix {



  def vector(d: Double*): Matrix = new DenseMatrix(1, d.length, d.toArray)

  def apply(row: Int, col: Int): Matrix = new DenseMatrix(row, col, new Array[Double](row * col))

  def apply(size: Int, identity: Boolean = false): Matrix = {
    val m = new DenseMatrix(size, size, new Array[Double](size * size))
    if (identity) {
      0 until size foreach ((i: Int) => { m(i, i) = 1D })
    }
    m
  }

  def apply(data: Array[Double]): Matrix = new DenseMatrix(1, data.length, data)

  def apply(data: Array[Array[Double]]): Matrix = new DenseMatrix(data.length, data(0).length, data.flatten.toArray)

  def apply(data: Seq[Double]): Matrix = new DenseMatrix(1, data.length, data.toArray)

  def apply(row: Int, col: Int, data: Array[Double]): Matrix = new DenseMatrix(row, col, data)

  def apply(row: Int, col: Int, data: Array[Boolean]): Matrix = new DenseMatrix(row, col, data.map(b => if (b) 1D else -1D))

  def arithmetic(data: Array[Double], other: Array[Double], op: (Double, Double) => Double): Array[Double] =
    (for (i <- 0 until data.length) yield op(data(i), other(i))).toArray

  def map(m: Matrix, compute: (Double) => Double): Matrix = apply(m.row, m.col, m.flatten.map(compute))

  def ramdomize(row: Int, col: Int, min: Double, max: Double) = {
    val data = new Array[Double](row * col)
    for (i <- 0 until data.length) {
        data(i) = (Math.random() * (max - min)) + min
    }
    apply(row, col, data)
  }

  def doubleArrayEquals(data: Array[Double], other: Array[Double]): Boolean = {
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

