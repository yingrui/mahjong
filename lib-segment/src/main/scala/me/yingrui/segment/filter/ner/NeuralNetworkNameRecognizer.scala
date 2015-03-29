package me.yingrui.segment.filter.ner

import me.yingrui.segment.core.SegmentResult
import me.yingrui.segment.filter.{ForeignName, NameEntityRecognizerStatisticResult}
import me.yingrui.segment.neural._
import me.yingrui.segment.math.Matrix
import me.yingrui.segment.dict.POSUtil
import collection.mutable
import me.yingrui.segment.util.WordUtil

trait RecognizerCreator {
  def create(segmentResult: SegmentResult): NameEntityRecognizer
}

class ChineseNameRecognizerCreator extends RecognizerCreator {

  def create(segmentResult: SegmentResult): NameEntityRecognizer = NeuralNetworkNameRecognizer(segmentResult)

}

class ForeignNameRecognizerCreator extends RecognizerCreator {

  def create(segmentResult: SegmentResult): NameEntityRecognizer = NeuralNetworkNameRecognizer(segmentResult)

}

class NeuralNetworkNameRecognizer(val segmentResult: SegmentResult) extends NameEntityRecognizer {

  val cache = mutable.HashSet[String]()

  def recognizeNameWordBetween(begin: Int, end: Int): NameEntityRecognizeResult = {
    val result = recognizeName(begin, end)
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
      if (possibleNameWordCount > 3) {
        computeForeignName(possibleNameWordCount, begin, end, name)
      } else {
        compute(possibleNameWordCount, begin, end, name)
      }
    }
  }

  def computeForeignName(possibleNameWordCount: Int, begin: Int, end: Int, name: String): NameEntityRecognizeResult = {
    if (isForeignName(begin, end)) {
      new NameEntityRecognizeResult(computeNameWordLength(begin, end, true), false, true)
    } else new NameEntityRecognizeResult(-1, false, false)
  }

  def compute(possibleNameWordCount: Int, begin: Int, end: Int, name: String): NameEntityRecognizeResult = {
    if (possibleNameWordCount > 2 && startWithXing(begin)) {
      val newEnd = begin + 2
      if (isAllSingleWord(begin, newEnd)) {
        new NameEntityRecognizeResult(computeNameWordLength(begin, newEnd, false), true, false)
      } else new NameEntityRecognizeResult(-1, false, false)
    } else {
      if (matchTypicalShortName(name)) {
        new NameEntityRecognizeResult(computeNameWordLength(begin, end, false), false, false)
      } else if (matchTypicalChineseName(name)) {
        new NameEntityRecognizeResult(computeNameWordLength(begin, end, false), true, false)
      } else if (segmentResult.getWord(end).length <= 2 && startWithXing(begin) && isAllSingleWord(begin, end)) {
        new NameEntityRecognizeResult(computeNameWordLength(begin, end, false), true, false)
      } else if (isRecognizedMing(begin, end)) {
        new NameEntityRecognizeResult(computeNameWordLength(begin, end, false), false, false)
      } else if (isForeignName(begin, end)) {
        new NameEntityRecognizeResult(computeNameWordLength(begin, end, true), false, true)
      } else new NameEntityRecognizeResult(-1, false, false)
    }
  }

  private def matchTypicalShortName(name: String): Boolean = (
    name.matches("(阿.|大.|.妹|.姐|.叔|.婶|.哥|.兄|.弟|.嫂|.婆|.公|.伯)")
    || (name.length == 2 && isXing(name.charAt(0).toString) && name.matches("(.子|.氏|.老|.总|.局|.工|.某)"))
    || (name.length == 2 && isXing(name.charAt(1).toString) && name.matches("(小.|老.)"))
  )

  private def matchTypicalChineseName(name: String): Boolean =
    (name.length == 3 && isXing(name.charAt(0).toString) && name.matches(".(.)\\1"))

  private def isRecognizedMing(begin: Int, end: Int): Boolean = {
    val name = getName(begin, end)
    end > begin && cache.exists(knownName => knownName.endsWith(name))
  }

  private def isForeignName(begin: Int, end: Int) = isAllSingleWord(begin, end) && isAllForeignName(begin, end)

  private def startWithXing(begin: Int): Boolean = isXing(segmentResult.getWord(begin))

  private def isXing(word: String) = NeuralNetworkNameRecognizer.nameDistribution.xingSet.contains(word)

  private def isAllSingleWord(begin: Int, end: Int): Boolean = {
    var isSingleWord = true
    for (i <- begin to end) {
      isSingleWord = isSingleWord && segmentResult.getWord(i).length == 1 && (segmentResult.getPOS(i) != POSUtil.POS_W || (segmentResult.getWord(i).equals("·") && i > begin && i < end)) && (segmentResult.getPOS(i) != POSUtil.POS_UNKOWN)
    }
    isSingleWord
  }

  private def isAllForeignName(begin: Int, end: Int): Boolean = {
    var isForeignName = !WordUtil.isPos_P_C_U_W_UN(segmentResult.getPOS(begin), segmentResult.getWord(begin))
    for (i <- begin to end) {
      isForeignName = isForeignName && NeuralNetworkNameRecognizer.foreignName.isForeignName(segmentResult.getWord(i))
    }

    if (end - begin <= 2) {
      isForeignName = isForeignName && NeuralNetworkNameRecognizer.foreignName.isForeignNameStartChar(segmentResult.getWord(begin))
    }
    isForeignName
  }

  private def computeNameWordLength(begin: Int, end: Int, recognizeForeignName: Boolean): Int = {
    var nameWordCount = end - begin + 1
    val classifier = NeuralNetworkNameRecognizer.getClassifier(segmentResult, begin, end)
    val feature = classifier.extractedFeatures
    val result = classifier.classify(feature)
//    println(result + " --- " + Matrix(feature))
    val isNER = isName(result)
    if (isNER && !recognizeForeignName) {

      if(nameWordCount == 3) {
        val classifier2WordName = NeuralNetworkNameRecognizer.getClassifier(segmentResult, begin, begin + 1)
        val feature2WordName = classifier2WordName.extractedFeatures
        val result2WordName = classifier2WordName.classify(feature2WordName)
//        println(result2WordName + " --- " + Matrix(feature2WordName))
        val isNameEither = isName(result2WordName)
        if(isNameEither && getError(result) > getError(result2WordName)) {
          val isEndBoundaryWord = classifier.rightBoundaryFreq < classifier2WordName.rightBoundaryFreq && classifier2WordName.rightBoundaryFreq > 150
          val isEndConjunctionWord = WordUtil.isPos_P_C_U_W_UN(segmentResult.getPOS(end), segmentResult.getWord(end))
          if(isEndBoundaryWord || isEndConjunctionWord) {
            nameWordCount = 2
          }
        }
      }

      if (nameWordCount == 3) {
        if (isXing(segmentResult.getWord(end))) {
          if (computeNameWord(segmentResult, end, end + 1) || computeNameWord(segmentResult, end, end + 2)) {
            nameWordCount = 2
          }
        }
      }

    } else if (!isNER) {
      nameWordCount = -1
    }

    nameWordCount
  }

  private def computeNameWord(sentence: SegmentResult, begin: Int, end: Int): Boolean = {
    if (end < sentence.length() && isAllSingleWord(begin, end)) {
      val classifier = NeuralNetworkNameRecognizer.getClassifier(segmentResult, begin, end)
      val feature = classifier.extractedFeatures
      val result = classifier.classify(feature)
      isName(result) && getError(result) < 1E-4
    } else {
      false
    }
  }

  private def getError(actual: Matrix): Double = Matrix.arithmetic(actual.flatten, Array(1.0, 0.0), (a, i) => Math.pow(i - a, 2D)).sum

  private def isName(result: Matrix) = result(0, 0) > result(0, 1) && result(0, 0) > 0.6

}

