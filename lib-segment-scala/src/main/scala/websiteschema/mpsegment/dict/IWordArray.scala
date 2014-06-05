package websiteschema.mpsegment.dict

import scala.collection.mutable.Map
import scala.collection.mutable.OpenHashMap
import scala.collection.mutable

trait IWordArray {

  def find(word: String): IWord

  def getWordItems(): Array[IWord]

  def add(word: IWord)
}

object BinaryWordArray {

  val ordering: Ordering[IWord] = Ordering[String].on[IWord](_.getWordName())

  def apply(wordItems: Array[IWord]) = {
    val wordArray = new BinaryWordArray()
    wordArray.wordSet = new mutable.TreeSet[IWord]()(ordering)
    wordArray.wordSet ++= wordItems
    wordArray
  }
}

class BinaryWordArray extends IWordArray {

  var wordSet = new mutable.TreeSet[IWord]()(BinaryWordArray.ordering)

  override def find(word: String): IWord =
    wordSet.find(w => w.getWordName().equals(word)) match {
      case Some(w) => w
      case _ => null
    }

  override def add(word: IWord) {
    wordSet.add(word)
  }

  override def getWordItems(): Array[IWord] = wordSet.toArray
}

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

class HashWordArray extends IWordArray {

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

  override def add(word: IWord) {
    wordIndex += (word.getWordName() -> word)
  }
}

