package me.yingrui.segment.word2vec

import java.io.File
import java.lang.Math.sqrt

import me.yingrui.segment.math.Matrix
import me.yingrui.segment.util.SerializeHandler

object Word2VecDemo extends App {

  val reader = SerializeHandler(new File("vectors.dat"), SerializeHandler.READ_ONLY)

  val vocab = Vocabulary(reader)
  val matrix: Matrix = reader.deserializeMatrix()

  println(s"Vocabulary has ${vocab.size} words and total word count is ${vocab.getTotalWordCount}")
  println(s"matrix shape(${matrix.row}, ${matrix.col})")

  val E = Matrix(matrix.col, 1).map(x => 1D)
  val A = matrix.map(ele => ele * ele) x E

  def findSimWords(vector: Matrix, B: Double): List[(String, Double)] = {
    var result = List[(String, Double)]()
    for (j <- 1 until vector.row) {
      val cosine = vector(j, 0) / (sqrt(A(j, 0)) * sqrt(B))
      result :+= (vocab.getWord(j), cosine)
    }
    result.sortBy(t => t._2).reverse.take(5)
  }

  for (i <- 1 to vocab.size) {
    val vector = matrix.row(i)
    val result = matrix x vector.T
    assert(result.row == matrix.row && result.col == 1)
    println(s"${vocab.getWord(i)} : " + findSimWords(result, A(i, 0)))
  }
}
