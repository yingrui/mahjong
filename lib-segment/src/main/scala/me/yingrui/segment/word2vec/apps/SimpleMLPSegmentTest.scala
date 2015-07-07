package me.yingrui.segment.word2vec.apps

import java.nio.file.{Files, Paths}

import me.yingrui.segment.math.Matrix
import me.yingrui.segment.neural.{NeuralNetwork, SigmoidLayer, SoftmaxLayer}
import me.yingrui.segment.word2vec.SimpleMLPSegment

object SimpleMLPSegmentTest extends App {

  val word2vecModelFile = if (args.indexOf("--word2vec-model") >= 0) args(args.indexOf("--word2vec-model") + 1) else "vectors.cn.hs.dat"
  val trainFile = if (args.indexOf("--train-file") >= 0) args(args.indexOf("--train-file") + 1) else "lib-segment/training-100000.txt"
  val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "segment-vector.dat"
  val ngram = if (args.indexOf("-ngram") >= 0) args(args.indexOf("-ngram") + 1).toInt else 2
  val maxIteration = if (args.indexOf("-iter") >= 0) args(args.indexOf("-iter") + 1).toInt else 400

  print("loading...\r")
  val segment = new SimpleMLPSegment(trainFile, word2vecModelFile, ngram)

  if (Files.exists(Paths.get(saveFile))) {
    val network = new NeuralNetwork
    network.add(SigmoidLayer(Matrix(segment.numberOfFeatures, segment.numberOfNeurons), Matrix(1, segment.numberOfNeurons)))
    network.add(SoftmaxLayer(Matrix(segment.numberOfNeurons, segment.numberOfClasses)).layer)
    network.load(saveFile)

    test(network)
    testSegmentCorpus(network)
  } else {
    val network = segment.trainAndTest(maxIteration)
    network save saveFile
  }

  def testSegmentCorpus(network: NeuralNetwork) {
    val error = segment.testSegmentCorpus(network)
    val numberOfSamples = segment.testDataSet.map(doc => doc.length).sum.toDouble
    displayTestResult(error, numberOfSamples)
  }

  def test(network: NeuralNetwork) {
    val error = segment.test(network)
    val numberOfSamples = segment.trainDataSet.size.toDouble
    displayTestResult(error, numberOfSamples)
  }

  def displayTestResult(error: Double, numberOfSamples: Double) {
    val accuracy = 1.0 - error / numberOfSamples

    println("error = " + error + " total = " + numberOfSamples)
    println("accuracy = " + accuracy)
  }
}
