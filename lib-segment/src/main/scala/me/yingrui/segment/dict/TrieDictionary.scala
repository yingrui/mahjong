package me.yingrui.segment.dict

class TrieDictionary extends IDictionary {

  val step = 100
  var allWords = new Array[IWord](step)
  var top = 0
  val root = new TrieNode('\0', -1)

  def addWord(word: IWord) {
    checkWordArray
    allWords(top) = word
    root.insert(word.getWordName(), top)
    top += 1
  }

  private def checkWordArray {
    if (top >= allWords.length) {
      val temp = allWords
      allWords = new Array[IWord](temp.length + step)
      System.arraycopy(temp, 0, allWords, 0, temp.length)
    }
  }

  override def getWord(wordStr: String): IWord = {
    val word = root.search(wordStr)
    if (word != null && word.index >= 0) allWords(word.index) else null
  }

  override def getWords(wordStr: String): Array[IWord] = {
    getWordList(wordStr).toArray
  }

  def getWordList(wordStr: String): List[IWord] = {
    val word = getWord(wordStr)
    if (null != word) {
      if (wordStr.length > 1)
        word :: getWordList(wordStr.substring(0, wordStr.length - 1))
      else
        List[IWord](word)
    } else {
      if (wordStr.length > 1)
        getWordList(wordStr.substring(0, wordStr.length - 1))
      else
        List[IWord]()
    }
  }

  override def iterator(): List[IWord] = {
    allWords.toList.filter(_ != null)
  }
}