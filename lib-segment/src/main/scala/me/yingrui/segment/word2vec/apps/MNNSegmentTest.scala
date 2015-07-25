package me.yingrui.segment.word2vec.apps

import java.io.File
import java.lang.Math._

import me.yingrui.segment.math.Matrix
import me.yingrui.segment.neural.{NeuralNetwork, SoftmaxLayer}
import me.yingrui.segment.util.SerializeHandler
import me.yingrui.segment.word2vec.{MNNSegmentViterbiClassifier, SegmentCorpus, Vocabulary}

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext

object MNNSegmentTest extends App {

  implicit val executionContext = ExecutionContext.Implicits.global

  val word2VecModelFile = if (args.indexOf("--word2vec-model") >= 0) args(args.indexOf("--word2vec-model") + 1) else "vectors.cn.hs.dat"
  val trainFile = if (args.indexOf("--train-file") >= 0) args(args.indexOf("--train-file") + 1) else "lib-segment/training-10000.txt"
  val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "segment-vector.dat"
  val ngram = if (args.indexOf("-ngram") >= 0) args(args.indexOf("-ngram") + 1).toInt else 2

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
  var transitionProb = Matrix(1, 1)
  val documents = corpus.loadDocuments(trainFile).map(doc => corpus.convertToWordIndexes(doc)).toList

  print("loading models...\r")
  load()

  print("testing...\r")
  displayResult(test(), documents.map(doc => doc.length).sum.toDouble)

  val errorCount = testSegmentCorpus()
  displayResult(errorCount, testDataSet.map(doc => doc.length).sum.toDouble)

  private def load(): Unit = {
    val deserializer = SerializeHandler(new File(saveFile), SerializeHandler.READ_ONLY)
    val size = deserializer.deserializeInt()
    assert(size == networks.size)
    for (i <- 0 until size) {
      networks(i).load(deserializer)
    }
    transitionProb = deserializer.deserializeMatrix()
    deserializer.close()
  }


  private def displayResult(errorCount: Double, numberOfSamples: Double): Unit = {
    val accuracy = 1.0D - errorCount / numberOfSamples
    println("error = " + errorCount + " total = " + numberOfSamples)
    println("accuracy = " + accuracy)
  }

  private def testSegmentCorpus(): Double = {
    var errors = 0D
    testDataSet.foreach(document => {
      val expectedOutput = document.map(data => data._3)
      val inputs = splitByUnknownWords(document)

      val outputs = inputs.map(input => classify(input, networks, transitionProb)).flatten
      assert(outputs.length == expectedOutput.length)
      errors += (0 until document.length).map(i => if (expectedOutput(i) == outputs(i)) 0D else 1D).sum
    })
    errors
  }

  def classify(input: Seq[(Int, Matrix)], networks: Seq[NeuralNetwork], transitionProb: Matrix): Seq[Int] = {
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

  private def initialize(numberOfFeatures: Int, numberOfClasses: Int, size: Int) = for (i <- 0 until size) yield {
    val network = new NeuralNetwork()
    network.add(SoftmaxLayer(Matrix(numberOfFeatures, numberOfClasses)).layer)
    network
  }
}
