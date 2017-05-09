package me.yingrui.segment.core

import me.yingrui.segment.dict.{DictionaryLookupResult, DictionaryService, IWord}
import me.yingrui.segment.util.StringUtil

abstract class AbstractWordScanner {
  private var sentence: String = null
  private var maxWordLength: Int = 0
  private var dictionaryService: DictionaryService = null

  def getDictionaryService(): DictionaryService = {
    return dictionaryService
  }

  def startScanningAt(startPos: Int) {
    val lastMinWordLen = 0
    val length = sentence.length()
    try {
      var begin = startPos
      while (begin < length) {
        val minWordLen = scanTheMinWordLength(begin)

        //find single Char word or multi-chars alpha-numeric word
        val atomWord = sentence.substring(begin, begin + minWordLen)
        val singleCharWord = foundAtomWord(atomWord)

        //find all possible slices except single word and english word
        val candidateWord = getCandidateSentence(begin, lastMinWordLen)
        val result = dictionaryService.lookup(candidateWord)
        processFoundWordItems(begin, singleCharWord, result)

        begin += 1
      }
    } catch {
      case ex: Throwable =>
        println(ex)
    } finally {
    }
  }

  def foundAtomWord(atomWord: String): IWord

  def processFoundWordItems(begin: Int, singleCharWord: IWord, lookupResult: DictionaryLookupResult)

  def setSentence(sentence: String) {
    this.sentence = sentence
  }

  def setMaxWordLength(maxWordLength: Int) {
    this.maxWordLength = maxWordLength
  }

  def setDictionaryService(dictionaryService: DictionaryService) {
    this.dictionaryService = dictionaryService
  }

  private def scanTheMinWordLength(begin: Int): Int = {
    val index = scanEnglishWordAndShorten(begin)
    val minWordLen = (index - begin) + 1
    return minWordLen
  }

  private def scanEnglishWordAndShorten(begin: Int): Int = {
    val sentenceLength = sentence.length()
    var index = begin
    if (StringUtil.isCharAlphabeticalOrDigital(sentence.charAt(index))) {
      while (index < sentenceLength && StringUtil.isCharAlphabeticalOrDigital(sentence.charAt(index))) {
        index += 1
      }
      index -= 1
    }
    return index
  }

  private def getCandidateSentence(begin: Int, lastMinWordLen1: Int): String = {
    var candidateWord = ""
    val length = sentence.length()
    val rest = length - begin
    if (maxWordLength <= rest) {
      var end = (begin + maxWordLength + lastMinWordLen1) - 1
      end = if (end < length)
        end
      else length
      candidateWord = sentence.substring(begin, end)
    } else {
      candidateWord = sentence.substring(begin)
    }
    return candidateWord
  }
}
