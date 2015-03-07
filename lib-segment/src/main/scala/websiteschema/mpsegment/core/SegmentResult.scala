package websiteschema.mpsegment.core

import websiteschema.mpsegment.dict.POSUtil
import websiteschema.mpsegment.util.CharCheckUtil

class SegmentResult(size: Int) {

  private var words: Array[Word] = null

  words = new Array[Word](size)
  for (i <- 0 until size) {
    words(i) = new Word()
  }

  def getWords() = words

  def map[B](op: Word => B) = words.map(op)

  def setWords(wordArray: Array[String]) {
    for (i <- 0 until wordArray.length) {
      words(i).name = wordArray(i)
    }
  }

  def setWordStartAts(wordStartAts: Array[Int]) {
    for (i <- 0 until wordStartAts.length) {
      words(i).start = wordStartAts(i)
    }
  }

  def setWordEndAts(wordEndAts: Array[Int]) {
    for (i <- 0 until wordEndAts.length) {
      words(i).end = wordEndAts(i)
    }
  }

  def setPOSArray(tags: Array[Int]) {
    for (i <- 0 until tags.length) {
      words(i).pos = tags(i)
    }
  }

  def setDomainTypes(marks: Array[Int]) {
    for (i <- 0 until marks.length) {
      words(i).domainType = marks(i)
    }
  }

  def setConcepts(concepts: Array[String]) {
    for (i <- 0 until concepts.length) {
      words(i).concept = concepts(i)
    }
  }

  def setPinyin(pinyin: Array[String]) {
    for (i <- 0 until pinyin.length) {
      words(i).pinyin = pinyin(i)
    }
  }

  def getWord(i: Int) = words(i).name

  def getWordStartAt(index: Int) = words(index).start

  def getWordEndAt(index: Int) = words(index).end

  def getPinyin(i: Int) = words(i).pinyin

  def getPOS(i: Int) = words(i).pos

  def getConcept(i: Int) = words(i).concept

  def getDomainType(i: Int) = words(i).domainType

  def length(): Int = if (words != null) words.size else 0

  def apply(i: Int) = words(i)

  def setWord(index: Int, word: String) {
    words(index).name = word
  }

  def setPOS(index: Int, pos: Int) {
    words(index).pos = pos
  }

  def setConcept(index: Int, concept: String) {
    words(index).concept = concept
  }

  def setPinyin(index: Int, pinyin: String) {
    words(index).pinyin = pinyin
  }

  def append(segmentResult: SegmentResult) {
    words ++= segmentResult.words
  }

  def merge(start: Int, end: Int, pos: Int) {
    val word = mergedWords(start, end, pos)
    if (null != word) {
      for (i <- start + 1 to end) {
        markWordToBeDeleted(i)
      }
    }
  }

  def separate(index: Int, indexAtWord: Int, firstPOS: Int, secondPOS: Int) {
    val _words = new Array[Word](length() + 1)
    var offset = 0
    for (i <- 0 until length()) {
      _words(i + offset) = words(i)
      if (i == index) {
        offset = 1
        val end = _words(i).end
        val wordName = words(i).name

        _words(i).name = wordName.substring(0, indexAtWord)
        _words(i).pos = firstPOS
        _words(i).end = _words(i).start + indexAtWord

        val word = new Word()
        word.name = wordName.substring(indexAtWord)
        word.pos = secondPOS
        word.concept = "N/A"
        word.start = _words(i).end
        word.end = end

        _words(i + offset) = word
      }
    }
    words = _words
  }

  def markWordToBeDeleted(i: Int) {
    words(i) = null
  }

  def compact() {
    words = words.filter(_ != null)
  }

  def adjustAdjacentWords(wordIndex: Int, from: Int) {
    val word = words(wordIndex).name
    words(wordIndex).name = word.substring(0, from)
    words(wordIndex).end = words(wordIndex).start + from
    words(wordIndex + 1).name = word.substring(from) + words(wordIndex + 1).name
    words(wordIndex + 1).start = words(wordIndex).end
  }

  def toOriginalString(): String = {
    val stringBuilder = new StringBuilder()
    for (i <- 0 until length()) {
      stringBuilder.append(getWord(i))
    }
    stringBuilder.toString()
  }

  def indexWhere(p: (Word) => Boolean, from: Int): Int = words.indexWhere(p, from)

  def foreach(fn: (Word) => Unit) {
    words.foreach(fn)
  }

  override def toString(): String = {
    val retString = new StringBuilder()
    for (j <- 0 until length()) {
      retString.append(getWord(j)).append("/").append(POSUtil.getPOSString(getPOS(j))).append(" ")
    }

    retString.toString()
  }

  private def mergedWords(start: Int, end: Int, pos: Int): Word = {
    val wordName = new StringBuilder()
    val pinyin = new StringBuilder()
    val startWord = getStartWord(start)
    appendWordAndPinyin(wordName, pinyin, startWord)
    for (i <- start + 1 to end) {
      val word = words(i)
      if (word != null) {
        appendWordAndPinyin(wordName, pinyin, word)
      }
    }
    startWord.name = wordName.toString()
    startWord.pinyin = pinyin.toString()
    startWord.pos = pos
    startWord.end = getStartWord(end).end
    return startWord
  }

  private def appendWordAndPinyin(wordName: StringBuilder, pinyin: StringBuilder, startWord: Word) {
    wordName.append(startWord.name)
    appendPinyin(pinyin, startWord)
  }

  private def appendPinyin(pinyin: StringBuilder, startWordAtom: Word) {
    if (null != startWordAtom.pinyin) {
      if (pinyin.length() > 0 && CharCheckUtil.isChinese(startWordAtom.name)) {
        pinyin.append("'")
      }
      pinyin.append(startWordAtom.pinyin)
    }
  }

  private def getStartWord(index: Int): Word = {
    val word = apply(index)
    if (null != word) {
      return word
    } else {
      return lookupWordBefore(index)
    }
  }

  private def lookupWordBefore(index: Int): Word = {
    var i = index - 1
    while (i >= 0) {
      val word = apply(i)
      if (null != word) {
        return word
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
