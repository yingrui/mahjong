package websiteschema.mpsegment.filter.ner

import websiteschema.mpsegment.core.SegmentResult
import websiteschema.mpsegment.filter.{ForeignName, NameEntityRecognizerStatisticResult}
import websiteschema.mpsegment.util.SerializeHandler
import java.io.File
import websiteschema.mpsegment.pinyin.WordToPinyinClassfierFactory
import websiteschema.mpsegment.neural.{Normalizer, Sigmoid, Layer, NeuralNetwork}
import websiteschema.mpsegment.math.Matrix
import websiteschema.mpsegment.dict.POSUtil

class NeuralNetworkChineseNameRecognizer(val segmentResult: SegmentResult, featureExtractor: NERFeatures, network: NeuralNetwork, normalizer: Normalizer) extends NameEntityRecognizer {

  def recognizeNameWordBetween(begin: Int, end: Int): Int = {
    val nameWordLength = recognizeName(begin, end)
    if (nameWordLength < 0 && end - begin == 2) {
      recognizeNameWordBetween(begin, end - 1)
    } else {
      nameWordLength
    }
  }


  def recognizeName(begin: Int, end: Int): Int = {
    val name = (for (i <- begin to end) yield segmentResult.getWord(i)).mkString("")
    val len = end - begin + 1
    if (len <= 1) {
      -1
    } else {
      if (len >= 3) {
        if (isAllSingleWord(begin, end) && isAllForeignName(begin, end)) {
//          println(name)
          calculateByNeuralNetwork(begin, end)
        } else {
          val newEnd = begin + 2
          if (startWithXing(begin) && isAllSingleWord(begin, newEnd)) {
//            println(name)
            calculateByNeuralNetwork(begin, newEnd)
          } else -1
        }
      } else {
        if (segmentResult.getWord(end).length <= 2 && startWithXing(begin) && isAllSingleWord(begin, end)) {
//          println(name)
          calculateByNeuralNetwork(begin, end)
        } else -1
      }
    }
  }

  def startWithXing(begin: Int): Boolean = NeuralNetworkChineseNameRecognizer.nameDistribution.xingSet.contains(segmentResult.getWord(begin))

  def isAllSingleWord(begin: Int, end: Int): Boolean = {
    var isSingleWord = true
    for (i <- begin to end) {
      isSingleWord = isSingleWord && segmentResult.getWord(i).length == 1 && (segmentResult.getPOS(i) != POSUtil.POS_W) && (segmentResult.getPOS(i) != POSUtil.POS_UNKOWN)
    }
    isSingleWord
  }

  def isAllForeignName(begin: Int, end: Int): Boolean = {
    var isForeignName = true
    for (i <- begin to end) {
      isForeignName = isForeignName && NeuralNetworkChineseNameRecognizer.foreignName.isForiegnName(segmentResult.getWord(i))
    }
    isForeignName
  }

  def calculateByNeuralNetwork(begin: Int, end: Int): Int = {
    val feature = featureExtractor.getFeatures(segmentResult, begin, end)
    val input = normalizer.normalize(Matrix(feature))
    val actual = network.computeOutput(input)
//    println(actual)
    if (actual(0, 0) > actual(0, 1) && actual(0, 0) > 0.6)
      end - begin + 1
    else
      -1
  }
}

object NeuralNetworkChineseNameRecognizer {

  val nameEntityRecognizerStatisticResult = new NameEntityRecognizerStatisticResult()
  nameEntityRecognizerStatisticResult.load("cn_name_feature.dat")

  val nameDistribution = new NameProbDistribution()

  val foreignName = new ForeignName()
  foreignName.loadNameWord()

