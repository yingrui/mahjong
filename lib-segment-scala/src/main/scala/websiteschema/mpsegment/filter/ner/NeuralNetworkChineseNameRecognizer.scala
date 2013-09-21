package websiteschema.mpsegment.filter.ner

import websiteschema.mpsegment.core.SegmentResult
import websiteschema.mpsegment.filter.{ForeignName, NameEntityRecognizerStatisticResult}
import websiteschema.mpsegment.util.SerializeHandler
import java.io.File
import websiteschema.mpsegment.pinyin.WordToPinyinClassfierFactory
import websiteschema.mpsegment.neural.{Normalizer, Sigmoid, Layer, NeuralNetwork}
import websiteschema.mpsegment.math.Matrix
import websiteschema.mpsegment.dict.POSUtil
import collection.mutable

class NeuralNetworkChineseNameRecognizer(val segmentResult: SegmentResult) extends NameEntityRecognizer {

  val cache = mutable.HashSet[String]()

  def recognizeNameWordBetween(begin: Int, end: Int): NameEntityRecognizeResult = {
    val result = recognizeName(begin, end)
//    if (nameWordLength > 0) {
//      println(for (i <- begin to (begin + nameWordLength - 1)) yield segmentResult.getWord(i))
//    }
    if (result.nameWordCount < 0 && end - begin >= 2) {
      recognizeNameWordBetween(begin, end - 1)
    } else {
      if(result.startWithXing){
        cache += getName(begin, begin + result.nameWordCount - 1)
      }
      result
    }
  }

  private def getName(begin: Int, end: Int) = (for (i <- begin to end) yield segmentResult.getWord(i)).mkString("")

  private def recognizeName(begin: Int, end: Int): NameEntityRecognizeResult = {
    val name = getName(begin, end)
//    println(name)
    val possibleNameWordCount = end - begin + 1
    if (possibleNameWordCount <= 1) {
      new NameEntityRecognizeResult(-1, false, false)
    } else {
      if (possibleNameWordCount > 2 && startWithXing(begin)) {
        if (name.length > 3 && isForeignName(begin, end)){
          new NameEntityRecognizeResult(computeNameWordLength(begin, end), false, true)
        } else {
          val newEnd = begin + 2
          if (isAllSingleWord(begin, newEnd)) {
            new NameEntityRecognizeResult(computeNameWordLength(begin, newEnd), true, false)
          } else new NameEntityRecognizeResult(-1, false, false)
        }
      } else {
        if (matchTypicalShortName(name)){
          new NameEntityRecognizeResult(computeNameWordLength(begin, end), false, false)
        } else if (segmentResult.getWord(end).length <= 2 && startWithXing(begin) && isAllSingleWord(begin, end)) {
          new NameEntityRecognizeResult(computeNameWordLength(begin, end), true, false)
        } else if (isRecognizedMing(begin, end)) {
          new NameEntityRecognizeResult(computeNameWordLength(begin, end), false, false)
        } else if (isForeignName(begin, end)){
          new NameEntityRecognizeResult(computeNameWordLength(begin, end), false, true)
        } else new NameEntityRecognizeResult(-1, false, false)
      }
    }
  }

  private def matchTypicalShortName(name: String): Boolean = {
    name.matches("(阿.|大.|小.|老.|.妹|.姐|.叔|.婶|.哥|.兄|.弟|.子|.嫂|.婆|.公|.伯|.氏|.老)")
  }

  private def isRecognizedMing(begin: Int, end: Int): Boolean = {
    val name = getName(begin, end)
    end > begin && cache.exists(knownName => knownName.endsWith(name))
  }

  private def isForeignName(begin: Int, end: Int) = isAllSingleWord(begin, end) && isAllForeignName(begin, end)

  private def startWithXing(begin: Int): Boolean = isXing(segmentResult.getWord(begin))

  private def isXing(word: String) = NeuralNetworkChineseNameRecognizer.nameDistribution.xingSet.contains(word)

