package me.yingrui.segment.word2vec

import java.io._
import java.lang.Math.sqrt

import scala.collection.mutable.ListBuffer

class WordReader(val input: Reader, val window: Int = 5) {
  private val reader = new BufferedReader(input)
  private var readedWords = List[String]()
  private val random = new scala.util.Random()

  def read(): String = readWord().toLowerCase().trim

  def readWindow(): Seq[String] = {
    if(readedWords.isEmpty) {
      for(i <- 0 until window) readedWords :+= ""
      for(i <- 0 to window) readedWords :+= read()
    } else {
      val word = read()
      readedWords = readedWords.tail :+ word
    }

    if(readedWords(window).isEmpty) {
      List[String]()
    } else {
      readedWords
    }
  }

  def readWindow(words: List[String], index: Int): Seq[String] = {
    var result = List[String]()
    if (index < window) {
      result = (0 until window - index).map(i => "").toList
    }
    val end = index + window + 1
    result ++= words.slice(index - window, if(end > words.size) words.size else end)
    if(result.size < 2 * window + 1) {
      result ++= (0 until 2 * window + 1 - result.size).map(i => "").toList
    }
    result
  }

  def readWordListAndRandomlyDiscardFrequentWords(length: Int, start: Long, maxCount: Long, sample: Double, vocab: Vocabulary): (Long, List[String]) = {
    val result = new ListBuffer[String]()
    var word = readWord()
    var count = start + 1
    while(!word.isEmpty && result.size < length && count <= maxCount) {
      val wordIndex = vocab.getIndex(word)
      if (wordIndex > 0) {
        val wordCount = vocab.getCount(word).toDouble
        val ran = (sqrt(wordCount / (sample * vocab.getTotalWordCount.toDouble)) + 1D) * (sample * vocab.getTotalWordCount.toDouble) / wordCount
        if(ran > random.nextDouble()) {
          result += word
        }
      }

      word = readWord()
      count += 1
    }
    (count - start, result.toList)
  }

  private def readWord(): String = {
    val word = new StringBuilder
    var ch = reader.read()
    while(ch != -1) {
      if(ch != ' ') {
        word.append(ch.asInstanceOf[Char])
      } else if(!word.isEmpty) {
        return word.toString()
      }
      ch = reader.read()
    }
    word.toString()
  }

}
