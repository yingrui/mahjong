package me.yingrui.segment.word2vec

import java.io.{FileInputStream, InputStreamReader}
import java.util
import scala.collection.mutable

import me.yingrui.segment.util.SerializeHandler

class Vocabulary(private val wordIndexMap: util.Map[String, Int],
                 private val wordCount: mutable.Buffer[Int],
                 private var totalWordCount: Long) {

  def getTotalWordCount = this.totalWordCount

  def size = wordCount.size

  def getWord(index: Int): String = {
    for(key <- wordIndexMap.keySet().toArray(new Array[String](0))) {
      if (wordIndexMap.get(key) == index) {
        return key
      }
    }
    ""
  }

  def add(word: String): Unit = add(word, 1)

  private def add(word: String, count: Int): Unit = {
    totalWordCount += count
    if (!wordIndexMap.containsKey(word)) {
      wordIndexMap.put(word, wordCount.size)
      wordCount += count
    } else {
      val wordIndex = wordIndexMap.get(word)
      wordCount(wordIndex) += count
    }
  }

  def getCount(word: String): Int = if (wordIndexMap.containsKey(word)) wordCount(wordIndexMap.get(word)) else 0
  def getCountByIndex(index: Int) = wordCount(index)

  def getIndex(word: String): Int = wordIndexMap.get(word)

  def rebuild(minCount: Int): Unit = {
    val temp = Vocabulary.apply()

    val words: Array[String] = wordIndexMap.keySet().toArray(new Array[String](0))
    (for(word <- words; count = getCount(word); if count >= minCount) yield {
      (count, word)
    }).sortBy(t => t._1).reverse.foreach(t => temp.add(t._2, t._1))

    this.wordIndexMap.clear()
    this.wordIndexMap.putAll(temp.wordIndexMap)
    this.wordCount.clear()
    this.wordCount.appendAll(temp.wordCount)
    this.totalWordCount = temp.totalWordCount
  }

}

object Vocabulary {

  def apply(): Vocabulary = new Vocabulary(new util.HashMap[String, Int](), List(0).toBuffer, 0L)

  def apply(reader: WordReader): Vocabulary = {
    val vocab = apply()
    var word = reader.read()
    while (!word.isEmpty) {
      vocab.add(word)
      word = reader.read()
    }
    vocab
  }

  def apply(file: String): Vocabulary = {
    val reader = new InputStreamReader(new FileInputStream(file))
    val wordReader = new WordReader(reader)
    val vocab = apply(wordReader)
    reader.close()
    vocab
  }

  def apply(reader: SerializeHandler): Vocabulary = {
    val wordIndexMap = reader.deserializeMapStringInt()
    val wordCount = reader.deserializeArrayInt().toBuffer
    val totalWordCount = reader.deserializeLong()
    new Vocabulary(wordIndexMap, wordCount, totalWordCount)
  }

  implicit class RichVocabulary(vocab: Vocabulary) {

    def save(writer: SerializeHandler): Unit = {
      writer.serializeMapStringInt(vocab.wordIndexMap)
      writer.serializeArrayInt(vocab.wordCount.toArray)
      writer.serializeLong(vocab.totalWordCount)
    }
  }
}
