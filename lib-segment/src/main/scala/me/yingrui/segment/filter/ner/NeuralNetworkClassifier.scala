package me.yingrui.segment.filter.ner

import me.yingrui.segment.core.SegmentResult
import me.yingrui.segment.filter.NameEntityRecognizerStatisticResult
import me.yingrui.segment.math.Matrix
import me.yingrui.segment.neural.{Normalizer, NeuralNetwork}
import me.yingrui.segment.dict.POSUtil

class NeuralNetworkClassifier(sentence: SegmentResult, index: Int, end: Int, statisticResult: NameEntityRecognizerStatisticResult, nameDistribution: NameProbDistribution, network: NeuralNetwork, normalizer: Normalizer) {

  val name = {
    val words = for (i <- index to end) yield sentence.getWord(i)
    words.mkString.toList.map(ch => ch.toString)
  }

  val leftBound =   if (index == 0 || sentence.getPOS(index - 1) == POSUtil.POS_W)
                      "\0"
                    else sentence.getWord(index - 1)

  val rightBound =  if (end + 1 < sentence.length() && sentence.getPOS(end + 1) != POSUtil.POS_W)
                      sentence.getWord(end + 1)
                    else "\0"

  val extractedFeatures = {
//    println("------" + name + " " + leftBound + " " + rightBound)
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
  def rightBoundary = statisticResult.rightBoundary(rightBound)

  def classify(feature: Seq[Double]): Matrix = {
    val input = normalizer.normalize(Matrix(feature))
    network.computeOutput(input)
  }
}
