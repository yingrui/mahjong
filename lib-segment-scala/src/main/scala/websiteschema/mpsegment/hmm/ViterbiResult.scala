package websiteschema.mpsegment.hmm

class ViterbiResult(val length: Int) {

  var states: Array[Array[Int]] = new Array[Array[Int]](length)
  var delta: Array[Array[Double]] = new Array[Array[Double]](length)
  var psai: Array[Array[Int]] = new Array[Array[Int]](length)

  def getMaxProbPathId = {
    var pos = 0
    var maxProb = Double.NegativeInfinity
    for (j <- 0 until delta(length - 1).length) {
      val p = delta(length - 1)(j)
      if (p > maxProb) {
        maxProb = p
        pos = j
      }
    }
    pos
  }

  def getStatePath(end: Int, len: Int, position: Int): Array[Int] = {
    val maxDepth = if (end + 1 > len) len else end + 1
    val ret = new Array[Int](maxDepth)
    var pos = position
    for (i <- 0 until maxDepth) {
      val state = states(end - i)(pos)
      pos = psai(end - i)(pos)
      ret(ret.length - i - 1) = state
    }
    ret
  }
}
