package me.yingrui.segment.word2vec

import java.io.File
import java.lang.Math._

import me.yingrui.segment.math.Matrix
import me.yingrui.segment.neural.errors.CrossEntropyLoss
import me.yingrui.segment.neural.{BPSigmoidLayer, BackPropagation, NeuralNetwork, SoftmaxLayer}
import me.yingrui.segment.util.SerializeHandler

import scala.collection.mutable.ListBuffer

class MLPSegment {

  val segmentCorpusFile = "lib-segment/training-10000.txt"

  val word2VecModelFile = "vectors.cn.hs.dat"

  val reader = SerializeHandler(new File(word2VecModelFile), SerializeHandler.READ_ONLY)
  val vocab = Vocabulary(reader)
  val word2VecModel = reader.deserialize2DArrayDouble()

  val corpus = new SegmentCorpus(word2VecModel, vocab)
  val trainDataSet = corpus.load(segmentCorpusFile)
  val transitionProb = corpus.buildLabelTransitionProb
  val testDataSet = corpus.loadSegmentDataSet(segmentCorpusFile)

  val numberOfFeatures = word2VecModel(0).length
  val numberOfNeurons = 200
  val numberOfClasses = 4

  def train(maxIteration: Int) = {
    val layer0Weight = Matrix.randomize(numberOfFeatures, numberOfNeurons, -1D, 1D)
    val layer0Bias = Matrix.randomize(1, numberOfNeurons, -1D, 1D)
    val layer1Weight = Matrix.randomize(numberOfNeurons, numberOfClasses, -1D, 1D)

    val loss = new CrossEntropyLoss
    val network = new BackPropagation(numberOfNeurons, numberOfClasses, 0.05, 0.0D, loss)
    network.addLayer(new BPSigmoidLayer(layer0Weight, layer0Bias))
    network.addLayer(SoftmaxLayer(layer1Weight))

    def takeARound(trainSet: Seq[(Matrix, Matrix)]): Double = {
      network.errorCalculator.clear
      var count = 0
      while (count < trainSet.size) {
        val data = trainSet(count)

        val output: Matrix = network.computeOutput(data._1)
        val expectedOutput: Matrix = data._2
        network.computeError(output, expectedOutput)
        network.update()
        count += 1
        if (count % 1000 == 0) {
          print("Progress %2.5f \r".format(count.toDouble / trainSet.size.toDouble))
        }
      }

      network.getLoss
    }

    var iteration = 0
    var cost = 0D
    val costs = new ListBuffer[Double]()
    var lastCost = Double.MaxValue
    var hasImprovement = true
    while (iteration < maxIteration && hasImprovement) {
      val tic = System.currentTimeMillis()
      cost = takeARound(trainDataSet)
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
      val classifier = new MLPSegmentViterbiClassifier(network, transitionProb)
      val result = classifier.classify(document.map(data => (data._1, data._2)))
      val output = result.getBestPath

      val errors = (0 until document.length).map(i => if (expectedOutput(i) == output(i)) 0D else 1D)
      errors.sum
    }).sum
  }
}
