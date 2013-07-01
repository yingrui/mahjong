package websiteschema.mpsegment.pinyin

import websiteschema.mpsegment.hmm._
import websiteschema.mpsegment.util.SerializeHandler

import collection.mutable.Map
import java.io.{DataInputStream, InputStream, FileInputStream, File}
import websiteschema.mpsegment.util.FileUtil._
import io.Source

class WordToPinyinModel {

  private val viterbi = new Viterbi()
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
        case Some(regex(han, pinyin)) => addPinyin(han, pinyin)
        case None =>
      }
    }
  }


  def addPinyin(han: String, pinyin: String) {
    val observe = observeBank.add(Node(han))
    val state = stateBank.get(pinyin)
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


  def getViterbi(): Viterbi = {
    return viterbi
  }

  def getNgram(): Trie = {
    return ngram
  }

  def getStateBank(): NodeRepository = {
    return stateBank
  }

  def getObserveBank(): NodeRepository = {
    return observeBank
  }

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
    var tran = Transition(ngram, stateBank)
    viterbi.setTran(tran)
    viterbi.setE(emission)
  }
}