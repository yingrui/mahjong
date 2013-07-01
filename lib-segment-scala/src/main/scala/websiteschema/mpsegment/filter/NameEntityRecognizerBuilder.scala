package websiteschema.mpsegment.filter

import java.util
import websiteschema.mpsegment.tools.PFRCorpusLoader
import websiteschema.mpsegment.util.FileUtil._
import websiteschema.mpsegment.core.SegmentResult
import websiteschema.mpsegment.dict.POSUtil

class NameEntityRecognizerStatisticResult {
  val mapNormalWordFreq: java.util.Map[String, Int] = new util.HashMap[String, Int]()
  val mapNameWordFreq: java.util.Map[String, Int] = new util.HashMap[String, Int]()
  val mapWordFreq: java.util.Map[String, Int] = new util.HashMap[String, Int]()
  val mapCharBigram: util.Map[String, Int] = new util.HashMap[String, Int]()

  var charOccurTotal = 0.0D
  var wordOccurTotal = 0.0D
  var nameOccurTotal = 0.0D

  val mapRightBoundaryWordFreq: java.util.Map[String, Int] = new util.HashMap[String, Int]()
  val mapLeftBoundaryWordFreq: java.util.Map[String, Int] = new util.HashMap[String, Int]()

  def charFreq(han: String) = freqAsNormalWord(han) + freqAsNameWord(han)

  def freqAsNormalWord(han: String) = if(mapNormalWordFreq.containsKey(han)) mapNormalWordFreq.get(han) else 0

  def freqAsNameWord(han: String) = if(mapNameWordFreq.containsKey(han)) mapNameWordFreq.get(han) else 0

  def wordFreq(word:String) = if(mapWordFreq.containsKey(word)) mapWordFreq.get(word) else 0

  def freqAsRightBoundary(word:String) = if(mapRightBoundaryWordFreq.containsKey(word)) mapRightBoundaryWordFreq.get(word) else 0

  def freqAsLeftBoundary(word:String) = if(mapLeftBoundaryWordFreq.containsKey(word)) mapLeftBoundaryWordFreq.get(word) else 0

  def diff(han: String) = freqAsNameWord(han).toDouble / charFreq(han).toDouble

  def rightBoundaryDiff(word: String) = freqAsRightBoundary(word).toDouble / wordFreq(word).toDouble

  def leftBoundaryDiff(word: String) = freqAsLeftBoundary(word).toDouble / wordFreq(word).toDouble

  def bigram(word: String) = if (mapCharBigram.containsKey(word)) mapCharBigram.get(word) else 0

  def leftBoundaryMutualInformation(word: String) = {
    val pxy = freqAsLeftBoundary(word).toDouble / wordOccurTotal
    val px = wordFreq(word).toDouble / wordOccurTotal
    val py = nameOccurTotal / wordOccurTotal
    val p = pxy / (px * py)
    Math.log(p)/Math.log(2)
  }

  def rightBoundaryMutualInformation(word: String) = {
    val pxy = freqAsRightBoundary(word).toDouble / wordOccurTotal
    val px = wordFreq(word).toDouble / wordOccurTotal
    val py = nameOccurTotal / wordOccurTotal
    val p = pxy / (px * py)
    Math.log(p)/Math.log(2)
  }

  def conditionProbability(name: List[String]) = {
    var freqAsName = 0.0D
    var freq = 0.0D
    for (han <- name) {
      freqAsName += Math.log(freqAsNameWord(han) + 1)
      freq += Math.log(charFreq(han) + 1)
    }

    (freqAsName - freq) / Math.log(2)
  }

  def mutualInformation(name: List[String]) = {
    var sigma = 0.0D
    var count = 0.0D
    for (i <- 0 until name.length - 1) {
      sigma += mutualInformationBetweenChars(name(i), name(i + 1))
      count += 1.0D
    }
    sigma / count
  }

