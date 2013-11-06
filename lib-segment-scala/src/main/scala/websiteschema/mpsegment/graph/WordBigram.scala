package websiteschema.mpsegment.graph

import websiteschema.mpsegment.hmm.NodeRepository
import websiteschema.mpsegment.hmm.Trie
import websiteschema.mpsegment.util.SerializeHandler
import collection.immutable.HashMap
import java.io._

class WordBigram {

  private var trie: Trie = new Trie()
  private var nodeRepository: NodeRepository = new NodeRepository()

  private def load(resource: String) {
    val inputStream = getInputStream(resource)
    val reader = SerializeHandler(new DataInputStream(inputStream))
    nodeRepository.load(reader)
    trie.load(reader)
    trie.buildIndex(1)
  }

  def getProbability(word1: String, word2: String): Double = {
    val firstWord = nodeRepository.get(getWordFirstTwoChars(word1))
    val secondWord = nodeRepository.get(getWordFirstTwoChars(word2))
    if (firstWord == null || secondWord == null) {
      return -1
    }
    val bigram = Array[Int](
      firstWord.getIndex(),
      secondWord.getIndex()
    )
    val node = trie.searchNode(bigram)
    if(null != node) node.getProb() else -1.0D
  }

  private def getInputStream(resource: String): InputStream = {
    var inputStream: InputStream = null
    val file = new File(resource)
    if (file.exists()) {
      inputStream = new FileInputStream(file)
    } else {
      inputStream = getClass().getClassLoader().getResourceAsStream(resource)
    }
    return inputStream
  }

  private def getWordFirstTwoChars(word: String): String = {
    return if(word.length() > 2) word.substring(word.length() - 2) else word
  }
}

object WordBigram {
  private var mapWordBigram = HashMap[String, WordBigram]()

  def apply(corpusName: String) = {
    createWordBigram(corpusName)
  }

  def apply(trie: Trie, nodeRepository: NodeRepository) = {
    val bigram: WordBigram = new WordBigram()
    bigram.trie = trie
    bigram.nodeRepository = nodeRepository
    bigram
  }

  private def createWordBigram(corpusName: String): WordBigram = {
    if (!mapWordBigram.contains(corpusName)) {
      val instance = new WordBigram()
      instance.load(corpusName)
      mapWordBigram += (corpusName -> instance)
      instance
    } else {
      mapWordBigram(corpusName)
    }
  }
}
