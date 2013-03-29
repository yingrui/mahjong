package websiteschema.mpsegment.hmm

import websiteschema.mpsegment.util.ISerialize
import websiteschema.mpsegment.util.SerializeHandler
import collection.mutable.HashMap
import collection.mutable.Map

class Pi extends ISerialize {

    private var pi: Map[Int, Double] = HashMap[Int,Double]()
    private var total = 1

    private def init(pii: Map[Int,Int]) {
        val keySet = pii.keys
        for (i <- keySet) {
            total += pii(i)
        }

        for (i <- keySet) {
            setPi(i, pii(i).toDouble / total.toDouble)
        }
    }

    def getPi(index: Int) : Double =
      pi.get(index) match {
        case Some(p) => p
        case _ => 1.0D / total.toDouble
      }

    def setPi(index: Int, prob: Double) {
        pi += (index -> prob)
    }

    override def save(writeHandler: SerializeHandler) {
        writeHandler.serializeInt(total)
        writeHandler.serializeMapIntDouble(pi)
    }

    override def load(readHandler: SerializeHandler) {
        total = readHandler.deserializeInt()
        pi = readHandler.deserializeMapIntDouble()
    }
}

object Pi {

  def apply() = new Pi()

  def apply(pii: Map[Int, Int]) = {
    val pi = new Pi()
    pi.init(pii)
    pi
  }

}
