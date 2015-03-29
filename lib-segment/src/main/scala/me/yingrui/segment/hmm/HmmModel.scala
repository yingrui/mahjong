package me.yingrui.segment.hmm

import me.yingrui.segment.util.SerializeHandler

import collection.mutable.Map
import java.io.{DataInputStream, InputStream, File}
import me.yingrui.segment.util.FileUtil._
import io.Source

class HmmModel() {

  private val viterbi = new HmmViterbi
  private val stateBank = new NodeRepository()
  private val observeBank = new NodeRepository()
  private val ngram = new Trie()
  private var pi = Pi()
  private var emission = Emission()

  def containsObserve(observed: String) = observeBank.get(observed) != null

  def load(filename: String) {
    load(getResourceAsStream(filename))
  }

  def load(is: InputStream) {
    try {
      val handler = SerializeHandler(new DataInputStream(is))
      observeBank.load(handler)
      stateBank.load(handler)
      pi.load(handler)
      emission.load(handler)
      ngram.load(handler)
      handler.close()
    } catch {
      case ex: Throwable =>
        ex.printStackTrace()
    }
  }

  def add(observeStr: String, stateStr: String) {
    val observe = observeBank.add(Node(observeStr))
    val state = stateBank.get(stateStr)
    if (state != null) {
      emission.setProb(state.getIndex, observe.getIndex, 0.5D)
    }
  }

  def save(file: String) {
    val handler = SerializeHandler(new File(file), SerializeHandler.WRITE_ONLY)
    observeBank.save(handler)
    stateBank.save(handler)
    pi.save(handler)
    emission.save(handler)
    ngram.save(handler)
    handler.close()
  }


  def getViterbi = viterbi

  def getEmission = emission

  def getNgram = ngram

  def getStateBank = stateBank

  def getObserveBank = observeBank

  def buildPi(pii: Map[Int, Int]) {
    pi = Pi(pii)
  }

  def buildEmission(emisMatrix: Map[Int, Map[Int, Int]]) {
    emission = Emission(emisMatrix)
  }

  def buildViterbi: Unit = {
    buildViterbi(emission)
  }

  def buildViterbi(emission: Emission) {
    viterbi.setObserveBank(observeBank)
    viterbi.setStateBank(stateBank)
    viterbi.setPi(pi)
    val tran = Transition(ngram, stateBank)
    viterbi.setTran(tran)
    viterbi.setE(emission)
  }
}