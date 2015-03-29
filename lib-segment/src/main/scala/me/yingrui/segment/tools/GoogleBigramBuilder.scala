package me.yingrui.segment.tools

import me.yingrui.segment.hmm.Node
import me.yingrui.segment.hmm.NodeRepository
import me.yingrui.segment.hmm.Trie
import me.yingrui.segment.util.CharCheckUtil
import me.yingrui.segment.util.SerializeHandler

import java.io._
import java.util.zip.GZIPInputStream

class GoogleBigramBuilder extends App {

  val trie = new Trie()
  val nodeRepository = new NodeRepository()

  val directory = if (args.length > 0) args(0) else "/Users/twer/workspace/nlp/google-ngram/2gram"
  val dir = new File(directory)
  if (dir.isDirectory()) {
    scanDirectory(dir)
    save("google-bigram.dat")
  } else {
    System.exit(1)
  }

  def scanDirectory(dir: File) {
    val files = dir.listFiles()
    for (file <- files) {
      println(file.getName())
      if (file.getName().startsWith("googlebooks-chi-sim-all-2gram-")) {
        loadFile(file)
      }
    }
  }

  def loadFile(file: File) {
    val reader = getBufferedReader(file)
    var line = reader.readLine()
    while (line != null) {
      parseLine(line)
      line = reader.readLine()
    }
  }

  def parseLine(line: String) {
    val elements = line.split("\\s+")
    if (elements.length == 5) {
      val word1 = elements(0)
      val word2 = elements(1)
      val year = elements(2).toInt
      val count = elements(3).toInt
      val documentCount = elements(4).toInt
      statisticBigram(word1, word2, count)
    }
  }

  def statisticBigram(word1: String, word2: String, count: Int) {
    if (CharCheckUtil.isChinese(word1) && CharCheckUtil.isChinese(word2)) {
      var wordNode1 = Node(getWordFirstTwoChars(word1))
      var wordNode2 = Node(getWordFirstTwoChars(word2))
      wordNode1 = nodeRepository.add(wordNode1)
      wordNode2 = nodeRepository.add(wordNode2)
      trie.insert(Array[Int](wordNode1.getIndex(), wordNode2.getIndex()), count)
    }
  }

  def getWordFirstTwoChars(word: String): String = {
    if (word.length() > 2) word.substring(word.length() - 2) else word
  }

  def getBufferedReader(file: File): BufferedReader = {
    val inputStream = getInputStream(file)
    new BufferedReader(new InputStreamReader(inputStream, "utf-8"))
  }

  def getInputStream(file: File): InputStream = {
    if (file.getName().endsWith(".gz")) {
      return new GZIPInputStream(new FileInputStream(file))
    }
    return new FileInputStream(file); //To change body of created methods use File | Settings | File Templates.
  }

  def save(filename: String) {
    val writer = SerializeHandler(new File(filename), SerializeHandler.WRITE_ONLY)
    nodeRepository.save(writer)
    trie.save(writer)
  }
}
