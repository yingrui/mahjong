package me.yingrui.segment.word2vec

import java.io.File
import java.lang.Math._
import java.nio.file.Files

import me.yingrui.segment.math.Matrix
import me.yingrui.segment.neural.errors.CrossEntropyLoss
import me.yingrui.segment.neural.{BPSigmoidLayer, BackPropagation, NeuralNetwork, SoftmaxLayer}
import me.yingrui.segment.util.SerializeHandler

import scala.collection.mutable.ListBuffer

class MLPSegment(val segmentCorpusFile: String, val word2VecModelFile: String, val ngram: Int = 1) {

  val reader = SerializeHandler(new File(word2VecModelFile), SerializeHandler.READ_ONLY)
  val vocab = Vocabulary(reader)
  val word2VecModel = reader.deserialize2DArrayDouble()

  val corpus = new SegmentCorpus(word2VecModel, vocab, ngram)
  val trainDataSet = corpus.load(segmentCorpusFile)
  val transitionProb = corpus.buildLabelTransitionProb
  val testDataSet = corpus.loadSegmentDataSet(segmentCorpusFile)

  val numberOfFeatures = word2VecModel(0).length
  val numberOfNeurons = 200
  val numberOfClasses = Math.pow(4, ngram).toInt

  def train(maxIteration: Int) = {
    val layer0Weight = Matrix.randomize(numberOfFeatures, numberOfNeurons, -1D, 1D)
    val layer0Bias = Matrix.randomize(1, numberOfNeurons, -1D, 1D)
    val layer1Weight = Matrix.randomize(numberOfNeurons, numberOfClasses, -1D, 1D)

    val loss = new CrossEntropyLoss
    val alpha = 0.05
    val network = new BackPropagation(numberOfNeurons, numberOfClasses, alpha, 0.0D, loss)
    network.addLayer(new BPSigmoidLayer(layer0Weight, layer0Bias))
    network.addLayer(SoftmaxLayer(layer1Weight))

    def takeARound(trainSet: Seq[(Matrix, Matrix)], currentIteration: Int): Double = {
      network.errorCalculator.clear
      var count = 0
      val rate = amendAlpha(alpha, currentIteration, maxIteration)
      while (count < trainSet.size) {
        val data = trainSet(count)

        val output: Matrix = network.computeOutput(data._1)
        val expectedOutput: Matrix = data._2
        network.computeError(output, expectedOutput)
        network.update(rate)
        count += 1
        if (count % 1000 == 0) {
          val progress = count.toDouble / trainSet.size.toDouble
          print("Progress %2.5f Alpha %2.5f\r".format(progress, rate))
        }
      }

      network.getLoss
    }

    def amendAlpha(startAlpha: Double, currentIteration: Int, maxIteration: Int): Double = {
      val minRate = 1e-2
      val alpha = startAlpha - ((currentIteration.toDouble + 1D) / (maxIteration.toDouble + 1D)) * (startAlpha - minRate)
      if (alpha < minRate)
        minRate
      else if (alpha >= startAlpha)
        startAlpha
      else
        alpha
    }

    var iteration = 0
    var cost = 0D
    val costs = new ListBuffer[Double]()
    var lastCost = Double.MaxValue
    var hasImprovement = true
    while (shouldContinue && iteration < maxIteration && hasImprovement) {
      val tic = System.currentTimeMillis()
      cost = takeARound(trainDataSet, iteration)
      val toc = System.currentTimeMillis()
      costs += cost
      val averageCost = costs.takeRight(20).sum / costs.takeRight(20).size.toDouble
      println("Iteration: %2d cost: %2.5f average cost: %2.5f elapse: %ds".format(iteration, cost, averageCost, (toc - tic) / 1000))
      hasImprovement = (lastCost - averageCost) > 1e-5

      lastCost = averageCost
      iteration += 1
    }

    network.getNetwork
  }

  def shouldContinue: Boolean = {
    val tmpFile = new File("stop-training.tmp")
    if (Files.exists(tmpFile.toPath)) {
      tmpFile.delete()
      false
    } else {
      true
    }
  }

  def classify(classifier: NeuralNetwork, input: Matrix): Matrix = {
    val actualOutput = classifier computeOutput input
    var maxIndex = 0
    var maxValue = 0D
    for (i <- 0 until actualOutput.col) {
      if (actualOutput(0, i) > maxValue) {
        maxValue = actualOutput(0, i)
        maxIndex = i
      }
    }

    for (i <- 0 until actualOutput.col) {
      actualOutput(0, i) = if (i == maxIndex) 1D else 0D
    }

    actualOutput
  }

  def trainAndTest(maxIteration: Int) = {
    val tic = System.currentTimeMillis()
    println("train set contains " + trainDataSet.size + " samples")

    val classifier = train(maxIteration)

    val error = test(classifier)

    val numberOfSamples = trainDataSet.size.toDouble
    val accuracy = 1.0 - error / numberOfSamples

    println("error = " + error + " total = " + numberOfSamples)
    println("accuracy = " + accuracy)
    val toc = System.currentTimeMillis()
    val second = (toc - tic) / 1000
    println(s"elapsed time: ${second}s")
    classifier
  }

  def test(classifier: NeuralNetwork): Double = {
    trainDataSet.map(data => {
      val testInput = data._1
      val expectedOutput = data._2

      val actualOutput: Matrix = classify(classifier, testInput)

      if ((expectedOutput - actualOutput).map(abs(_)).sum > 0)
        1.0D
      else
        0.0D
    }).sum
  }

  def testSegmentCorpus(network: NeuralNetwork): Double = {
    testDataSet.map(document => {
      val expectedOutput = document.map(data => data._3)
      val inputs = splitByUnknownWords(document)

      val outputs = inputs.map(input => classify(input, network)).flatten
      assert(outputs.length == expectedOutput.length)
      val errors = (0 until document.length).map(i => if (expectedOutput(i) == outputs(i)) 0D else 1D)
      errors.sum
    }).sum
  }

  def classify(input: Seq[(Int, Matrix)], network: NeuralNetwork): Seq[Int] = {
    if (input.forall(data => data._1 <= 0)) {
      input.map(ele => 0)
    } else {
      val classifier = new MLPSegmentViterbiClassifier(network, transitionProb, ngram)
      val result = classifier.classify(input)
      result.getBestPath
    }
  }

  def splitByUnknownWords(document: Seq[(Int, Matrix, Int)]): Seq[Seq[(Int, Matrix)]] = {
    val inputs = document.map(data => (data._1, data._2))
    var start = 0
    var unknownWordIndex = inputs.indexWhere(input => input._1 <= 0, start)
    val result = ListBuffer[Seq[(Int, Matrix)]]()
    while (start < inputs.length) {
      if (unknownWordIndex < 0) {
        result += inputs.slice(start, inputs.length)
        start = inputs.length
      } else {
        if (start < unknownWordIndex) result += inputs.slice(start, unknownWordIndex)

        result += inputs.slice(unknownWordIndex, unknownWordIndex + 1)
        start = unknownWordIndex + 1
        unknownWordIndex = inputs.indexWhere(input => input._1 <= 0, start)
      }
    }
    result
  }
}
