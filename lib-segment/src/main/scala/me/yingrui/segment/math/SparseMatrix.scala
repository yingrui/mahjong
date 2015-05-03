package me.yingrui.segment.math

import me.yingrui.segment.graph.SparseVector

import scala.collection.mutable.Map

class SparseMatrix(val row: Int, val col: Int, val rowOrder: Boolean = true) {



  private val vectors = Map[Int, SparseVector]()

  def apply(i: Int, j: Int): Double = vectors.get(i) match {
    case Some(v) => v(j)
    case _ => 0D
  }

  def update(i: Int, j: Int, value: Double): Unit = {
    vectors.get(i) match {
      case Some(v) => v.update(j, value)
      case _ => {
        val vector = new SparseVector(col)
        vector.update(j, value)
        vectors += (i -> vector)
      }
    }
  }

  private def addVector(i: Int, vector: SparseVector): Unit = {
    vectors += (i -> vector)
  }

  def row(i: Int): SparseMatrix = {
    val rowVector = new SparseMatrix(1, col)
    vectors.get(i) match {
      case Some(v) => rowVector.addVector(0, v)
      case _ =>
    }
    rowVector
  }

  def col(i: Int): SparseMatrix = {
    val rowVector = new SparseMatrix(1, col)
    vectors.get(i) match {
      case Some(v) => rowVector.addVector(0, v)
      case _ =>
    }
    rowVector
  }
}

class SparseVector(val size: Int) {
  val data = Map[Int, Double]()

  def apply(i: Int) = data.get(i) match {
    case Some(value) => value
    case _ => 0D
  }

  def update(i: Int, value: Double): Unit = {
    data += (i -> value)
  }

  private def get(i: Int) = data.get(i)
}

object SparseMatrix {

  def apply(row: Int, col: Int, data: Array[Double]): SparseMatrix = {
    val m = new SparseMatrix(row, col)
    for (i <- 0 until row; j <- 0 until col) {
      m(i, j) = data(i * col + j)
    }
    m
  }

}