package me.yingrui.segment.word2vec

object Word2VecUtil {

  private val EXP_TABLE_SIZE = 1000D
  private val MAX_EXP = 6D
  private val EXP_TABLE: Seq[Double] = for(i <- 0 until EXP_TABLE_SIZE.toInt) yield {
    val exp = Math.exp((i.toDouble / EXP_TABLE_SIZE * 2D - 1D) * MAX_EXP)
    exp / (exp + 1D)
  }

  def simplifiedSigmoid(x: Double): Double = {
    val index = ((x + MAX_EXP) * (EXP_TABLE_SIZE / MAX_EXP / 2)).toInt
    if(index >= EXP_TABLE_SIZE)
      1D
    else if(index < 0)
      0D
    else
      EXP_TABLE(index)
  }

}
