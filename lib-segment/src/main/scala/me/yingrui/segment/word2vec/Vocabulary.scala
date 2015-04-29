package me.yingrui.segment.word2vec

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

  def add(word: String): Unit = {
    totalWordCount += 1
    if (!wordIndexMap.containsKey(word)) {
      wordIndexMap.put(word, wordCount.size)
      wordCount += 1
    } else {
      val wordIndex = wordIndexMap.get(word)
      wordCount(wordIndex) += 1
    }
  }

  def getCount(word: String): Int = if (wordIndexMap.containsKey(word)) wordCount(wordIndexMap.get(word)) else 0

  def getIndex(word: String): Int = wordIndexMap.get(word)

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
