package me.yingrui.segment.word2vec

trait Word2VecNetwork {
  def size: Int
  def wordsCount: Int
  def wordVector: Array[Array[Double]]
  def learn(input: Array[Int], output: Array[(Int, Int)], alpha: Double): Unit
  def clearError: Unit
  def getLoss: Double
}