  private def mutualInformationBetweenChars(name1: String, name2: String): Double = {
    val pxy = (bigram(name1 + name2) + 1).toDouble / charOccurTotal
    val px = (charFreq(name1) + 1).toDouble / charOccurTotal
    val py = (charFreq(name2) + 1).toDouble / charOccurTotal
    val p = pxy / (px * py)
    Math.log(p)/Math.log(2)
  }

  //

  def normalWordFreqPlusOne(word: String) = plusOne(mapNormalWordFreq, word)

  def nameWordFreqPlusOne(word: String) = plusOne(mapNameWordFreq, word)

  def wordFreqPlusOne(word: String) = plusOne(mapWordFreq, word)

  def wordRightBoundaryFreqPlusOne(word: String) = plusOne(mapRightBoundaryWordFreq, word)

  def wordLeftBoundaryFreqPlusOne(word: String) = plusOne(mapLeftBoundaryWordFreq, word)

  def charBigramPlusOne(word: String) = plusOne(mapCharBigram , word)

  private def plusOne(map: util.Map[String, Int], key:String) {
    if (!map.containsKey(key)) map.put(key, 0)
    map.put(key, map.get(key) + 1)
  }
}

class NameEntityRecognizerBuilder {
  var loader: PFRCorpusLoader = null
  val result = new NameEntityRecognizerStatisticResult()

  def load(resource: String) = {
    loader = PFRCorpusLoader(getResourceAsStream(resource))
  }

  def analysis: NameEntityRecognizerStatisticResult = {
    var sentence = loader.readLine()
    while (sentence != null) {
      statisticFrequency(sentence)
      sentence = loader.readLine()
    }
    result
  }

  private def statisticFrequency(sentence: SegmentResult) {
    for (i <- 0 until sentence.length()) {
      val word = sentence.getWord(i)

      result.wordOccurTotal += 1.0D
      if (sentence.getPOS(i) == POSUtil.POS_NR) result.nameOccurTotal += 1.0D

      statisticBoundary(i, sentence)
      for (ch <- word) {
        if (sentence.getPOS(i) == POSUtil.POS_NR) {
          result.nameWordFreqPlusOne(ch.toString)
        } else {
          result.normalWordFreqPlusOne(ch.toString)
        }
      }
    }

    val sentenceString = sentence.toOriginalString()
    for (i <- 0 until sentenceString.length) {
      result.charOccurTotal += 1.0D
      if (i < sentenceString.length - 1) {
        result.charBigramPlusOne(sentenceString.substring(i, i + 2))
      }
    }
  }

  private def statisticBoundary(i: Int, sentence: SegmentResult) {
    val word = sentence.getWord(i)
    result.wordFreqPlusOne(word)
    if (i > 0 && (isPreviousWordPosNR(sentence, i) || isPreviousWordPosR(sentence, i))) {
      result.wordRightBoundaryFreqPlusOne(word)
    }

    if (i < sentence.length() - 1 && (isNextWordPosNR(sentence, i) || isNextWordPosR(sentence, i))) {
      result.wordLeftBoundaryFreqPlusOne(word)
    }
  }

  private def isNextWordPosNR(sentence: SegmentResult, i: Int): Boolean = {
    sentence.getPOS(i + 1) == POSUtil.POS_NR
  }

  private def isNextWordPosR(sentence: SegmentResult, i: Int): Boolean = {
    sentence.getPOS(i + 1) == POSUtil.POS_R
  }

  private def isPreviousWordPosR(sentence: SegmentResult, i: Int): Boolean = {
    sentence.getPOS(i - 1) == POSUtil.POS_R
  }

  private def isPreviousWordPosNR(sentence: SegmentResult, i: Int): Boolean = {
    sentence.getPOS(i - 1) == POSUtil.POS_NR
  }
}

object NameEntityRecognizerBuilder {
  def apply() = new NameEntityRecognizerBuilder()
}
