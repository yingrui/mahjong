package websiteschema.mpsegment.tools.accurary

import websiteschema.mpsegment.core.SegmentResult
import websiteschema.mpsegment.core.SegmentWorker
import websiteschema.mpsegment.core.WordAtom
import websiteschema.mpsegment.tools.PFRCorpusLoader
import websiteschema.mpsegment.util.NumberUtil
import websiteschema.mpsegment.util.StringUtil
import collection.mutable._
import websiteschema.mpsegment.tools.accurary.SegmentErrorType._
import websiteschema.mpsegment.dict.POSUtil

class SegmentAccuracy(testCorpus: String, segmentWorker: SegmentWorker) extends SegmentResultCompareHook {

  private var loader: PFRCorpusLoader = null
  private var totalWords: Int = 0
  private var correct: Int = 0
  private var wrong: Int = 0
  private var accuracyRate: Double = 0D

  private val allWordsAndFreqInCorpus = HashMap[String, Int]()
  private var allErrorAnalyzer: Map[SegmentErrorType, ErrorAnalyzer] = null


  initialErrorAnalyzer()
  loader = PFRCorpusLoader(getClass().getClassLoader().getResourceAsStream(testCorpus))

  def getAccuracyRate(): Double = {
    return accuracyRate
  }

  def getWrong(): Int = {
    return wrong
  }

  def getTotalWords(): Int = {
    return totalWords
  }

  def getErrorAnalyzer(errorType: SegmentErrorType): ErrorAnalyzer = {
    return allErrorAnalyzer(errorType)
  }

  def checkSegmentAccuracy() {
    val isUseContextFreq = segmentWorker.isUseContextFreqSegment()
    segmentWorker.setUseContextFreqSegment(true)
    try {
      var expectResult = loader.readLine()
      while (expectResult != null) {
        val sentence = expectResult.toOriginalString()
        val actualResult = segmentWorker.segment(sentence)

        NerNameStatisticData.scanNameWordCount(expectResult)
        NerNameStatisticData.scanRecognizedNameWordCount(actualResult)
//        println(actualResult)
        totalWords += expectResult.length()

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
      segmentWorker.setUseContextFreqSegment(isUseContextFreq)
    }
    assert(correct > 0 && totalWords > 0)
    accuracyRate = correct.toDouble / totalWords.toDouble
  }

  private def initialErrorAnalyzer() {
    allErrorAnalyzer = new LinkedHashMap[SegmentErrorType, ErrorAnalyzer]()
    allErrorAnalyzer.put(SegmentErrorType.NER_NR, new NerNameErrorAnalyzer())
    allErrorAnalyzer.put(SegmentErrorType.NER_NS, new NerPlaceErrorAnalyzer())
    allErrorAnalyzer.put(SegmentErrorType.UnknownWord, new NewWordErrorAnalyzer())
    allErrorAnalyzer.put(SegmentErrorType.ContainDisambiguate, new ContainErrorAnalyzer())
    allErrorAnalyzer.put(SegmentErrorType.Other, new OtherErrorAnalyzer())
  }

  private def recordWordFreqInCorpus(expectResult: SegmentResult) {
    expectResult.foreach(word => {
      val freq = if (allWordsAndFreqInCorpus.contains(word.word)) allWordsAndFreqInCorpus(word.word) + 1 else 1
      allWordsAndFreqInCorpus.put(word.word, freq)
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

  override def correctWordHook(expectWord: WordAtom, matchedWord: WordAtom) {
    segmentCorrect(expectWord, matchedWord)
    correct += 1
  }

  private def segmentCorrect(expect: WordAtom, actual: WordAtom) {
    if (actual.pos == POSUtil.POS_NR) {
      NerNameStatisticData.correctRecognizedNameCount += 1
    }
  }

  override def analyzeReason(expect: WordAtom, possibleErrorWord: String) {
    for (errorType <- allErrorAnalyzer.keys) {
      val analyzer = allErrorAnalyzer(errorType)
      val isErrorWord = analyzer.analysis(expect, possibleErrorWord)
      if (isErrorWord) {
        return
      }
    }
  }
}





