package websiteschema.mpsegment.tools.accurary

import collection.mutable._
import collection.SortedMap

abstract class AbstractErrorAnalyzer extends ErrorAnalyzer {

  private var occurTimes = 0
  private var wordsWithError: Map[String, Int] = HashMap[String, Int]()

  override def getErrorOccurTimes(): Int = {
    return occurTimes
  }

  override def getWords(): ListMap[String, Int] = {
    return sort()
  }

  override def postAnalysis(allWordsAndFreqInCorpus: Map[String, Int]) {
  }

  def increaseOccur() {
    occurTimes += 1
  }

  def addErrorWord(word: String) {
    val occurs = if(wordsWithError.contains(word)) wordsWithError(word) + 1 else 1
    wordsWithError += (word -> occurs)
  }

  def removeErrorWord(word: String) {
    wordsWithError.remove(word)
  }

  private def sort(): ListMap[String, Int] = {
    return ListMap(wordsWithError.toList.sortBy{_._2}:_*)
  }
}
