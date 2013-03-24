package websiteschema.mpsegment.hmm

import websiteschema.mpsegment.util.ISerialize
import websiteschema.mpsegment.util.SerializeHandler

class Transition extends ISerialize {

  var root: Trie = null

  def getRoot(): Trie = root

  var stateBank: NodeRepository = null
  var sortor: TrieNodeSortor = null

  def setStateBank(stateBank: NodeRepository) {
    this.stateBank = stateBank
  }

  def setSortor(sortor: TrieNodeSortor) {
    Trie.setTreeNodeSorter(sortor)
  }

  def setProb(s1: Int, s2: Int, prob: Double) {
    val ngram = List[Int](s1, s2)
    val node = root.insert(ngram.toArray)
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

  def getCoProb(c: Array[Int], s: Int): Double = {
    val ngram = new Array[Int](c.length + 1)
    System.arraycopy(c, 0, ngram, 0, c.length)
    ngram(c.length) = s

    return getProb(ngram, ngram.length)
  }

  def getProb(s1: Int, s2: Int): Double = {
    val ngram = new Array[Int](2)
    ngram(0) = s1
    ngram(1) = s2
    return getProb(ngram, 2)
  }

  def getProb(ngram: Array[Int], n: Int): Double = {
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
