package me.yingrui.segment.math

trait MatrixBuilder {

  def vector(data: Seq[Double]): Matrix

  def apply(row: Int, col: Int): Matrix

  def apply(size: Int, identity: Boolean): Matrix

  def apply(data: Array[Double]): Matrix

  def apply(data: Array[Array[Double]]): Matrix

  def apply(data: Seq[Double]): Matrix

  def apply(row: Int, col: Int, data: Array[Double]): Matrix

  def applyBoolean(row: Int, col: Int, data: Array[Boolean]): Matrix

  def randomize(row: Int, col: Int, min: Double, max: Double): Matrix

  def randomize(row: Int, col: Int): Matrix
}
