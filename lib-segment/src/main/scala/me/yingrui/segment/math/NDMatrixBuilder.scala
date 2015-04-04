package me.yingrui.segment.math

import org.nd4j.linalg.factory.Nd4j._

import scala.util.Random

class NDMatrixBuilder extends MatrixBuilder {

  override def vector(data: Seq[Double]): Matrix = new NDMatrix(create(data.toArray))

  override def apply(data: Seq[Double]): Matrix = vector(data)

  override def apply(data: Array[Double]): Matrix = vector(data)

  override def apply(row: Int, col: Int): Matrix = new NDMatrix(zeros(row, col))

  override def apply(size: Int, identity: Boolean): Matrix = if(identity) new NDMatrix(eye(size)) else apply(size, size)

  override def apply(data: Array[Array[Double]]): Matrix = new NDMatrix(create(data))

  override def apply(row: Int, col: Int, data: Array[Double]): Matrix = new NDMatrix(create(data, Array(row, col)))

  override def applyBoolean(row: Int, col: Int, data: Array[Boolean]): Matrix = apply(row, col, data.map(b => if(b) 1D else -1D))

  override def randomize(row: Int, col: Int, min: Double, max: Double) = {
    val data = new Array[Double](row * col)
    for (i <- 0 until data.length) {
      data(i) = (Math.random() * (max - min)) + min
    }
    apply(row, col, data)
  }

  override def randomize(row: Int, col: Int): Matrix = {
    val data = (0 until row * col).map(i => 1e-5 * Random.nextInt(100).toDouble)
    new DenseMatrix(row, col, data.toArray)
  }

}
