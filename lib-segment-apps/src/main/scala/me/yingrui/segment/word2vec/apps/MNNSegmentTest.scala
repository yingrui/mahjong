package me.yingrui.segment.word2vec.apps

import java.io.{BufferedReader, File, InputStreamReader}
import java.lang.Math._

import me.yingrui.segment.math.Matrix
import me.yingrui.segment.neural.{NeuralNetwork, SoftmaxLayer}
import me.yingrui.segment.util.SerializeHandler
import me.yingrui.segment.word2vec.{MNNSegmentViterbiClassifier, NeuralNetworkSegment, SegmentCorpus, Vocabulary}

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext

object MNNSegmentTest extends App {

  implicit val executionContext = ExecutionContext.Implicits.global

  val word2VecModelFile = if (args.indexOf("--word2vec-model") >= 0) args(args.indexOf("--word2vec-model") + 1) else "vectors.cn.hs.dat"
  val trainFile = if (args.indexOf("--train-file") >= 0) args(args.indexOf("--train-file") + 1) else "lib-segment/training-10000.txt"
  val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "segment-vector.dat"
  val skipSelf = if (args.indexOf("-skip-self") >= 0) args(args.indexOf("-skip-self") + 1).toBoolean else true
  val ngram = if (args.indexOf("-ngram") >= 0) args(args.indexOf("-ngram") + 1).toInt else 2

  print("loading word2vec model...\r")
  val reader = SerializeHandler(new File(word2VecModelFile), SerializeHandler.READ_ONLY)
  val vocab = Vocabulary(reader)
  val word2VecModel = reader.deserialize2DArrayDouble()
  assert(vocab.size == word2VecModel.length, "vocab size is not equal to word2vec model size")
  val numberOfFeatures = word2VecModel(0).length
  val numberOfClasses = Math.pow(4, ngram).toInt

  print("loading training corpus...\r")
  val corpus = new SegmentCorpus(word2VecModel, vocab, ngram, ngram)

  print("loading models...\r")
  val (networks, transitionProb) = load(saveFile)

  print("testing...\r")
  displayResult(test(trainFile))
  displayResult(testSegmentCorpus(trainFile, networks))

  println("\nType QUIT to exit:")
  val inputReader = new BufferedReader(new InputStreamReader(System.in))
  var line = inputReader.readLine()
  val neuralNetworkSegment = new NeuralNetworkSegment(word2VecModel, vocab, networks, transitionProb)
  while (line != null && !line.equals("QUIT")) {
    val result = neuralNetworkSegment.segment(line)
    println(result)
    line = inputReader.readLine()
  }

  private def load(saveFile: String): (IndexedSeq[NeuralNetwork], Matrix) = {
    val deserializer = SerializeHandler(new File(saveFile), SerializeHandler.READ_ONLY)
    val networks = initialize(numberOfFeatures, numberOfClasses, vocab.size)
    val size = deserializer.deserializeInt()
    assert(size == networks.size)
    for (i <- 0 until size) {
      networks(i).load(deserializer)
    }
    val transitionProb = deserializer.deserializeMatrix()
    deserializer.close()
    (networks, transitionProb)
  }

  private def displayResult(result: (Double, Double)): Unit = result match {
    case (errorCount, numberOfSamples) => {
      val accuracy = 1.0D - errorCount / numberOfSamples
      println("error = " + errorCount + " total = " + numberOfSamples)
      println("accuracy = " + accuracy)
    }
    case _ =>
  }

  def testSegmentCorpus(file: String, networks: Seq[NeuralNetwork]): (Double, Double) = {
    var errors = 0.0
    var total = 0.0
    corpus.foreachDocuments(file) { data =>
      val document = corpus.convertToSegmentDataSet(data, skipSelf)
      val expectedOutputs = document.map(_._3)
      val inputs = splitByUnknownWords(document)

      val outputs = inputs.map(input => classify(input, networks)).flatten
      assert(outputs.length == expectedOutputs.length)
      for (i <- 0 until document.length) {
        total += 1D
        if (expectedOutputs(i) != outputs(i)) errors += 1D
      }
    }
    (errors, total)
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

  private def test(file: String): (Double, Double) = {
    var errors = 0.0
    var total = 0.0
    corpus.foreachDocuments(file) { document =>
      val wordIndexesAndLabelIndexes = corpus.getWordIndexesAndLabelIndexes(document)

      for (position <- 0 until wordIndexesAndLabelIndexes.length) {
        total += 1.0
        val wordIndex = wordIndexesAndLabelIndexes(position)._1
        val expectedOutput = corpus.getOutputMatrix(wordIndexesAndLabelIndexes, position)
        val input = corpus.convertToMatrix(corpus.getContextWords(wordIndexesAndLabelIndexes, position, skipSelf))
        val network = networks(wordIndex)
        val output = classify(network, input)
        if ((expectedOutput - output).map(abs(_)).sum > 0)
          errors += 1.0D
        else
          0.0D
      }
    }
    (errors, total)
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
