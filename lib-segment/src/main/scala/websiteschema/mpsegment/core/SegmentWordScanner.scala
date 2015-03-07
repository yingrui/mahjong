package websiteschema.mpsegment.core

import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.dict.DictionaryLookupResult
import websiteschema.mpsegment.dict.IWord
import websiteschema.mpsegment.dict.POSUtil
import websiteschema.mpsegment.dict.UnknownWord
import websiteschema.mpsegment.graph.IGraph

import collection.mutable.Map

class SegmentWordScanner(segmentMin: Boolean, useContextFreqSegment: Boolean, graph: IGraph, contextFreqMap: Map[String, Int]) extends AbstractWordScanner {

  private val BigWordLength = 4
  private val logCorpus = MPSegmentConfiguration.LOG_CORPUS

  override def foundAtomWord(atomWord: String): IWord = {
    var singleCharWord = getDictionaryService().lookup(atomWord).firstMatchWord
    if (singleCharWord == null) {
      singleCharWord = initAsUnknownWord(atomWord); //Unknown Word
    }
    return singleCharWord
  }

  override def processFoundWordItems(begin: Int, singleCharWord: IWord, lookupResult: DictionaryLookupResult) {
    val the1stMatchWord = lookupResult.firstMatchWord
    val the2ndMatchWord = lookupResult.the2ndMatchWord
    val the3rdMatchWord = lookupResult.the3rdMatchWord
    val matchedWordCount = lookupResult.matchedWordCount
    //将查找到的词添加到图中。
    //为了减少图的分支，同时因为单字词在中文中往往没有太多意义。
    //如果存在多个多字词，则不向图中添加单字词
    val shouldAddSingleCharWord = the1stMatchWord == null || the1stMatchWord.getWordLength() == 1 || matchedWordCount < 2
    if (shouldAddSingleCharWord) {
      addSingleCharWordToGraph(begin, singleCharWord.getWordLength(), singleCharWord)
    }
    val foundOtherWords = the1stMatchWord != null && the1stMatchWord.getWordLength() > 1
    if (foundOtherWords) {
      addMatchedWordToGraph(begin, the2ndMatchWord)
      addMatchedWordToGraph(begin, the3rdMatchWord)
      if (!segmentMin || !isMatchedMoreThanOneWord(matchedWordCount) || !isFirstMatchedWordBigWord(the1stMatchWord) || isAtomWordAccordingPOS(the1stMatchWord)) {
        addMatchedWordToGraph(begin, the1stMatchWord)
      }
    }
  }

  private def initAsUnknownWord(unknownWordStr: String): IWord = {
    return UnknownWord(unknownWordStr)
  }

  private def isFirstMatchedWordBigWord(the1stMatchWord: IWord): Boolean = {
    return the1stMatchWord.getWordLength() > BigWordLength
  }

  private def isMatchedMoreThanOneWord(matchedWordCount: Int): Boolean = {
    return matchedWordCount > 1
  }

  private def isAtomWordAccordingPOS(the1stMatchWord: IWord): Boolean = {
    val wordPOSTable = the1stMatchWord.getWordPOSTable()
    val the1stMatchedWordPOS = if (null != wordPOSTable) wordPOSTable(0)(0) else -1
    return the1stMatchedWordPOS == POSUtil.POS_I || the1stMatchedWordPOS == POSUtil.POS_L
  }

  private def addMatchedWordToGraph(begin: Int, matchedWord: IWord) {
    if (null != matchedWord) {
      addEdgeObject(begin + 1, begin + matchedWord.getWordLength() + 1, getWeight(matchedWord), matchedWord)
    }
  }

  private def addSingleCharWordToGraph(begin: Int, lastMinWordLen: Int, singleCharWord: IWord) {
    addEdgeObject(begin + 1, begin + lastMinWordLen + 1, getWeight(singleCharWord), singleCharWord)
  }

  private def addEdgeObject(head: Int, tail: Int, weight: Int, word: IWord) {
    graph.addEdge(head, tail, weight, word)
  }

  private def getWeight(word: IWord): Int = {
    if (useContextFreqSegment) {
      val wordName = word.getWordName()
      var weight = 1
      if (wordName.length() > 1) {
        val contextFreq = if (contextFreqMap.contains(wordName)) contextFreqMap(wordName) else 1
        val freq = getFreqWeight(word)
        weight = freq + getContextFreqWeight(freq, contextFreq)
      } else {
        weight = getFreqWeight(word)
      }
      if (weight <= 0) {
        weight = 1
      }
      return weight
    }
    var weight = getFreqWeight(word)
    return weight
  }

  private def getFreqWeight(word: IWord): Int = {
    val log2Freq = word.getLog2Freq()
    if (logCorpus > log2Freq) {
      val freqWeight = (logCorpus - word.getLog2Freq()).toInt
      return freqWeight
    } else {
      return 1
    }
  }

  private def getContextFreqWeight(freq: Int, contextFreq: Int): Int = {
    val weight = -((1 - Math.exp(-0.1 * (contextFreq - 1))) * freq).toInt
    return weight
  }
}
