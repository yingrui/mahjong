package me.yingrui.segment.tools.accurary

import me.yingrui.segment.core.{SegmentResult, SegmentWorker, Word}
import me.yingrui.segment.dict.POSUtil
import me.yingrui.segment.tools.PFRCorpusLoader
import me.yingrui.segment.tools.accurary.SegmentErrorType._
import me.yingrui.segment.util.FileUtil.getResourceAsStream

import scala.collection.mutable._

class SegmentAccuracy(testCorpus: String, segmentWorker: SegmentWorker) extends SegmentResultCompareHook {

  private var loader: PFRCorpusLoader = null
  private var totalWords: Int = 0
  private var actualWords: Int = 0
  private var correct: Int = 0
  private var wrong: Int = 0
  private var recallRate: Double = 0D

  private val allWordsAndFreqInCorpus = HashMap[String, Int]()
  private val allErrorAnalyzer = new LinkedHashMap[SegmentErrorType, ErrorAnalyzer]()

  initialErrorAnalyzer()
  loader = PFRCorpusLoader(getResourceAsStream(testCorpus))

  def getRecallRate() = recallRate

  def F() = 2.0 * (getRecallRate() * getPrecisionRate()) / (getRecallRate() + getPrecisionRate())

  def getPrecisionRate() = correct.toDouble / actualWords.toDouble

  def getWrong() = wrong

  def getTotalWords() = totalWords

  def getErrorAnalyzer(errorType: SegmentErrorType) = allErrorAnalyzer(errorType)

  def checkSegmentAccuracy() {
    try {
      var expectResult = loader.readLine()
      while (expectResult != null) {
        val sentence = expectResult.toOriginalString()
        val actualResult = segmentWorker.segment(sentence)

        NerNameStatisticData.scanNameWordCount(expectResult)
        NerNameStatisticData.scanRecognizedNameWordCount(actualResult)
//        println(actualResult)
        totalWords += expectResult.length()
        actualWords += actualResult.length()

        recordWordFreqInCorpus(expectResult)

        val comparator = new SegmentResultComparator(this)
        comparator.compare(expectResult, actualResult)
        expectResult = loader.readLine()
      }
    } catch {
      case ex: Throwable =>
        ex.printStackTrace()
    } finally {
      postAnalysis()
    }
    assert(correct > 0 && totalWords > 0)
    recallRate = correct.toDouble / totalWords.toDouble
  }

  private def initialErrorAnalyzer() {
    allErrorAnalyzer.put(SegmentErrorType.NER_NR, new NerNameErrorAnalyzer())
    allErrorAnalyzer.put(SegmentErrorType.NER_NS, new NerPlaceErrorAnalyzer())
    allErrorAnalyzer.put(SegmentErrorType.UnknownWord, new NewWordErrorAnalyzer())
    allErrorAnalyzer.put(SegmentErrorType.ContainDisambiguate, new ContainErrorAnalyzer())
    allErrorAnalyzer.put(SegmentErrorType.Other, new OtherErrorAnalyzer())
  }

  private def recordWordFreqInCorpus(expectResult: SegmentResult) {
    expectResult.foreach(word => {
      val freq = if (allWordsAndFreqInCorpus.contains(word.name)) allWordsAndFreqInCorpus(word.name) + 1 else 1
      allWordsAndFreqInCorpus.put(word.name, freq)
    })
  }

  private def postAnalysis() {
    for (errorType <- allErrorAnalyzer.keys) {
      getErrorAnalyzer(errorType).postAnalysis(allWordsAndFreqInCorpus)
    }
  }

  override def errorWordHook {
    wrong += 1
  }

  override def foundCorrectWordHook(expectWord: Word, matchedWord: Word, expectWordIndex: Int, matchedWordIndex: Int) {
    segmentCorrect(expectWord, matchedWord)
    correct += 1
  }

  private def segmentCorrect(expect: Word, actual: Word) {
    if (actual.pos == POSUtil.POS_NR) {
      NerNameStatisticData.correctRecognizedNameCount += 1
    }
  }

  override def analyzeReason(expect: Word, possibleErrorWord: String) {
    for (errorType <- allErrorAnalyzer.keys) {
      val analyzer = allErrorAnalyzer(errorType)
      val isErrorWord = analyzer.analysis(expect, possibleErrorWord)
      if (isErrorWord) {
        return
      }
    }
  }
}





