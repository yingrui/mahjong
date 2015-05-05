package me.yingrui.segment.word2vec

import java.io.{InputStreamReader, BufferedReader, File}
import java.lang.Math.sqrt

import me.yingrui.segment.math.Matrix
import me.yingrui.segment.util.SerializeHandler

object Word2VecDemo extends App {

  val reader = SerializeHandler(new File("vectors.dat"), SerializeHandler.READ_ONLY)

  val vocab = Vocabulary(reader)
  val matrix: Matrix = Matrix(reader.deserialize2DArrayDouble())

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

  val inputReader = new BufferedReader(new InputStreamReader(System.in))
  var line = inputReader.readLine()
  while (line != null && !line.equals("quit")) {
    val wordIndex = vocab.getIndex(line.trim)
    if(wordIndex >= 0) {
      val vector = matrix.row(wordIndex)
      val result = matrix x vector.T
      assert(result.row == matrix.row && result.col == 1)
      println(s"${vocab.getWord(wordIndex)} : " + findSimWords(result, A(wordIndex, 0)))
    }
    line = inputReader.readLine()
  }

  def displayAll() {
    for (i <- 1 to vocab.size) {
      val vector = matrix.row(i)
      val result = matrix x vector.T
      assert(result.row == matrix.row && result.col == 1)
      println(s"${vocab.getWord(i)} : " + findSimWords(result, A(i, 0)))
    }
  }

//  displayAll()
}
