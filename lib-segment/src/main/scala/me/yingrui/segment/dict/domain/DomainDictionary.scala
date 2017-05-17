package me.yingrui.segment.dict.domain

import me.yingrui.segment.dict._

import collection.mutable

class DomainDictionary extends IDictionary {
  private val hashDictionary = new HashDictionary()
  hashDictionary.setHeadLength(2)
  private val wordNameIndexHashMap = mutable.HashMap[String, Int]()
  private var arrayWordItem = List[DomainWordItem]()
  private val synonymIndexHashMap = mutable.HashMap[Int, Int]()
  private val synonymHashMap = mutable.HashMap[Int, List[Int]]()
  private var maxWordLength: Int = 0

  override def getWord(wordName: String): IWord = {
    val words = hashDictionary.getWords(wordName)
    if (words != null && !words.isEmpty) words.head else null
  }

  override def getWords(wordStr: String): Array[IWord] = {
    return hashDictionary.getWords(wordStr)
  }

  override def iterator(): List[IWord] = {
    return arrayWordItem
  }

  override def addWord(word: IWord) {
    addWord(word.getWordName(),
      POSUtil.getPOSString(word.getWordPOSTable()(0)(0)),
      word.getWordPOSTable()(0)(1), word.getDomainType())
  }

  override def lookupWord(wordName: String): IWord = {
    return hashDictionary.lookupWord(wordName)
  }

  def pushWord(wordName: String, synonym: String, pos: String, freq: Int, domainType: Int) {
    if (wordName == null || wordName.trim().equals("") || pos == null || pos.trim().equals("") || wordName.trim().length() < 2) {
      System.err.println("Load a error word '" + wordName + "'into domain dictionary, already ignored.")
      return
    }

    val word = lookupWord(wordName)
    var index = -1
    if (null != word) {
      word.setOccuredCount(pos, freq)
      word.setDomainType(domainType)
      index = getWordIndex(wordName)
    } else {
      index = addWord(wordName, pos, freq, domainType)
    }
    if (null != synonym && !synonym.isEmpty()) {
      val synonymIndex = getWordIndex(synonym)
      addSynonym(index, synonymIndex)
    }
  }

  def getSynonymSet(wordName: String): List[IWord] = {
    val index = getWordIndex(wordName)
    if (index >= 0) {
      val synonymIndex = synonymIndexHashMap.getOrElse(index, -1)
      val synonymSet: List[Int] =
        if (synonymIndex >= 0) synonymHashMap(synonymIndex) else synonymHashMap(index)
      if (null != synonymSet) {
        val head = getWord(if (synonymIndex >= 0) synonymIndex else index)
        return head :: synonymSet.map(i => getWord(i))
      }
    }
    return null
  }

  private def getWordIndex(wordName: String): Int = wordNameIndexHashMap.getOrElse(wordName, -1)

  private def getWord(wordIndex: Int): IWord = {
    return if (wordIndex >= 0) arrayWordItem(wordIndex) else null
  }

  private def addWord(wordName: String, pos: String, freq: Int, domainType: Int): Int = {
    val word = DomainWordItem(wordName, domainType)
    word.setOccuredCount(pos, freq)

    addWordPOS(word)

    val index = arrayWordItem.size
    wordNameIndexHashMap += (wordName -> index)
    arrayWordItem = arrayWordItem ++ List(word)
    hashDictionary.addWord(word)
    if (wordName.length() > maxWordLength) {
      maxWordLength = wordName.length
    }
    return index
  }

  private def addWordPOS(word: IWord) {
    val coreDictionary = DictionaryFactory().getCoreDictionary()
    if (coreDictionary != null) {
      val wordInCoreDictionary = coreDictionary.getWord(word.getWordName())
      if (null != wordInCoreDictionary) {
        val posTable = wordInCoreDictionary.getPOSArray().getWordPOSTable()
        for (posAndFreq <- posTable) {
          word.setOccuredCount(POSUtil.getPOSString(posAndFreq(0)), posAndFreq(1))
        }
      }
    }
  }

  private def addSynonym(index: Int, synonymIndex: Int) {
    if (index >= 0 && synonymIndex >= 0) {
      synonymIndexHashMap += (index -> synonymIndex)
      var synonymSet = synonymHashMap.getOrElse(synonymIndex, null)
      if (null == synonymSet) {
        synonymSet = List[Int]()
      }
      if (!synonymSet.contains(index)) {
        synonymSet = synonymSet ++ List(index)
      }
      synonymHashMap += (synonymIndex -> synonymSet)
    }
  }
}