  val network = NeuralNetwork()
  network.add(new Layer(Matrix(7, 6, Array(
    44.63561806120612  , -0.3057653274327397 , 4.254619405430898   , -0.25306486255097693 , -1.7870422744734777 , -2.7833889218535854,
    -2.6349496761437687, -0.8260100050289256 , 1.1071725153275125  , 20.324417048309282   , -3.5978288550420876 , -2.3945207569030735,
    -3.419404006223498 , -3.9437886333049366 , 1.7132749661981994  , -3.386745765192176   , 2.1369591225791726  , -21.308566934814053,
    3.0548383358270796 , -2.2197036754887605 , -4.913094986394171  , 3.5286413994323698   , 2.5309504760707506  , -24.184921569713552,
    -6.927940446777642 , -17.295036856260918 , 2.0252639206879115  , 4.2637059223927345   , -14.215721812677616 , 53.09777424537756  ,
    -1.1927688733868762, 7.480477922535939   , 1.0874207853104525  , 0.21123281033680458  , -21.191598089502524 , -23.247357903941612,
    -35.57740211407981 , 12.81243173901548   , -7.4184731586959884 , -20.123205610679214  , 22.092774937167327  , -22.14872920047769 )),
    Sigmoid.activation))

  network.add(new Layer(Matrix(7, 2, Array(
    -3.91207508391144  , 3.9120750839114375 ,
    -8.357193544412432 , 8.357193544412457  ,
    7.577453137171375  , -7.5774531371713785,
    -5.5137237211661345, 5.513723721166135  ,
    -4.315624585110355 , 4.315624585110359  ,
    -4.33076769290651  , 4.33076769290651   ,
    8.357440761465732  , -8.357440761465755
  )), Sigmoid.activation))

  var errorCount = 0
  var totalCount = 0

  val normalizer = new Normalizer(
    Array(-0.40833712865470995,-0.3687956385749418,-5.947379414241624,-14.519856665016288,-38.20810986099769,-13.210378045650687),
    Array(5.39923590055081,5.359694410471041,5.931437870372603,5.095529607265829,38.16063368541203,9.704275789273217))

  def apply(segmentResult: SegmentResult): NeuralNetworkChineseNameRecognizer = {
    new NeuralNetworkChineseNameRecognizer(
      segmentResult,
      new NERFeatures(nameEntityRecognizerStatisticResult, nameDistribution),
      network,
      normalizer)
  }
}

class NameProbDistribution {

  val loader = SerializeHandler(new File("ner_cn.dat"), SerializeHandler.MODE_READ_ONLY)
  val wordCount = loader.deserializeInt()
  val pinyinFreq = loader.deserializeMapStringInt()
  val wordFreq = loader.deserializeMapStringInt()
  val xingSet = loader.deserializeArrayString().toSet

  def getProbAsName(words: List[String]): Double = {
    val prob = (words.map(word => if (wordFreq.containsKey(word)) wordFreq.get(word) else 1).sum.toDouble) / wordCount.toDouble
    Math.log(prob) / Math.log(2)
  }

  def getPinyinProbAsName(words: List[String]): Double = {
    val pinyinList = WordToPinyinClassfierFactory().getClassifier().classify(words.mkString)
    val prob = (pinyinList.map(pinyin => if (pinyinFreq.containsKey(pinyin)) pinyinFreq.get(pinyin) else pinyinFreq.get("unknown")).sum.toDouble) / wordCount.toDouble
    Math.log(prob) / Math.log(2)
  }
}

class NERFeatures(result: NameEntityRecognizerStatisticResult, nameDistribution: NameProbDistribution) {

  def getFeatures(sentence: SegmentResult, index: Int, end: Int) = {
    val name = {
      val words = for (i <- index to end) yield sentence.getWord(i)
      words.mkString.toList.map(ch => ch.toString)
    }

    val leftBound = if (index == 0) "\0" else sentence.getWord(index - 1)
    val rightBound = if (end + 1 < sentence.length()) sentence.getWord(end + 1) else "\0"
//    println("------" + name)
    List[Double](
      result.leftBoundaryMutualInformation(leftBound),
      result.rightBoundaryMutualInformation(rightBound),
      result.diffLog(name(0)),
      result.probability(name),
      result.conditionProbability(name),
//      result.mutualInformation(name),
      nameDistribution.getProbAsName(name)
    )
  }
}