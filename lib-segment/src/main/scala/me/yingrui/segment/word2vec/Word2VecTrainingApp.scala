package me.yingrui.segment.word2vec

import java.io.{File, FileInputStream, InputStreamReader}

import me.yingrui.segment.math.Matrix
import me.yingrui.segment.neural.errors.CrossEntropyLoss
import me.yingrui.segment.neural.{BPSimpleRecurrentLayer, BackPropagation, SoftmaxLayer}
import me.yingrui.segment.util.Logger._
import me.yingrui.segment.util.SerializeHandler

import scala.util.Random
import Math.abs

object Word2VecTrainingApp extends App {

  println("WORD VECTOR estimation toolkit")
  private val trainFile = if (args.indexOf("--train-file") >= 0) args(args.indexOf("--train-file") + 1) else "text8"
  private val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "vectors.dat"
  private val vecSize = if (args.indexOf("-size") >= 0) args(args.indexOf("-size") + 1).toInt else 100
  private val window = if (args.indexOf("-window") >= 0) args(args.indexOf("-window") + 1).toInt else 5

  private def readVocabulary = {
    val reader = new InputStreamReader(new FileInputStream(trainFile))
    val wordReader = new WordReader(reader, window)
    val vocab = Vocabulary(wordReader)
    reader.close()
    vocab.rebuild(5)
    vocab
  }

  private val vocab = readVocabulary
  println(s"Vocabulary has ${vocab.size} words and total word count is ${vocab.getTotalWordCount}")

  //  private val network: Word2VecNetwork = new Word2VecTrainingNetworkBuilder(vocab, vecSize).buildNetwork
  private val network: Word2VecNetwork = BagOfWordNetwork(vocab.size, vecSize)

  val random = new Random()

  def takeARound(): Double = {
    network.clearError
    val reader = new InputStreamReader(new FileInputStream(trainFile))

    val wordReader = new WordReader(reader, window)
    var words = wordReader.readWindow()
    var count = 0
    while (!words.isEmpty) {
      val wordIndex = vocab.getIndex(words(window))
      if (wordIndex > 0) {
        val input = words.map(w => vocab.getIndex(w))
        val output = new Array[(Int, Int)](25)
        output(0) = (wordIndex, 1)
        for (i <- 1 until 25) {
          var index = random.nextInt(vocab.size)
          if (index == wordIndex) index = random.nextInt(vocab.size)
          output(i) = (index, 0)
        }

        network.learn(input.toArray, output)

        count += 1
        if (count % 1000 == 0) print(s"progress: $count/${vocab.getTotalWordCount}\r")
      }
      words = wordReader.readWindow()
    }
    println()
    reader.close()
    network.getLoss
  }

  var iteration = 0
  var cost = 0D
  var lastCost = 0.0D
  var hasImprovement = true
  enableConsoleOutput
  while (iteration < 25 && hasImprovement) {
    cost = takeARound()
    debug(s"iter: ${iteration} cost: ${cost}")
    hasImprovement = abs(cost - lastCost) > 1e-5

    lastCost = cost
    iteration += 1
  }

  private val writer = SerializeHandler(new File(saveFile), SerializeHandler.WRITE_ONLY)
  vocab.save(writer)
  writer.serialize2DArrayDouble(network.wordVector)
}

class Word2VecTrainingNetworkBuilder(val vocab: Vocabulary, val vecSize: Int) {

  val ALPHA = 0.1D
  val loss = new CrossEntropyLoss
  //  val loss = new RMSLoss
  val network = new BackPropagation(vocab.size, vocab.size, ALPHA, 0.0D, loss)

  def initNetwork: BackPropagation = {
    val layer0Weight = Matrix.randomize(vocab.size, vecSize)
    val layer0Bias = Matrix.randomize(1, vecSize)
    val layer1Weight = Matrix.randomize(vecSize, vocab.size)
    val layer1Bias = Matrix.randomize(1, vocab.size)
    val recurrentHiddenLayer = new BPSimpleRecurrentLayer(layer0Weight, layer0Bias)
    //    val outputLayer = new BPSigmoidLayer(layer1Weight, layer1Bias)
    val outputLayer = SoftmaxLayer(layer1Weight)

    network.addLayer(recurrentHiddenLayer)
    network.addLayer(outputLayer)
    network
  }

  def buildNetwork = {
    class SimpleWord2VecNetwork extends Word2VecNetwork {
      val alpha = ALPHA
      val network = initNetwork
      val wordsCount = vocab.size
      val size = vecSize
      val wordVector = matrixTo2DArray(network.getNetwork.layers.head.weight)

      def learn(input: Array[Int], output: Array[(Int, Int)]): Unit = {
        val in = Matrix(1, vocab.size)
        val expectedOutput = Matrix(1, vocab.size)

        input.foreach(w => in(0, w) = 1D)
        output.foreach(t => expectedOutput(0, t._1) = t._2.toDouble)

        val out = network.computeOutput(in)
        network.computeError(out, expectedOutput)
        network.update()
      }

      def clearError = loss.clear

      def getLoss = loss.loss

      private def matrixTo2DArray(m: Matrix): Array[Array[Double]] = {
        val arrays = new Array[Array[Double]](m.row)
        for (i <- 0 until m.row) {
          arrays(i) = m.row(i).flatten
        }
        arrays
      }
    }

    new SimpleWord2VecNetwork()
  }
}