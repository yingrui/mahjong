package websiteschema.mpsegment.hmm

import junit.framework.Assert
import org.junit.Test

class ViterbiTest {

  @Test
  def should_return_status_333332_with_giving_observes_THTHTH() {
    Trie.setTreeNodeSorter(new TrieNodeBinarySort())
    val viterbi = initTestData

    val o = List("T", "H", "T", "H", "T", "H")
    try {
      val s = viterbi.calculateWithLog(o)
      val sb = new StringBuilder()
      for (state <- s) {
        print(state.getName() + " ")
        sb.append(state.getName()).append(" ")
      }
      assert(sb.toString().trim().equals("three three three three three two"))
    } catch {
      case ex: Throwable =>
        Assert.fail(ex.getMessage())
    }
  }

  @Test
  def should_handle_unknown_State() {
    val o = List("A", "H")
    val viterbi = initTestData

    try {
      val s = viterbi.calculateWithLog(o)
      val sb = new StringBuilder()
      for (state <- s) {
        print(state.getName() + " ")
        sb.append(state.getName()).append(" ")
      }
      Assert.fail("should throw ObserveListException.")
    } catch {
      case ex: Throwable =>
        println(ex.getMessage())
    }

  }

  def initTestData = {
    val v = new HmmViterbi
    val stateBank: NodeRepository = new NodeRepository()
    stateBank.add(Node("one"))
    stateBank.add(Node("two"))
    stateBank.add(Node("three"))
    v.setStateBank(stateBank)

    val observeBank: NodeRepository = v.getObserveBank()
    observeBank.add(Node("H"))
    observeBank.add(Node("T"))
    v.setObserveBank(observeBank)

    //transition
    //   s1  s2  s3
    //s1 0.8 0.1 0.1
    //s2 0.1 0.8 0.1
    //s3 0.1 0.1 0.8
    val tran = Transition()
    tran.setStateBank(stateBank)
    tran.setProb(stateBank.get("one").getIndex(), stateBank.get("one").getIndex(), 0.3)
    tran.setProb(stateBank.get("one").getIndex(), stateBank.get("two").getIndex(), 0.3)
    tran.setProb(stateBank.get("one").getIndex(), stateBank.get("three").getIndex(), 0.4)
    tran.setProb(stateBank.get("two").getIndex(), stateBank.get("one").getIndex(), 0.2)
    tran.setProb(stateBank.get("two").getIndex(), stateBank.get("two").getIndex(), 0.6)
    tran.setProb(stateBank.get("two").getIndex(), stateBank.get("three").getIndex(), 0.2)
    tran.setProb(stateBank.get("three").getIndex(), stateBank.get("one").getIndex(), 0.2)
    tran.setProb(stateBank.get("three").getIndex(), stateBank.get("two").getIndex(), 0.2)
    tran.setProb(stateBank.get("three").getIndex(), stateBank.get("three").getIndex(), 0.6)
    tran.getRoot().printTreeNode("")
    v.setTran(tran)
    //emission
    //   o1  o2
    //s1 0.5 0.5
    //s2 0.8 0.2
    //s3 0.2 0.8
    val e: Emission = v.getE()
    e.setProb(stateBank.get("one").getIndex(), observeBank.get("H").getIndex(), 0.5)
    e.setProb(stateBank.get("one").getIndex(), observeBank.get("T").getIndex(), 0.5)
    e.setProb(stateBank.get("two").getIndex(), observeBank.get("H").getIndex(), 0.8)
    e.setProb(stateBank.get("two").getIndex(), observeBank.get("T").getIndex(), 0.2)
    e.setProb(stateBank.get("three").getIndex(), observeBank.get("H").getIndex(), 0.2)
    e.setProb(stateBank.get("three").getIndex(), observeBank.get("T").getIndex(), 0.8)
    v.setE(e)

    //Pi = [0.2 0.3 0.5]
    val pi: Pi = v.getPi()
    pi.setPi(stateBank.get("one").getIndex(), 0.2)
    pi.setPi(stateBank.get("two").getIndex(), 0.4)
    pi.setPi(stateBank.get("three").getIndex(), 0.4)
    v.setPi(pi)

    v
  }
}
