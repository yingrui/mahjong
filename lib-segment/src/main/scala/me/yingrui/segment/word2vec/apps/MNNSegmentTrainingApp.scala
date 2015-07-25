package me.yingrui.segment.word2vec.apps

import java.io.File
import java.lang.Math._
import java.nio.file.Files

import me.yingrui.segment.math.Matrix
import me.yingrui.segment.math.Matrix.randomize
import me.yingrui.segment.neural.errors.CrossEntropyLoss
import me.yingrui.segment.neural.{BackPropagation, NeuralNetwork, SoftmaxLayer}
import me.yingrui.segment.util.SerializeHandler
import me.yingrui.segment.word2vec.{MNNSegmentViterbiClassifier, SegmentCorpus, Vocabulary}

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object MNNSegmentTrainingApp extends App {

  implicit val executionContext = ExecutionContext.Implicits.global

  val word2VecModelFile = if (args.indexOf("--word2vec-model") >= 0) args(args.indexOf("--word2vec-model") + 1) else "vectors.cn.hs.dat"
  val trainFile = if (args.indexOf("--train-file") >= 0) args(args.indexOf("--train-file") + 1) else "lib-segment/training-10000.txt"
  val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "segment-vector.dat"
  val ngram = if (args.indexOf("-ngram") >= 0) args(args.indexOf("-ngram") + 1).toInt else 2
  val maxIteration = if (args.indexOf("-iter") >= 0) args(args.indexOf("-iter") + 1).toInt else 25
  val taskCount = if (args.indexOf("-thread") >= 0) args(args.indexOf("-thread") + 1).toInt else Runtime.getRuntime().availableProcessors()

  print("loading word2vec model...\r")
  val reader = SerializeHandler(new File(word2VecModelFile), SerializeHandler.READ_ONLY)
  val vocab = Vocabulary(reader)
  val word2VecModel = reader.deserialize2DArrayDouble()
  assert(vocab.size == word2VecModel.length, "vocab size is not equal to word2vec model size")
  val numberOfFeatures = word2VecModel(0).length
  val numberOfClasses = Math.pow(4, ngram).toInt
  val networks = initialize(numberOfFeatures, numberOfClasses, vocab.size)

  print("loading training corpus...\r")
  val corpus = new SegmentCorpus(word2VecModel, vocab, ngram)

  val testDataSet = corpus.loadSegmentDataSet(trainFile)
  val transitionProb = corpus.getLabelTransitionProb(trainFile)

  val documents = corpus.loadDocuments(trainFile).map(doc => corpus.convertToWordIndexes(doc)).toList
  val groups = documents.grouped(documents.size / taskCount).toList

  var iteration = 0
  var cost = 0D
  val costs = new ListBuffer[Double]()
  var lastCost = Double.MaxValue
  var hasImprovement = true
  while (shouldContinue && iteration < maxIteration && hasImprovement) {
    val tic = System.currentTimeMillis()
    cost = takeARound(iteration)
    val toc = System.currentTimeMillis()
    costs += cost
    val averageCost = costs.takeRight(20).sum / costs.takeRight(20).size.toDouble
    println("Iteration: %2d cost: %2.5f average cost: %2.5f elapse: %ds".format(iteration, cost, averageCost, (toc - tic) / 1000))
    hasImprovement = (lastCost - averageCost) > 1e-5

    lastCost = averageCost
    iteration += 1
  }

  println("testing...")
  displayResult(test(), documents.map(doc => doc.length).sum.toDouble)


  val errorCount = testSegmentCorpus()
  displayResult(errorCount, testDataSet.map(doc => doc.length).sum.toDouble)

  println("saving...")
  saveModel()

  private def displayResult(errorCount: Double, numberOfSamples: Double): Unit = {
    val accuracy = 1.0D - errorCount / numberOfSamples
    println("error = " + errorCount + " total = " + numberOfSamples)
    println("accuracy = " + accuracy)
  }

  private def testSegmentCorpus(): Double = {
    val neuralNetworks = networks.map(network => network.getNetwork)
    testDataSet.map(document => {
      val expectedOutput = document.map(data => data._3)
      val inputs = splitByUnknownWords(document)

      val outputs = inputs.map(input => classify(input, neuralNetworks)).flatten
      assert(outputs.length == expectedOutput.length)
      val errors = (0 until document.length).map(i => if (expectedOutput(i) == outputs(i)) 0D else 1D)
      errors.sum
    }).sum
  }

  def classify(input: Seq[(Int, Matrix)], networks: Seq[NeuralNetwork]): Seq[Int] = {
    if (input.forall(data => data._1 <= 0)) {
      input.map(ele => 0)
    } else {
      val classifier = new MNNSegmentViterbiClassifier(networks, transitionProb, ngram)
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

  private def saveModel(): Unit = {
    val dumper = SerializeHandler(new File(saveFile), SerializeHandler.WRITE_ONLY)
    dumper.serializeInt(networks.size)
    for (network <- networks) {
      network.getNetwork.save(dumper)
    }
    dumper.serializeMatrix(transitionProb)
    dumper.close()
  }

  private def test(): Double = {
    (for (document <- documents; data <- document) yield {
      val wordIndex = data._2
      val network = networks(wordIndex)
      val input = corpus.convertToMatrix(data._1)
      val output = classify(network, input)
      val expectedOutput = data._3
      if ((expectedOutput - output).map(abs(_)).sum > 0)
        1.0D
      else
        0.0D
    }).sum
  }

  def classify(classifier: BackPropagation, input: Matrix): Matrix = {
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

  private def takeARound(currentIteration: Int): Double = {
    networks.foreach(network => network.errorCalculator.clear)

    val tasks = for (group <- groups) yield {
      Future {
        for (document <- group; data <- document) {
          val wordIndex = data._2
          val network = networks(wordIndex)
          val input = corpus.convertToMatrix(data._1)
          val output = network.computeOutput(input)
          network.computeError(output, data._3)
          network.update()
        }
      }
    }

    tasks.foreach(f => Await.result(f, Duration.Inf))
    networks.map(network => network.getLoss).sum
  }

  private def initialize(numberOfFeatures: Int, numberOfClasses: Int, size: Int) = for (i <- 0 until size) yield {
    val layerWeight = randomize(numberOfFeatures, numberOfClasses, -1D, 1D)

    val loss = new CrossEntropyLoss
    val network = new BackPropagation(numberOfFeatures, numberOfClasses, 0.1D, 0.0D, loss)
    network.addLayer(SoftmaxLayer(layerWeight))
    network
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
}
