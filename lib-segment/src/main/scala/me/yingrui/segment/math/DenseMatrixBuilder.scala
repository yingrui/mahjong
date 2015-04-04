package me.yingrui.segment.math

import scala.util.Random

class DenseMatrixBuilder extends MatrixBuilder {

  override def vector(data: Seq[Double]): Matrix = new DenseMatrix(1, data.length, data.toArray)

  override def apply(data: Array[Double]): Matrix = vector(data)

  override def apply(data: Seq[Double]): Matrix = vector(data)

  override def apply(row: Int, col: Int): Matrix = new DenseMatrix(row, col, new Array[Double](row * col))

  override def apply(size: Int, identity: Boolean): Matrix = {
    val m = new DenseMatrix(size, size, new Array[Double](size * size))
    if (identity) {
      0 until size foreach ((i: Int) => { m(i, i) = 1D })
    }
    m
  }

  override def apply(data: Array[Array[Double]]): Matrix = new DenseMatrix(data.length, data(0).length, data.flatten.toArray)

  override def apply(row: Int, col: Int, data: Array[Double]): Matrix = new DenseMatrix(row, col, data)

  override def applyBoolean(row: Int, col: Int, data: Array[Boolean]): Matrix = new DenseMatrix(row, col, data.map(b => if (b) 1D else -1D))

  override def randomize(row: Int, col: Int, min: Double, max: Double) = {
    val data = new Array[Double](row * col)
    for (i <- 0 until data.length) {
      data(i) = (Math.random() * (max - min)) + min
    }
    apply(row, col, data)
  }

  override def randomize(row: Int, col: Int): Matrix = {
    val data = (0 until row * col).map(i => 1e-5 * Random.nextInt(100).toDouble)
    apply(row, col, data.toArray)
  }

}
