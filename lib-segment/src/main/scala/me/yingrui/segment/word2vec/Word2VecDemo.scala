package me.yingrui.segment.word2vec

import java.io.{InputStreamReader, BufferedReader, File}
import java.lang.Math.sqrt

import me.yingrui.segment.math.Matrix
import me.yingrui.segment.util.SerializeHandler

object Word2VecDemo extends App {

  println("loading...")
  val reader = SerializeHandler(new File("vectors.dat"), SerializeHandler.READ_ONLY)

  val vocab = Vocabulary(reader)
  val matrix: Matrix = Matrix(reader.deserialize2DArrayDouble())

  println(s"Vocabulary has ${vocab.size} words and total word count is ${vocab.getTotalWordCount}")
  println(s"matrix shape(${matrix.row}, ${matrix.col})")

  val E = Matrix(matrix.col, 1).map(x => 1D)
  val A = (matrix.map(ele => ele * ele) x E).map(ele => sqrt(ele))

  def findSimWords(vector: Matrix, B: Double): Seq[(String, String)] = {
    val cosines = vector / A / B

    val result = (1 until vector.row).map(j => (j, cosines(j, 0)))

    result.sortBy(t => t._2).reverse.tail.take(50).map(t => (vocab.getWord(t._1), "%3.3f".format(t._2)))
  }

  println("please input word to find its similar words (type QUIT to break)")
  val inputReader = new BufferedReader(new InputStreamReader(System.in))
  var line = inputReader.readLine()
  while (line != null && !line.equals("QUIT")) {
    if(line.contains(" ")) {
      val words = line.split(" ")
      computeSimilarity(words(0), words(1))
    } else {
      displaySimilarWords(line)
    }
    line = inputReader.readLine()
  }

  def displaySimilarWords(word: String) {
    val wordIndex = vocab.getIndex(word.trim)
    if (wordIndex >= 0) {
      val vector = Matrix(matrix.row(wordIndex).flatten)
      val result = matrix x vector.T
      assert(result.row == matrix.row && result.col == 1)
      println(s"${vocab.getWord(wordIndex)} : " + findSimWords(result, A(wordIndex, 0)))
    }
  }

  def computeSimilarity(word1: String, word2: String) {
    val wordIndex1 = vocab.getIndex(word1.trim)
    val wordIndex2 = vocab.getIndex(word2.trim)
    if (wordIndex1 > 0 & wordIndex2 > 0) {
      val vector1 = Matrix(matrix.row(wordIndex1).flatten)
      val vector2 = Matrix(matrix.row(wordIndex2).flatten)
      val result = (vector1 * vector2) / A(wordIndex1, 0) / A(wordIndex2, 0)
      println(s"similarity : $result")
    }
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
