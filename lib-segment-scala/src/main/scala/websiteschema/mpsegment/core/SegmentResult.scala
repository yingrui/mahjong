package websiteschema.mpsegment.core

import websiteschema.mpsegment.dict.POSUtil
import websiteschema.mpsegment.util.CharCheckUtil
import collection.mutable.ListBuffer

class SegmentResult(size: Int) {

  private var wordAtoms: Array[WordAtom] = null

  wordAtoms = new Array[WordAtom](size)
  for (i <- 0 until size) {
    wordAtoms(i) = new WordAtom()
  }

  def getWordAtoms(): Array[WordAtom] = {
    return wordAtoms
  }

  def setWords(words: Array[String]) {
    for (i <- 0 until words.length) {
      wordAtoms(i).word = words(i)
    }
  }

  def setPOSArray(tags: Array[Int]) {
    for (i <- 0 until tags.length) {
      wordAtoms(i).pos = tags(i)
    }
  }

  def setDomainTypes(marks: Array[Int]) {
    for (i <- 0 until marks.length) {
      wordAtoms(i).domainType = marks(i)
    }
  }

  def setConcepts(concepts: Array[String]) {
    for (i <- 0 until concepts.length) {
      wordAtoms(i).concept = concepts(i)
    }
  }

  def setPinyin(pinyin: Array[String]) {
    for (i <- 0 until pinyin.length) {
      wordAtoms(i).pinyin = pinyin(i)
    }
  }

  def getWord(i: Int): String = {
    return wordAtoms(i).word
  }

  def getWordIndexInOriginalString(index: Int): Int = {
    var wordIndexInOriginalString = 0
    for (i <- 0 until index) {
      wordIndexInOriginalString += wordAtoms(i).word.length
    }
    return wordIndexInOriginalString
  }

  def getPinyin(i: Int): String = {
    return wordAtoms(i).pinyin
  }

  def getPOS(i: Int): Int = {
    return wordAtoms(i).pos
  }

  def getConcept(i: Int): String = {
    return wordAtoms(i).concept
  }

  def getDomainType(i: Int): Int = {
    return wordAtoms(i).domainType
  }

  def length(): Int = {
    if (wordAtoms != null) {
      return wordAtoms.size
    } else {
      return 0
    }
  }

  def getWordAtom(i: Int): WordAtom = {
    return wordAtoms(i)
  }

  def setWord(index: Int, word: String) {
    wordAtoms(index).word = word
  }

  def setPOS(index: Int, pos: Int) {
    wordAtoms(index).pos = pos
  }

  def setConcept(index: Int, concept: String) {
    wordAtoms(index).concept = concept
  }

  def setPinyin(index: Int, pinyin: String) {
    wordAtoms(index).pinyin = pinyin
  }

  def append(segmentResult: SegmentResult) {
    wordAtoms ++= segmentResult.wordAtoms
  }

  def merge(start: Int, end: Int, pos: Int) {
    var wordAtom = mergedWordAtom(start, end, pos)
    if (null != wordAtom) {
      for (i <- start + 1 to end) {
        markWordToBeDeleted(i)
      }
    }
  }

  def markWordToBeDeleted(i: Int) {
    wordAtoms(i) = null
  }

  def compact() {
    wordAtoms = wordAtoms.filter(_ != null)
  }

  def toOriginalString(): String = {
    val stringBuilder = new StringBuilder()
    for (i <- 0 until length()) {
      stringBuilder.append(getWord(i))
    }
    return stringBuilder.toString()
  }

  override def toString(): String = {
    val retString = new StringBuilder()
    for (j <- 0 until length()) {
      retString.append(getWord(j)).append("/").append(POSUtil.getPOSString(getPOS(j))).append(" ")
    }

    return retString.toString()
  }

  private def mergedWordAtom(start: Int, end: Int, pos: Int): WordAtom = {
    val wordName = new StringBuilder()
    val pinyin = new StringBuilder()
    val startWordAtom = getStartWordAtom(start)
    appendWordAndPinyin(wordName, pinyin, startWordAtom)
    for (i <- start + 1 to end) {
      val wordAtom = wordAtoms(i)
      if (wordAtom != null) {
        appendWordAndPinyin(wordName, pinyin, wordAtom)
      }
    }
    startWordAtom.word = wordName.toString()
    startWordAtom.pinyin = pinyin.toString()
    startWordAtom.pos = pos
    return startWordAtom
  }

  private def appendWordAndPinyin(wordName: StringBuilder, pinyin: StringBuilder, startWordAtom: WordAtom) {
    wordName.append(startWordAtom.word)
    appendPinyin(pinyin, startWordAtom)
  }

  private def appendPinyin(pinyin: StringBuilder, startWordAtom: WordAtom) {
    if (null != startWordAtom.pinyin) {
      if (pinyin.length() > 0 && CharCheckUtil.isChinese(startWordAtom.word)) {
        pinyin.append("'")
      }
      pinyin.append(startWordAtom.pinyin)
    }
  }

  private def getStartWordAtom(index: Int): WordAtom = {
    val wordAtom = getWordAtom(index)
    if (null != wordAtom) {
      return wordAtom
    } else {
      return lookupWordAtomBefore(index)
    }
  }

  private def lookupWordAtomBefore(index: Int): WordAtom = {
    var i = index - 1
    while (i >= 0) {
      val wordAtom = getWordAtom(i)
      if (null != wordAtom) {
        return wordAtom
      }
      i -= 1
    }
    return null
  }


}
