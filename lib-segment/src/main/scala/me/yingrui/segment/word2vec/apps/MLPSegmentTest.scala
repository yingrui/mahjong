package me.yingrui.segment.word2vec.apps

import java.nio.file.{Files, Paths}

import me.yingrui.segment.math.Matrix
import me.yingrui.segment.neural.{SoftmaxLayer, SigmoidLayer, NeuralNetwork}
import me.yingrui.segment.word2vec.MLPSegment

object MLPSegmentTest extends App {

  val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "segment-vector.dat"
  
  print("loading...\r")
  val segment = new MLPSegment()

  if (Files.exists(Paths.get(saveFile))) {
    val network = new NeuralNetwork
    network.add(SigmoidLayer(Matrix(segment.numberOfFeatures, segment.numberOfNeurons), Matrix(1, segment.numberOfNeurons)))
    network.add(SoftmaxLayer(Matrix(segment.numberOfNeurons, segment.numberOfClasses)).layer)
    network.load(saveFile)

    val error = segment.testSegmentCorpus(network)
    val numberOfSamples = segment.testDataSet.map(doc => doc.length).sum.toDouble
    val accuracy = 1.0 - error / numberOfSamples

    println("error = " + error + " total = " + numberOfSamples)
    println("accuracy = " + accuracy)
  } else {
    val network = segment.trainAndTest(maxIteration = 200)
    network save saveFile
  }


}
