package websiteschema.mpsegment.hmm

import websiteschema.mpsegment.util.ISerialize
import websiteschema.mpsegment.util.SerializeHandler

class Transition extends ITransition with ISerialize {

  var root: Trie = null

  def getRoot(): Trie = root

  var stateBank: NodeRepository = null

  def setStateBank(stateBank: NodeRepository) {
    this.stateBank = stateBank
  }

  def setProb(s1: Int, s2: Int, prob: Double) {
    val ngram = Array[Int](s1, s2)
    val node = root.insert(ngram)
    node.setProb(prob)
  }

  private def getProb(ngram: Array[Int]): Double = {
    var ret: Double = 0.0D

    val node = root.searchNode(ngram.toArray)
    if (null != node) {
      ret = node.getProb()
    } else {
      ret = 1.0 / root.getCount().toDouble
    }

    return ret
  }

  def getConditionProb(condition: Array[Int], state: Int): Double = {
    val ngram = new Array[Int](condition.length + 1)
    System.arraycopy(condition, 0, ngram, 0, condition.length)
    ngram(condition.length) = state

    return getProb(ngram, ngram.length)
  }

  def getConditionProb(condition: Int, state: Int): Double = {
    return getProb(Array[Int](condition, state), 2)
  }

  private def getProb(ngram: Array[Int], n: Int): Double = {
    var ret = 0.00000001D

    //bigram
    if (2 == n) {
      return getProb(ngram)
    }
    var i = n
    while (i > 0) {
      val igram = new Array[Int](i)
      for (j <- 1 to i) {
        igram(i - j) = ngram(n - j)
      }
      ret += Flag.labda(i - 1) * getProb(igram)
      i -= 1
    }

    return ret
  }

  override def save(writeHandler: SerializeHandler) {
    root.save(writeHandler)
    stateBank.save(writeHandler)
  }

  override def load(readHandler: SerializeHandler) {
    root.load(readHandler)
    stateBank.load(readHandler)
  }
}

object Transition {
  def apply() = {
    val transition = new Transition()
    transition.stateBank = new NodeRepository()
    transition.root = new Trie()
    transition
  }

  def apply(root: Trie, stateBank: NodeRepository) = {
    val transition = new Transition()
    transition.stateBank = stateBank
    transition.root = root
    transition
  }
}
