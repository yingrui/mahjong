package websiteschema.mpsegment.hmm

import java.util.Properties

object Flag {

  private val n = 3
  private val p = new Properties()
  val labda = new Array[Double](n)
  private var ngramMinimumShowTimes = 1

  try {
    p.load(getClass.getClassLoader().getResourceAsStream("pinyin.properties"))
  } catch {
    case ex: Throwable =>
      println(ex.getMessage())
  }


  labda(0) = 1.0 / 6.0
  labda(1) = 1.0 / 3.0
  labda(2) = 1.0 / 2.0
  ngramMinimumShowTimes = p.getProperty("NgramMinimumShowTimes", "1").toInt

  def getNgramMinimumShowTimes(): Int = {
    return ngramMinimumShowTimes
  }
}
