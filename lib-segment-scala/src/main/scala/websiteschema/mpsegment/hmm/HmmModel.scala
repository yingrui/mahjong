package websiteschema.mpsegment.hmm

import websiteschema.mpsegment.util.SerializeHandler

import collection.mutable.Map
import java.io.{DataInputStream, InputStream, File}
import websiteschema.mpsegment.util.FileUtil._
import io.Source

class HmmModel {

  private val viterbi = new ViterbiImpl()
  private val stateBank = new NodeRepository()
  private val observeBank = new NodeRepository()
  private val ngram = new Trie()
  private var pi = Pi()
  private var emission: Emission = new Emission()

  def containsObserve(ch: String): Boolean = {
    return observeBank.get(ch) != null
  }

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
      buildViterbi()
      handler.close()
    } catch {
      case ex: Throwable =>
        ex.printStackTrace()
    }
  }

  def loadDictionary(dictFile : String) {
    val source = Source.fromInputStream(getResourceAsStream(dictFile))
    for (line <- source.getLines()) {
      val regex = "([^ ]+) *= *([^ ]+)".r
      regex findFirstIn line match {
        case Some(regex(han, pinyin)) => add(han, pinyin)
        case None =>
      }
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
    val handler = SerializeHandler(new File(file), SerializeHandler.MODE_WRITE_ONLY)
    observeBank.save(handler)
    stateBank.save(handler)
    pi.save(handler)
    emission.save(handler)
    ngram.save(handler)
    handler.close()
  }


  def getViterbi = viterbi

  def getNgram = ngram

  def getStateBank = stateBank

  def getObserveBank = observeBank

  def buildPi(pii: Map[Int, Int]) {
    pi = Pi(pii)
  }

  def buildEmission(emisMatrix: Map[Int, Map[Int, Int]]) {
    emission = Emission(emisMatrix)
  }

  def buildViterbi() {
    viterbi.setObserveBank(observeBank)
    viterbi.setStateBank(stateBank)
    viterbi.setPi(pi)
    val tran = Transition(ngram, stateBank)
    viterbi.setTran(tran)
    viterbi.setE(emission)
  }
}