package me.yingrui.segment.word2vec

import java.io.{File, FileInputStream, InputStreamReader}
import java.lang.Math._

import me.yingrui.segment.math.Matrix
import me.yingrui.segment.neural.{BPSigmoidLayer, SoftmaxLayer, BPSimpleRecurrentLayer, BackPropagation}
import me.yingrui.segment.neural.errors.{RMSLoss, CrossEntropyLoss}
import me.yingrui.segment.util.SerializeHandler
import me.yingrui.segment.util.Logger._

object Word2VecTrainingApp extends App {

  println("WORD VECTOR estimation toolkit")
  private val trainFile = if (args.indexOf("--train-file") >= 0) args(args.indexOf("--train-file") + 1) else "text8.txt"
  private val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "vectors.dat"
  private val vecSize = if (args.indexOf("-size") >= 0) args(args.indexOf("-size") + 1).toInt else 100
  private val window = if (args.indexOf("-window") >= 0) args(args.indexOf("-window") + 1).toInt else 5

  private def readVocabulary = {
    val reader = new InputStreamReader(new FileInputStream(trainFile))
    val wordReader = new WordReader(reader, window)
    val vocab = Vocabulary(wordReader)
    reader.close()
    vocab
  }
  private val vocab = readVocabulary
  println(s"Vocabulary has ${vocab.size} words and total word count is ${vocab.getTotalWordCount}")

  private val word2VecBuilder = new Word2VecTrainingNetworkBuilder(vocab, vecSize)
  private val network = word2VecBuilder.initNetwork

  def takeARound(): Double = {
    network.errorCalculator.clear
    val reader = new InputStreamReader(new FileInputStream(trainFile))

    val wordReader = new WordReader(reader, window)
    var words = wordReader.readWindow()
    while (!words.isEmpty) {
      val input = Matrix(1, vocab.size)
      val expectedOutput = Matrix(1, vocab.size)
      words.foreach(w => input(0, vocab.getIndex(w)) = 1D)
      expectedOutput(0, vocab.getIndex(words(window))) = 1D

      val output = network.computeOutput(input)
      network.computeError(output, expectedOutput)
      network.update()

      words = wordReader.readWindow()
    }

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

  network.getNetwork

  private val writer = SerializeHandler(new File(saveFile), SerializeHandler.WRITE_ONLY)
  vocab.save(writer)
  writer.serializeMatrix(network.getNetwork.layers.head.weight)
}

class Word2VecTrainingNetworkBuilder(val vocab: Vocabulary, val vecSize: Int) {

  val loss = new CrossEntropyLoss
//  val loss = new RMSLoss
  val network = new BackPropagation(vocab.size, vocab.size, 0.1D, 0.0D, loss)

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
}