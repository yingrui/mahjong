package me.yingrui.segment.dict

trait IWordArray {

  def find(word: String): IWord

  /**
    * Find multiple words from input string
    * @param wordStr
    * @param maxWordLen
    * @param maxWordCount
    * @return found words, sorted by word length in order desc
    */
  def findWords(wordStr: String, maxWordLen: Int, maxWordCount: Int): Array[IWord]

  def getWordItems(): Array[IWord]

  def add(word: IWord): Unit
}

abstract class AbstractWordArray extends IWordArray {

  override def add(word: IWord): Unit = {
    updateWordLengthStatus(word.getWordName().length)
    addWord(word)
  }

  def addWord(word: IWord): Unit

  override def findWords(wordStr: String, maxWordLen: Int, maxWordCount: Int): Array[IWord] = {
    var array = Array[IWord]()
    var i = 1
    var foundCount = 0
    while (i < maxWordLen && foundCount < maxWordCount) {
      val length = i + 1
      if (this.containsWordWhichLengthEqual(length)) {
        val candidateWord = wordStr.substring(0, length)
        val word = this.find(candidateWord)
        if (null != word) {
          array = Array(word) ++ array
          foundCount += 1
        }
      }
      i += 1
    }
    array
  }


  private def containsWordWhichLengthEqual(length: Int): Boolean = {
    val status = (1L << (length - 1)) & wordLengthStatus
    status > 0
  }

  private var wordLengthStatus: Long = 1L
  private def updateWordLengthStatus(wordLength: Int): Unit = {
    wordLengthStatus = (1L << (wordLength - 1)) | wordLengthStatus
  }

}