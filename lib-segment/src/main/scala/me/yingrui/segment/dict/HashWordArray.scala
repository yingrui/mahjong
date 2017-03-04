package me.yingrui.segment.dict

import scala.collection.mutable.Map
import scala.collection.mutable.OpenHashMap

object HashWordArray {
  def apply(words: Array[IWord]) = {
    val wordArray = new HashWordArray()
    wordArray.wordIndex = OpenHashMap[String, IWord]()
    for (word <- words) {
      wordArray.wordIndex += (word.getWordName() -> word)
    }

    wordArray
  }
}

class HashWordArray extends AbstractWordArray {

  var wordIndex: Map[String, IWord] = null
  var wordItems: Array[IWord] = null

  override def find(word: String): IWord = {
    wordIndex.get(word) match {
      case Some(w) => w
      case _ => null
    }
  }

  override def getWordItems(): Array[IWord] = {
    return wordIndex.toList.map(_._2).toArray
  }

  override def addWord(word: IWord) {
    wordIndex += (word.getWordName() -> word)
  }
}