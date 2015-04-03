package me.yingrui.segment.math

import java.util

import org.nd4j.linalg.api.ndarray.INDArray

class NDMatrix(val data: INDArray) extends Matrix {

  val row = data.rows()
  val col = data.columns()

  def row(i: Int): Matrix = new NDMatrix(data.getRow(i))
  def col(i: Int): Matrix = new NDMatrix(data.getColumn(i))

  def apply(i: Int, j: Int): Double = data.data().getDouble(data.offset() + j * row + i)
  def update(i: Int, j: Int, value: Double) = data.data().put(data.offset() + j * row + i, value)

  def flatten: Array[Double] = {
    if (data.data().length() == row * col)
      data.data().asDouble()
    else
      (for (i <- 0 until row) yield {
        for (j <- 0 until col) yield data.getDouble(i, j)
      }).flatten.toArray
  }

  def sum: Double = flatten.sum

  override def toString() = data.toString

  def +(n: Double): NDMatrix = new NDMatrix(data.add(n))

  def +(m: Matrix): Matrix = new NDMatrix(data.add(m.asInstanceOf[NDMatrix].data))

  def +=(m: Matrix): Unit = data.addi(m.asInstanceOf[NDMatrix].data)

  def -(n: Double): Matrix = new NDMatrix(data.sub(n))

  def -(m: Matrix): Matrix = new NDMatrix(data.sub(m.asInstanceOf[NDMatrix].data))

  def -=(m: Matrix): Unit = data.subi(m.asInstanceOf[NDMatrix].data)

  def x(n: Double): Matrix = throw new UnsupportedOperationException
  def x(m: Matrix): Matrix = throw new UnsupportedOperationException
  def %(m: Matrix): Matrix = throw new UnsupportedOperationException
  def *=(n: Double): Unit = throw new UnsupportedOperationException

  def *(m: Matrix): Double = throw new UnsupportedOperationException

  def /(n: Double): Matrix = throw new UnsupportedOperationException

  def T: Matrix = throw new UnsupportedOperationException

  def isVector: Boolean = throw new UnsupportedOperationException

  def clear: Unit = throw new UnsupportedOperationException

  def :=(other: Matrix): Unit = throw new UnsupportedOperationException
  def :=(other: Array[Double]): Unit = throw new UnsupportedOperationException

}
