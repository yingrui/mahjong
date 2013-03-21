package websiteschema.mpsegment.hmm

import websiteschema.mpsegment.util.ISerialize
import websiteschema.mpsegment.util.SerializeHandler
import collection.mutable.Map
import collection.mutable.HashMap

class Emission extends ISerialize {

  //observe -> states
  private val matrix = HashMap[Int, Map[Int, Double]]();
  private var total = 0;

  def getProb(s: Int, o: Int): Double = {
    val e = matrix.getOrElse(o, null)
    if (null != e) {
      if (e.contains(s)) {
        return e(s);
      }
    }

    return 1.0D / total.toDouble;
  }

  def getStateProbByObserve(observe: Int): Iterable[Int] = {
    val map = matrix.getOrElse(observe, null)
    return if (null != map) map.keys else null;
  }

  def setProb(s: Int, o: Int, prob: Double) {
    var map = matrix.getOrElse(o, null)
    if (null == map) {
      map = HashMap[Int, Double]();
    }
    map += (s -> prob)
    matrix += (o -> map)
  }

  override def save(writeHandler: SerializeHandler) {
    writeHandler.serializeInt(total);
    val size = if (null != matrix) matrix.size else 0
    writeHandler.serializeInt(size);
    for (key <- matrix.keys)
    {
      writeHandler.serializeInt(key);
      val row = matrix(key)
      writeHandler.serializeMapIntDouble(row);
    }
  }

  override def load(readHandler: SerializeHandler) {
    total = readHandler.deserializeInt();
    val size = readHandler.deserializeInt()
    for (i <- 0 until size)
    {
      val key = readHandler.deserializeInt()
      val row = readHandler.deserializeMapIntDouble()
      matrix += (key -> row)
    }
  }
}

object Emission {
  def apply(emisMatrix: Map[Int, Map[Int, Int]]) = {
    val emission = new Emission();
    for (state <- emisMatrix.keys) {
      val mapO = emisMatrix(state)
      var sum = 1
      val observes = mapO.keys
      for (o <- observes) {
        sum += mapO(o);
      }

      for (o <- observes) {
        val prob = mapO(o).toDouble / sum.toDouble
        emission.setProb(state, o, prob);
      }
      emission.total += sum
    }
    emission
  }
}
