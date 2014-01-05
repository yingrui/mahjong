package websiteschema.mpsegment.hmm

import websiteschema.mpsegment.util.ISerialize
import websiteschema.mpsegment.util.SerializeHandler

class Emission extends ISerialize {

  //observe -> states
  private val matrix: java.util.Map[Int, java.util.Map[Int, Double]] = new java.util.HashMap[Int, java.util.Map[Int, Double]]()
  private var total = 0

  def getProb(s: Int, o: Int): Double = {
    val emission = matrix.get(o);
    if (emission!= null) {
      if (emission.containsKey(s)) {
        return emission.get(s)
      }
    }
    return 1.0D / total.toDouble
  }

  def getStateProbByObserve(observe: Int): java.util.Collection[Int] = {
    val map = matrix.get(observe)
    return if (null != map) map.keySet() else null
  }

  def setProb(s: Int, o: Int, prob: Double) {
    var map = matrix.get(o)
    if (null == map) {
      map = new java.util.HashMap[Int, Double]()
      matrix.put(o, map)
    }
    map.put(s, prob)
  }

  override def save(writeHandler: SerializeHandler) {
    writeHandler.serializeInt(total)
    val size = if (null != matrix) matrix.size else 0
    writeHandler.serializeInt(size)
    val keys = new java.util.ArrayList[Int](matrix.keySet())
    var i = 0;
    while (i < keys.size())
    {
      val key = keys.get(i)
      writeHandler.serializeInt(key)
      val row = matrix.get(key)
      writeHandler.serializeMapIntDouble(row)
      i += 1
    }
  }

  override def load(readHandler: SerializeHandler) {
    total = readHandler.deserializeInt()
    val size = readHandler.deserializeInt()
    for (i <- 0 until size)
    {
      val key = readHandler.deserializeInt()
      val row = readHandler.deserializeMapIntDouble()
      matrix.put(key, row)
    }
  }
}

object Emission {

  def apply(emisMatrix: collection.Map[Int, collection.Map[Int, Int]]) = {
    val emission = new Emission()
    for (state <- emisMatrix.keys) {
      val mapO = emisMatrix(state)
      var sum = 1
      val observes = mapO.keys
      for (o <- observes) {
        sum += mapO(o)
      }

      for (o <- observes) {
        val prob = mapO(o).toDouble / sum.toDouble
        emission.setProb(state, o, prob)
      }
      emission.total += sum
    }
    emission
  }
}
