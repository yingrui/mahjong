package websiteschema.mpsegment.core

import websiteschema.mpsegment.dict.IWord
import websiteschema.mpsegment.graph.IGraph
import websiteschema.mpsegment.graph.Path
import websiteschema.mpsegment.hmm._
import websiteschema.mpsegment.util.SerializeHandler
import collection.mutable.ListBuffer

class POSRecognizer extends IPOSRecognizer {

  private var viterbi: HmmViterbi = null
  private var graph: IGraph = null
  private var path: Path = null
  private var posFreq: Array[Int] = null
  private var observeList = ListBuffer[String]()

  viterbi = new HmmViterbi()
  Trie.setTreeNodeSorter(new TrieNodeQuickSort())
  load()

  private def initViterbiWithEmissionAndObserve() {
    observeList.clear()
    viterbi.setE(Emission())
    viterbi.setObserveBank(new NodeRepository())
    for (i <- 0 until path.getLength())
    {
      val word = getWordIndexAt(i)
      initViterbiWith(word)
    }
  }

  private def initViterbiWith(word: IWord) {
    val wordName = word.getWordName()
    val observe = Node(wordName)
    observeList += (wordName)
    viterbi.getObserveBank().add(observe)
    val posArray = word.getPOSArray()
    val posTable = posArray.getWordPOSTable()
    for (i <- 0 until posTable.length)
    {
      val pos = posTable(i)(0)
      val freq = posTable(i)(1)
      viterbi.getE().setProb(pos, observe.getIndex(), getEmissionProb(pos, freq))
    }
  }

  private def getEmissionProb(pos: Int, freq: Int): Double = {
    val tagFreq = getTagFreqs(pos)
    if (tagFreq > 0) {
      return (freq + 1).toDouble / tagFreq.toDouble
    } else {
      return 0.00001D
    }
  }

  private def getTagFreqs(i: Int): Int = {
    return posFreq(i)
  }

  private def getWordIndexAt(index: Int): IWord = {
    return graph.getEdgeObject(path.iget(index), path.iget(index + 1))
  }

  override def findPOS(p: Path, graph: IGraph): Array[Int] = {
    this.path = p
    this.graph = graph
    initViterbiWithEmissionAndObserve()
    try {
      val stateList = viterbi.calculateWithLog(observeList.toList)
      val posArray = new Array[Int](stateList.size)
      var i = 0
      for (state <- stateList) {
        posArray(i) = state.getIndex()
        i += 1
      }
      return posArray
    } catch {
      case ex: Throwable =>
      throw new RuntimeException("Could not find POS array." + ex.getMessage(), ex)
    }
  }

  def save(writeHandler: SerializeHandler) {
    POSResources.save(writeHandler)
  }

  def load() {
    posFreq = POSResources.getPosFreq()
    viterbi.setTran(POSResources.getTransition())
    viterbi.setPi(POSResources.getPi())
    viterbi.setStateBank(POSResources.getStateBank())
  }
}
