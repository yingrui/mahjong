package websiteschema.mpsegment.core

import websiteschema.mpsegment.dict.POSUtil
import websiteschema.mpsegment.util.CharCheckUtil

class SegmentResult(size: Int) {

  private var wordAtoms: Array[WordAtom] = null

  wordAtoms = new Array[WordAtom](size)
  for (i <- 0 until size) {
    wordAtoms(i) = new WordAtom()
  }

  def getWordAtoms() = wordAtoms

  def setWords(words: Array[String]) {
    for (i <- 0 until words.length) {
      wordAtoms(i).word = words(i)
    }
  }

  def setWordStartAts(wordStartAts: Array[Int]) {
    for (i <- 0 until wordStartAts.length) {
      wordAtoms(i).start = wordStartAts(i)
    }
  }

  def setWordEndAts(wordEndAts: Array[Int]) {
    for (i <- 0 until wordEndAts.length) {
      wordAtoms(i).end = wordEndAts(i)
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

  def getWord(i: Int) = wordAtoms(i).word

  def getWordStartAt(index: Int) = wordAtoms(index).start

  def getWordEndAt(index: Int) = wordAtoms(index).end

  def getPinyin(i: Int) = wordAtoms(i).pinyin

  def getPOS(i: Int) = wordAtoms(i).pos

  def getConcept(i: Int) = wordAtoms(i).concept

  def getDomainType(i: Int) = wordAtoms(i).domainType

  def length(): Int = if (wordAtoms != null) wordAtoms.size else 0

  def apply(i: Int) = wordAtoms(i)

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
    val wordAtom = mergedWordAtom(start, end, pos)
    if (null != wordAtom) {
      for (i <- start + 1 to end) {
        markWordToBeDeleted(i)
      }
    }
  }

  def separate(index: Int, indexAtWord: Int, firstPOS: Int, secondPOS: Int) {
    val _wordAtoms = new Array[WordAtom](length() + 1)
    var offset = 0
    for (i <- 0 until length()) {
      _wordAtoms(i + offset) = wordAtoms(i)
      if (i == index) {
        offset = 1
        val end = _wordAtoms(i).end
        val word = wordAtoms(i).word

        _wordAtoms(i).word = word.substring(0, indexAtWord)
        _wordAtoms(i).pos = firstPOS
        _wordAtoms(i).end = _wordAtoms(i).start + indexAtWord

        val wordAtom = new WordAtom()
        wordAtom.word = word.substring(indexAtWord)
        wordAtom.pos = secondPOS
        wordAtom.concept = "N/A"
        wordAtom.start = _wordAtoms(i).end
        wordAtom.end = end

        _wordAtoms(i + offset) = wordAtom
      }
    }
    wordAtoms = _wordAtoms
  }

  def markWordToBeDeleted(i: Int) {
    wordAtoms(i) = null
  }

  def compact() {
    wordAtoms = wordAtoms.filter(_ != null)
  }

  def adjustAdjacentWords(wordIndex: Int, from: Int) {
    val word = wordAtoms(wordIndex).word
    wordAtoms(wordIndex).word = word.substring(0, from)
    wordAtoms(wordIndex).end = wordAtoms(wordIndex).start + from
    wordAtoms(wordIndex + 1).word = word.substring(from) + wordAtoms(wordIndex + 1).word
    wordAtoms(wordIndex + 1).start = wordAtoms(wordIndex).end
  }

  def toOriginalString(): String = {
    val stringBuilder = new StringBuilder()
    for (i <- 0 until length()) {
      stringBuilder.append(getWord(i))
    }
    stringBuilder.toString()
  }

  def indexWhere(p: (WordAtom) => Boolean, from: Int): Int = wordAtoms.indexWhere(p, from)

  def foreach(fn: (WordAtom) => Unit) {
    wordAtoms.foreach(fn)
  }

  override def toString(): String = {
    val retString = new StringBuilder()
    for (j <- 0 until length()) {
      retString.append(getWord(j)).append("/").append(POSUtil.getPOSString(getPOS(j))).append(" ")
    }

    retString.toString()
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
    startWordAtom.end = getStartWordAtom(end).end
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
    val wordAtom = apply(index)
    if (null != wordAtom) {
      return wordAtom
    } else {
      return lookupWordAtomBefore(index)
    }
  }

  private def lookupWordAtomBefore(index: Int): WordAtom = {
    var i = index - 1
    while (i >= 0) {
      val wordAtom = apply(i)
      if (null != wordAtom) {
        return wordAtom
      }
      i -= 1
    }
    return null
  }
}

object SegmentResult {

  def apply(words: Array[String], posArray: Array[Int]): SegmentResult = {
    val length = words.length
    val segmentResult = new SegmentResult(length)
    segmentResult.setWords(words)
    segmentResult.setPOSArray(posArray)
    segmentResult.setDomainTypes((for (i <- 0 until length) yield 0).toArray)
    segmentResult.setConcepts((for (i <- 0 until length) yield "N/A").toArray)
    for (i <- 0 until length) {
      val start = if (i == 0) 0 else segmentResult.getWordEndAt(i - 1)
      segmentResult(i).start = start
      segmentResult(i).end = start + words(i).length
    }
    segmentResult
  }
}
