package websiteschema.mpsegment.filter

import websiteschema.mpsegment.util.{FileUtil, SerializeHandler}
import java.io.{DataInputStream, File}

class NameEntityRecognizerStatisticResult {
  val mapNormalWordFreq: java.util.Map[String, Int] = new java.util.HashMap[String, Int]()
  val mapNameWordFreq: java.util.Map[String, Int] = new java.util.HashMap[String, Int]()
  val mapWordFreq: java.util.Map[String, Int] = new java.util.HashMap[String, Int]()
  val mapCharBigram: java.util.Map[String, Int] = new java.util.HashMap[String, Int]()

  var charOccurTotal = 0.0D
  var wordOccurTotal = 0.0D
  var nameOccurTotal = 0.0D

  val mapRightBoundaryWordFreq: java.util.Map[String, Int] = new java.util.HashMap[String, Int]()
  val mapLeftBoundaryWordFreq: java.util.Map[String, Int] = new java.util.HashMap[String, Int]()

  def charFreq(han: String) = freqAsNormalWord(han) + freqAsNameWord(han)

  def freqAsNormalWord(han: String) = if(mapNormalWordFreq.containsKey(han)) mapNormalWordFreq.get(han) else 1

  def freqAsNameWord(han: String) = if(mapNameWordFreq.containsKey(han)) mapNameWordFreq.get(han) else 1

  def wordFreq(word:String) = if(mapWordFreq.containsKey(word)) mapWordFreq.get(word) else 1

  def freqAsRightBoundary(word:String) = if(mapRightBoundaryWordFreq.containsKey(word)) mapRightBoundaryWordFreq.get(word) else 1

  def freqAsLeftBoundary(word:String) = if(mapLeftBoundaryWordFreq.containsKey(word)) mapLeftBoundaryWordFreq.get(word) else 1

  def diff(han: String) = freqAsNameWord(han).toDouble / charFreq(han).toDouble

  def rightBoundaryDiff(word: String) = freqAsRightBoundary(word).toDouble / wordFreq(word).toDouble

  def leftBoundaryDiff(word: String) = freqAsLeftBoundary(word).toDouble / wordFreq(word).toDouble

  def bigram(word: String) = if (mapCharBigram.containsKey(word)) mapCharBigram.get(word) else 1

  def leftBoundaryMutualInformation(word: String) = {
    val pxy = freqAsLeftBoundary(word).toDouble / wordOccurTotal
    val px = wordFreq(word).toDouble / wordOccurTotal
    val py = nameOccurTotal / wordOccurTotal
    val p = pxy / (px * py)
    Math.log(p)/Math.log(2)
  }

  def diffLog(han: String) = Math.log(diff(han)) / Math.log(2)

  def rightBoundaryMutualInformation(word: String) = {
    val pxy = freqAsRightBoundary(word).toDouble / wordOccurTotal
    val px = wordFreq(word).toDouble / wordOccurTotal
    val py = nameOccurTotal / wordOccurTotal
    val p = pxy / (px * py)
    Math.log(p)/Math.log(2)
  }

  def rightBoundary(word: String) = {
    val pxy = (freqAsRightBoundary(word).toDouble + 1.0) / (wordFreq(word).toDouble + 1.0D)
    if (wordFreq(word).toDouble > 0.0D) pxy else 1.0 / wordOccurTotal
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

  def probability(name: List[String]) = {
    var sigma = 0.0D
    var count = 0.0D
    for (i <- 0 until name.length - 1) {
      sigma += (bigram(name(i) + name(i + 1)) + 1).toDouble / charOccurTotal
      count += 1.0D
    }
    Math.log(sigma / count) / Math.log(2)
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

  def normalWordFreqPlusOne(word: String) = plusOne(mapNormalWordFreq, word)

  def nameWordFreqPlusOne(word: String) = plusOne(mapNameWordFreq, word)

  def wordFreqPlusOne(word: String) = plusOne(mapWordFreq, word)

  def wordRightBoundaryFreqPlusOne(word: String) = plusOne(mapRightBoundaryWordFreq, word)

  def wordLeftBoundaryFreqPlusOne(word: String) = plusOne(mapLeftBoundaryWordFreq, word)

  def charBigramPlusOne(word: String) = plusOne(mapCharBigram , word)

  private def plusOne(map: java.util.Map[String, Int], key:String) {
    if (!map.containsKey(key)) map.put(key, 0)
    map.put(key, map.get(key) + 1)
  }

  def save(file: String) {
    val serializeHandle = SerializeHandler(new File(file), SerializeHandler.WRITE_ONLY)
    serializeHandle.serializeDouble(charOccurTotal)
    serializeHandle.serializeDouble(wordOccurTotal)
    serializeHandle.serializeDouble(nameOccurTotal)

    serializeHandle.serializeMapStringInt(mapNormalWordFreq)
    serializeHandle.serializeMapStringInt(mapNameWordFreq)
    serializeHandle.serializeMapStringInt(mapWordFreq)
    serializeHandle.serializeMapStringInt(mapCharBigram)
    serializeHandle.serializeMapStringInt(mapRightBoundaryWordFreq)
    serializeHandle.serializeMapStringInt(mapLeftBoundaryWordFreq)
  }

  def load(file: String) {
    val serializeHandle = new SerializeHandler(new DataInputStream(FileUtil.getResourceAsStream(file)), null)
    charOccurTotal = serializeHandle.deserializeDouble()
    wordOccurTotal = serializeHandle.deserializeDouble()
    nameOccurTotal = serializeHandle.deserializeDouble()

    mapNormalWordFreq.putAll(serializeHandle.deserializeMapStringInt())
    mapNameWordFreq.putAll(serializeHandle.deserializeMapStringInt())
    mapWordFreq.putAll(serializeHandle.deserializeMapStringInt())
    mapCharBigram.putAll(serializeHandle.deserializeMapStringInt())
    mapRightBoundaryWordFreq.putAll(serializeHandle.deserializeMapStringInt())
    mapLeftBoundaryWordFreq.putAll(serializeHandle.deserializeMapStringInt())
  }

}
