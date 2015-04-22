package me.yingrui.segment.math

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j._

class NDMatrix(val data: INDArray) extends Matrix {

  val row = data.rows()
  val col = data.columns()

  def row(i: Int): Matrix = new NDMatrix(data.getRow(i))
  def col(i: Int): Matrix = new NDMatrix(data.getColumn(i))

  def apply(i: Int, j: Int): Double = data.data().getDouble(data.offset() + j * row + i)
  def update(i: Int, j: Int, value: Double) = data.data().put(data.offset() + j * row + i, value)

  def flatten: Array[Double] = {
    if (isDataSizeMatchShape)
      data.data().asDouble()
    else
      (for (i <- 0 until row) yield {
        for (j <- 0 until col) yield data.getDouble(i, j)
      }).flatten.toArray
  }

  private def isDataSizeMatchShape: Boolean = data.data().length() == row * col

  def sum: Double = flatten.sum

  override def toString() = data.toString

  def +(n: Double): NDMatrix = new NDMatrix(data.add(n))

  def +(m: Matrix): Matrix = new NDMatrix(data.add(m.asInstanceOf[NDMatrix].data))

  def +=(m: Matrix): Unit = data.addi(m.asInstanceOf[NDMatrix].data)

  def -(n: Double): Matrix = new NDMatrix(data.sub(n))

  def -(m: Matrix): Matrix = new NDMatrix(data.sub(m.asInstanceOf[NDMatrix].data))

  def -=(m: Matrix): Unit = data.subi(m.asInstanceOf[NDMatrix].data)

  def x(n: Double): Matrix = new NDMatrix(data.mul(n))
  def x(m: Matrix): Matrix = new NDMatrix(data.mmul(m.asInstanceOf[NDMatrix].data))
  def %(m: Matrix): Matrix = new NDMatrix(data.mul(m.asInstanceOf[NDMatrix].data))
  def *=(n: Double): Unit = data.muli(n)

  def *(m: Matrix): Double = this.%(m).sum

  def /(n: Double): Matrix = new NDMatrix(data.div(n))

  def T: Matrix = new NDMatrix(
    if(isDataSizeMatchShape)
      data.transpose()
    else
      create(this.flatten, Array(row, col)).transpose()
  )

  def isVector: Boolean = data.isRowVector

  def isColumnVector: Boolean = data.isColumnVector

  def clear: Unit = data.assign(0D)

  def :=(other: Matrix): Unit = data.assign(other.asInstanceOf[NDMatrix].data)

  def map(compute: (Double) => Double): Matrix = {
    val array = data.data().asDouble().map(compute)
    new NDMatrix(create(array, data.shape(), data.stride(), data.offset()))
  }

}