  private def isAllSingleWord(begin: Int, end: Int): Boolean = {
    var isSingleWord = true
    for (i <- begin to end) {
      isSingleWord = isSingleWord && segmentResult.getWord(i).length == 1 && (segmentResult.getPOS(i) != POSUtil.POS_W) && (segmentResult.getPOS(i) != POSUtil.POS_UNKOWN)
    }
    isSingleWord
  }

  private def isAllForeignName(begin: Int, end: Int): Boolean = {
    var isForeignName = true
    for (i <- begin to end) {
      isForeignName = isForeignName && NeuralNetworkChineseNameRecognizer.foreignName.isForeignName(segmentResult.getWord(i))
    }

    if (end - begin <= 2) {
      isForeignName = isForeignName && NeuralNetworkChineseNameRecognizer.foreignName.isForeignNameStartChar(segmentResult.getWord(begin))
    }
    isForeignName
  }

  private def computeNameWordLength(begin: Int, end: Int): Int = {
    val nameWordCount = end - begin + 1
    val classifier = NeuralNetworkChineseNameRecognizer.getClassifier(segmentResult, begin, end)
    val feature = classifier.extractedFeatures
    val result = classifier.classify(feature)
//    println(result + " --- " + Matrix(feature))
    val isNER = isName(result)
    if (isNER) {
      if(nameWordCount < 3) {
        nameWordCount
      } else {
        val classifier2WordName = NeuralNetworkChineseNameRecognizer.getClassifier(segmentResult, begin, begin + 1)
        val feature2WordName = classifier2WordName.extractedFeatures
        val result2WordName = classifier2WordName.classify(feature2WordName)
        //      println(result2WordName + " --- " + classifier2WordName.rightBoundaryFreq)
        val isNameEither = isName(result2WordName)
        if(isNameEither && getError(result) > getError(result2WordName)
          && classifier.rightBoundaryFreq < classifier2WordName.rightBoundaryFreq && classifier2WordName.rightBoundaryFreq > 150) {
          2
        } else {
          nameWordCount
        }
      }
    } else {
      -1
    }
  }

  private def getError(actual: Matrix): Double = Matrix.arithmetic(actual.flatten, Array(1.0, 0.0), (a, i) => Math.pow(i - a, 2D)).sum

  private def isName(result: Matrix) = result(0, 0) > result(0, 1) && result(0, 0) > 0.6

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

  val normalizer = new Normalizer(
    Array(-0.40833712865470995,-0.3687956385749418,-5.947379414241624,-14.519856665016288,-38.20810986099769,-13.210378045650687),
    Array(5.39923590055081,5.359694410471041,5.931437870372603,5.095529607265829,38.16063368541203,9.704275789273217))

  def apply(segmentResult: SegmentResult): NeuralNetworkChineseNameRecognizer = new NeuralNetworkChineseNameRecognizer(segmentResult)

  def getClassifier(sentence: SegmentResult, begin: Int, end: Int) = {
    new NeuralNetworkClassifier(sentence, begin, end, nameEntityRecognizerStatisticResult, nameDistribution, network, normalizer)
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

class NeuralNetworkClassifier(sentence: SegmentResult, index: Int, end: Int, statisticResult: NameEntityRecognizerStatisticResult, nameDistribution: NameProbDistribution, network: NeuralNetwork, normalizer: Normalizer) {

  val name = {
    val words = for (i <- index to end) yield sentence.getWord(i)
    words.mkString.toList.map(ch => ch.toString)
  }

  val leftBound = if (index == 0) "\0" else sentence.getWord(index - 1)
  val rightBound = if (end + 1 < sentence.length()) sentence.getWord(end + 1) else "\0"

  val extractedFeatures = {
//    println("------" + name)
    List[Double](
      statisticResult.leftBoundaryMutualInformation(leftBound),
      statisticResult.rightBoundaryMutualInformation(rightBound),
      statisticResult.diffLog(name(0)),
      statisticResult.probability(name),
      statisticResult.conditionProbability(name),
//      statisticResult.mutualInformation(name),
      nameDistribution.getProbAsName(name)
    )
  }

  def rightBoundaryFreq = statisticResult.freqAsRightBoundary(rightBound)

  def classify(feature: Seq[Double]): Matrix = {
    val input = normalizer.normalize(Matrix(feature))
    network.computeOutput(input)
  }
}