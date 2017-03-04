package me.yingrui.segment.dict

class HeadIndexer {

  private def getWordOccuredSum(): Int = wordOccuredSum

  private def getWordCount(): Int = wordCount

  def getHeadStr(): String = headStr

  def getMaxWordLength(): Int = maxWordLength

  def add(word: IWord) {
    val wordName = word.getWordName()
    if (get(wordName) == null) {
      if (wordName.startsWith(headStr)) {
        addWord(wordName, word)
      } else {
        throw new DictionaryException("Head string is not start with " + headStr)
      }
    }
  }

  private def addWord(wordName: String, word: IWord) {
    updateWordLengthStatus(wordName.length)
    if (wordName.length() > getMaxWordLength()) {
      maxWordLength = wordName.length()
    }
    wordCount += 1
    wordOccuredSum += 1
    //NOTE: 5711 HeadIndexers have words less than 64, which total number is 51426,
    //      232 HeadIndexers have words more than 64, which total number is 27261
    if (wordCount > HeadIndexer.Word_Array_Size_Threshold && wordArray.isInstanceOf[BinaryWordArray]) {
      val tmpWordArray = HashWordArray(wordArray.getWordItems())
      wordArray = tmpWordArray
    }
    wordArray.add(word)
  }


  override def toString(): String = {
    val stringBuilder = new StringBuilder()
    stringBuilder.append((new StringBuilder("首字:")).append(getHeadStr()).append("(不同词数量:").append(getWordCount()).toString())
    stringBuilder.append((new StringBuilder(",最长词长度:")).append(getMaxWordLength()).toString())
    stringBuilder.append((new StringBuilder(",所有词的全部出现次数:")).append(getWordOccuredSum()).append(")\n").toString())
    val wordItems = wordArray.getWordItems()
    for (i <- 0 until wordCount) {
      val word = wordItems(i)
      stringBuilder.append(word.toString())
    }

    return stringBuilder.toString()
  }

  def get(word: String): IWord = {
    return wordArray.find(word)
  }

  def findMultiWord(wordStr: String): Array[IWord] = {
    if (wordStr.length() == 1) {
      if (wordStr.equals(headWord.getWordName())) {
        val words = new Array[IWord](1)
        words(0) = headWord
        return words
      } else {
        return null
      }
    }
    var maxWordLen = getMaxWordLength()
    if (wordStr.length() < maxWordLen) {
      maxWordLen = wordStr.length()
    }
    val array = findWords(wordStr, maxWordLen, 3)

    if (array.size > 0) array else null
  }

  private def findWords(wordStr: String, maxWordLen: Int, maxWordCount: Int): Array[IWord] = {
    var array = Array[IWord]()
    var i = 1
    var foundCount = 0
    while (i < maxWordLen && foundCount < maxWordCount) {
      val length = i + 1
      if (this.containsWordWhichLengthEqual(length)) {
        val candidateWord = wordStr.substring(0, length)
        val word = wordArray.find(candidateWord)
        if (null != word) {
          array = Array(word) ++ array
          foundCount += 1
        }
      }
      i += 1
    }
    array
  }

  def findWord(wordStr: String): IWord = {
    if (wordStr.length() == 1) {
      if (wordStr.equals(headStr)) {
        return headWord
      } else {
        return null
      }
    }
    var maxWordLen = getMaxWordLength()
    if (wordStr.length() < maxWordLen) {
      maxWordLen = wordStr.length()
    }
    val array = findWords(wordStr, maxWordLen, 1)

    if (array.size > 0) array(0) else null
  }

  def getWordArray(): IWordArray = wordArray

  private def updateWordLengthStatus(wordLength: Int): Unit = {
    wordLengthStatus = 1 << (wordLength - 1) | wordLengthStatus
  }

  private def containsWordWhichLengthEqual(length: Int): Boolean = {
    val status = 1 << (length - 1) & wordLengthStatus
    status > 0
  }

  private var headStr: String = null
  private var maxWordLength: Int = 0
  private var wordOccuredSum: Int = 0
  private var wordCount: Int = 0
  private var headWord: IWord = null
  private var wordArray: IWordArray = null
  private var wordLengthStatus: Long = 1
}

object HeadIndexer {
  private val Word_Array_Size_Threshold = 32

  def apply(headWord: IWord) = create(headWord, 1)

  def apply(headWord: IWord, headLength: Int) = create(headWord, headLength)

  private def create(headWord: IWord, headLength: Int) = {
    val indexer = new HeadIndexer
    indexer.headStr = headWord.getWordName().substring(0, headLength)
    indexer.headWord = headWord
    indexer.wordCount = 1
    indexer.wordOccuredSum = 1
    indexer.maxWordLength = headWord.getWordLength()
    indexer.updateWordLengthStatus(headWord.getWordLength())
    indexer.wordArray = BinaryWordArray(List[IWord](headWord).toArray)
    indexer
  }
}
