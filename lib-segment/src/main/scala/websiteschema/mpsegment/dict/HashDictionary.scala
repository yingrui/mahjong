package websiteschema.mpsegment.dict

class HashDictionary extends IDictionary {

  def getCapacity(): Int = {
    return headIndexersHashMap.size
  }

  def clear() {
    headIndexersHashMap = new java.util.HashMap[String, HeadIndexer]()
  }

  def lookupHeadIndexer(head: String): HeadIndexer = {
    return headIndexersHashMap.get(head)
  }

  override def getWord(wordStr: String): IWord = {
    val headIndexer = lookupHeadIndexer(getHead(wordStr))
    if (headIndexer != null) headIndexer.findWord(wordStr) else null
  }

  def lookupWord(wordStr: String): IWord = {
    val headIndexer = lookupHeadIndexer(getHead(wordStr))
    if (headIndexer != null) headIndexer.get(wordStr) else null
  }

  override def getWords(sentenceStr: String): Array[IWord] = {
    val headIndexer = lookupHeadIndexer(getHead(sentenceStr))
    if (headIndexer != null)
      headIndexer.findMultiWord(sentenceStr)
    else
      null
  }

  override def iterator(): List[IWord] = {
    var wordList = List[IWord]()
    for (i <- 0 until headIndexers.size) {
      val headIndexer = headIndexers(i)
      val wordArray = headIndexer.getWordArray()
      wordList = wordList ++ wordArray.getWordItems().toList
    }
    return wordList
  }

  def getHead(wordStr: String): String = {
    return wordStr.substring(0, headLength)
  }

  def addWord(word: IWord) {
    var headIndexer = lookupHeadIndexer(getHead(word.getWordName()))
    if (null == headIndexer) {
      headIndexer = createHeadIndexer(word)
    }
    headIndexer.add(word)
  }

  private def createHeadIndexer(word: IWord): HeadIndexer = {
    val headIndexer = HeadIndexer(word, headLength)
    headIndexers = headIndexers ++ List(headIndexer)
    headIndexersHashMap.put(headIndexer.getHeadStr(), headIndexer)
    return headIndexer
  }

  def setHeadLength(headLength: Int) {
    this.headLength = headLength
  }

  private var headLength: Int = 1
  private var headIndexersHashMap = new java.util.HashMap[String, HeadIndexer]()
  private var headIndexers = List[HeadIndexer]()
}
