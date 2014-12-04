package websiteschema.mpsegment.tools

import websiteschema.mpsegment.core.SegmentResult
import websiteschema.mpsegment.hmm.Node
import websiteschema.mpsegment.hmm.NodeRepository
import websiteschema.mpsegment.hmm.Trie
import websiteschema.mpsegment.util.CharCheckUtil
import websiteschema.mpsegment.util.SerializeHandler

import java.io._

object WordBigramBuilder extends App {
  var folder = "src/test/resources"
  if (args.length > 0) {
    folder = args(0)
  } else {
    println("prepare to train a model with PFR corpus.")
  }

  val bigram = new WordBigramBuilder()
  val dir = new File(folder)
  if (dir.isDirectory() && dir.exists()) {
    for (f <- dir.listFiles()) {
      val filename = f.getName()
      if (filename.endsWith(".txt") && filename.startsWith("PFR-")) {
        bigram.train(f.getAbsolutePath())
      }
    }
  }
  bigram.build()

  bigram.save("word-bigram.dat")
}

class WordBigramBuilder {

  private val trie: Trie = new Trie()
  private val nodeRepository: NodeRepository = new NodeRepository()

  def build() {
    trie.buildIndex(1)
  }

  def save(filename: String) {
    val writer = SerializeHandler(new File(filename), SerializeHandler.WRITE_ONLY)
    nodeRepository.save(writer)
    trie.save(writer)
  }

  def train(filename: String) {
    val file = new File(filename)
    if (file.exists()) {
      println("[WordBigramBuilder] train file " + file.getAbsolutePath())
      val corpusLoader = PFRCorpusLoader(new FileInputStream(file))
      var segmentResult = corpusLoader.readLine()
      while (null != segmentResult) {
        analysis(segmentResult)
        segmentResult = corpusLoader.readLine()
      }
    }
  }

  def getTrie(): Trie = {
    return trie
  }

  def getNodeRepository(): NodeRepository = {
    return nodeRepository
  }

  private def analysis(segmentResult: SegmentResult) {
    for (i <- 0 until segmentResult.length() - 1)
    {
      val word1 = segmentResult.getWord(i)
      val word2 = segmentResult.getWord(i + 1)
      statisticBigram(word1, word2)
    }
  }

  private def statisticBigram(word1: String, word2: String) {
    if (CharCheckUtil.isChinese(word1) && CharCheckUtil.isChinese(word2)) {
      var wordNode1 = Node(getWordFirstTwoChars(word1))
      var wordNode2 = Node(getWordFirstTwoChars(word2))
      wordNode1 = nodeRepository.add(wordNode1)
      wordNode2 = nodeRepository.add(wordNode2)
      trie.insert(Array[Int] (
        wordNode1.getIndex(), wordNode2.getIndex()
      ))
    }
  }

  private def getWordFirstTwoChars(word: String): String = {
    return if(word.length() > 2) word.substring(word.length() - 2) else word
  }
}
