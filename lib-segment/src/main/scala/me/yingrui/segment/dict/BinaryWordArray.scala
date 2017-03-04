package me.yingrui.segment.dict

import scala.collection.mutable

object BinaryWordArray {

  val ordering: Ordering[IWord] = Ordering[String].on[IWord](_.getWordName())

  def apply(wordItems: Array[IWord]) = {
    val wordArray = new BinaryWordArray()
    wordArray.wordSet = new mutable.TreeSet[IWord]()(ordering)
    wordArray.wordSet ++= wordItems
    wordArray
  }
}

class BinaryWordArray extends AbstractWordArray {

  var wordSet = new mutable.TreeSet[IWord]()(BinaryWordArray.ordering)

  override def find(word: String): IWord =
    wordSet.find(w => w.getWordName().equals(word)) match {
      case Some(w) => w
      case _ => null
    }

  override def addWord(word: IWord) {
    wordSet.add(word)
  }

  override def getWordItems(): Array[IWord] = wordSet.toArray
}