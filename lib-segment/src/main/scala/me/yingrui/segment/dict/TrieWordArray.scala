package me.yingrui.segment.dict

class TrieWordArray extends IWordArray {

  val step = 100
  var allWords = new Array[IWord](step)
  var top = 0
  val root = new TrieNode('\0', -1)

  override def add(word: IWord) {
    checkWordArray
    allWords(top) = word
    root.insert(word.getWordName(), top)
    top += 1
  }

  override def getWordItems(): Array[IWord] = allWords.filter(_ != null)

  override def find(wordStr: String): IWord = {
    val word = root.search(wordStr)
    if (word != null && word.index >= 0) allWords(word.index) else null
  }

  private def checkWordArray {
    if (top >= allWords.length) {
      val temp = allWords
      allWords = new Array[IWord](temp.length + step)
      System.arraycopy(temp, 0, allWords, 0, temp.length)
    }
  }
}

object TrieWordArray {

  def apply(wordItems: Array[IWord]): TrieWordArray = {
    val wordArray = new TrieWordArray()
    wordItems.foreach(wordArray.add(_))
    wordArray
  }

}