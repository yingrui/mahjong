package me.yingrui.segment.word2vec

import java.io._
import java.lang.Math.sqrt
import java.lang.System.currentTimeMillis

import scala.collection.mutable.ListBuffer

class WordIndexReader(val inputFile: String, val vocab: Vocabulary, val window: Int = 5) {
  private val reader = new DataInputStream(new FileInputStream(inputFile))
  private val random = new scala.util.Random(currentTimeMillis())

  private def read(): Int = readWord()

  def close(): Unit = reader.close();

  def readWindow(words: List[Int], index: Int): List[Int] = {
    var result = List[Int]()
    if (index < window) {
      result = (0 until window - index).map(i => 0).toList
    }
    val end = index + window + 1
    result ++= words.slice(index - window, if (end > words.size) words.size else end)
    if (result.size < 2 * window + 1) {
      result ++= (0 until 2 * window + 1 - result.size).map(i => 0).toList
    }
    result
  }

  def readWordListAndRandomlyDiscardFrequentWords(length: Int, sample: Double): (Long, List[Int]) = {
    assert(length > 0)
    val result = new ListBuffer[Int]()
    var word = readWord()
    var count = 0L
    while (word > 0 && result.size < length) {
      count += 1
      val wordCount = vocab.getCountByIndex(word).toDouble
      val ran = (sqrt(wordCount / (sample * vocab.getTotalWordCount.toDouble)) + 1D) * (sample * vocab.getTotalWordCount.toDouble) / wordCount
      val randomFilter = random.nextDouble()
      if (ran > randomFilter) {
        result += word
      }

      word = readWord()
    }
    (count, result.toList)
  }

  private def readWord(): Int =
    try {
      reader.readInt()
    } catch {
      case eof: EOFException => -1
    }

}
