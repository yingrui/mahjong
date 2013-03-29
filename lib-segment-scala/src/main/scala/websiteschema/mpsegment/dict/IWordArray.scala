package websiteschema.mpsegment.dict

import java.util.Arrays
import java.util.Comparator
import scala.collection.mutable.Map
import scala.collection.mutable.OpenHashMap

trait IWordArray {

  def find(word: String): IWord

  def getWordItems(): Array[IWord]

  def add(word: IWord)
}

object BinaryWordArray {

  def apply(wordItems: Array[IWord]) = {
    val wordArray = new BinaryWordArray()
    wordArray.wordItems = wordItems
    wordArray
  }
}

class BinaryWordArray extends IWordArray {

  var wordItems: Array[IWord] = null

  override def find(word: String): IWord = {
    val index = lookupWordItem(word)
    if (index >= 0) {
      return wordItems(index)
    }
    return null
  }

  override def add(word: IWord) {
    val temp = new Array[IWord](wordItems.length + 1)
    System.arraycopy(wordItems, 0, temp, 0, wordItems.length)
    temp(wordItems.length) = word
    Arrays.sort(temp, new Comparator[IWord]() {
      override def compare(o1: IWord, o2: IWord): Int = {
        return o1.getWordName().compareTo(o2.getWordName())
      }
    })
    wordItems = temp
  }

  private def lookupWordItem(word: String): Int = {
    var left = 0
    var right = wordItems.length - 1
    while (left <= right) {
      val mid = (left + right) / 2
      val comp = wordItems(mid).getWordName().compareTo(word)
      if (comp == 0) {
        return mid
      }
      if (comp < 0) {
        left = mid + 1
      } else {
        right = mid - 1
      }
    }

    return -1
  }

  override def getWordItems(): Array[IWord] = {
    return wordItems
  }
}

object HashWordArray {
  def apply(words: Array[IWord]) = {
    val wordArray = new HashWordArray()
    wordArray.wordIndex = OpenHashMap[String, Int]()
    var i = 0
    for (word <- words) {
      wordArray.wordIndex += (word.getWordName() -> i)
      i += 1
    }

    wordArray.wordItems = words
    wordArray
  }
}

class HashWordArray extends IWordArray {

  var wordIndex: Map[String, Int] = null
  var wordItems: Array[IWord] = null

  override def find(word: String): IWord = {
    wordIndex.get(word) match {
      case Some(i) => wordItems(i)
      case _ => null
    }
  }

  override def getWordItems(): Array[IWord] = {
    return wordItems
  }

  override def add(word: IWord) {
    val temp = new Array[IWord](wordItems.length + 1)
    System.arraycopy(wordItems, 0, temp, 0, wordItems.length)
    val index = wordItems.length
    temp(index) = word
    wordIndex += (word.getWordName() -> index)
    wordItems = temp
  }
}