object NeuralNetworkNameRecognizer {

  val nameEntityRecognizerStatisticResult = new NameEntityRecognizerStatisticResult()
  nameEntityRecognizerStatisticResult.load("cn_name_feature.dat")

  val nameDistribution = new NameProbDistribution()

  val foreignName = new ForeignName()
  foreignName.loadNameWord()

  val network = NeuralNetwork()
  network.add(SigmoidLayer(Matrix(7, 6, Array(
    44.63561806120612  , -0.3057653274327397 , 4.254619405430898   , -0.25306486255097693 , -1.7870422744734777 , -2.7833889218535854,
    -2.6349496761437687, -0.8260100050289256 , 1.1071725153275125  , 20.324417048309282   , -3.5978288550420876 , -2.3945207569030735,
    -3.419404006223498 , -3.9437886333049366 , 1.7132749661981994  , -3.386745765192176   , 2.1369591225791726  , -21.308566934814053,
    3.0548383358270796 , -2.2197036754887605 , -4.913094986394171  , 3.5286413994323698   , 2.5309504760707506  , -24.184921569713552,
    -6.927940446777642 , -17.295036856260918 , 2.0252639206879115  , 4.2637059223927345   , -14.215721812677616 , 53.09777424537756  ,
    -1.1927688733868762, 7.480477922535939   , 1.0874207853104525  , 0.21123281033680458  , -21.191598089502524 , -23.247357903941612,
    -35.57740211407981 , 12.81243173901548   , -7.4184731586959884 , -20.123205610679214  , 22.092774937167327  , -22.14872920047769 ))))

  network.add(SigmoidLayer(Matrix(7, 2, Array(
    -3.91207508391144  , 3.9120750839114375 ,
    -8.357193544412432 , 8.357193544412457  ,
    7.577453137171375  , -7.5774531371713785,
    -5.5137237211661345, 5.513723721166135  ,
    -4.315624585110355 , 4.315624585110359  ,
    -4.33076769290651  , 4.33076769290651   ,
    8.357440761465732  , -8.357440761465755))))

  val normalizer = new Normalizer(
    Array(-0.40833712865470995,-0.3687956385749418,-5.947379414241624,-14.519856665016288,-38.20810986099769,-13.210378045650687),
    Array(5.39923590055081,5.359694410471041,5.931437870372603,5.095529607265829,38.16063368541203,9.704275789273217))

  def apply(segmentResult: SegmentResult) = new NeuralNetworkNameRecognizer(segmentResult)

  def getClassifier(sentence: SegmentResult, begin: Int, end: Int) = {
    new NeuralNetworkClassifier(sentence, begin, end, nameEntityRecognizerStatisticResult, nameDistribution, network, normalizer)
  }
}