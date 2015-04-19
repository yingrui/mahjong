package me.yingrui.segment.neural.mlp

import java.lang.Math.abs

import me.yingrui.segment.math.Matrix
import me.yingrui.segment.neural.errors.CrossEntropyLoss
import me.yingrui.segment.neural._
import me.yingrui.segment.util.Logger._
import org.scalatest.{FunSuite, Matchers}

import scala.io.Source

class SigmoidSoftmaxMLPTest extends FunSuite with Matchers with WisconsinBreastCancerDataSet {

  private def train() = {
    val numberOfClasses = 2
    val numberOfNeurons = 10

    val numberOfFeatures = trainDataSet(0)._1.col

    val layer0Weight = Matrix.randomize(numberOfFeatures, numberOfNeurons, -1D, 1D)
    val layer0Bias = Matrix.randomize(1, numberOfNeurons, -1D, 1D)
    val layer1Weight = Matrix.randomize(numberOfNeurons, numberOfClasses, -1D, 1D)

    val loss = new CrossEntropyLoss
    val network = new BackPropagation(numberOfNeurons, numberOfClasses, 1.0, 0.0D, loss)
    network.addLayer(new BPSigmoidLayer(layer0Weight, layer0Bias))
    network.addLayer(SoftmaxLayer(layer1Weight))


    def takeARound(trainSet: Seq[(Matrix, Matrix)]): Double = {
      trainSet.foreach(data => {
        val output: Matrix = network.computeOutput(data._1)
        val expectedOutput: Matrix = data._2
        network.computeError(output, expectedOutput)
        network.update()
      })

      network.getLoss
    }

    var iteration = 0
    var cost = 0D
    var lastCost = 0.0D
    var hasImprovement = true
    while (iteration < 100000 && hasImprovement) {
      cost = takeARound(trainDataSet)
      debug(s"iter: ${iteration} cost: ${cost}")
      hasImprovement = abs(cost - lastCost) > 1e-5

      lastCost = cost
      iteration += 1
    }

    network.getNetwork
  }

  private def classify(classifier: NeuralNetwork, testInput: Matrix): Matrix = {
    val actualOutput = classifier computeOutput testInput
    if (actualOutput(0, 0) > actualOutput(0, 1)) {
      actualOutput(0, 0) = 1.0D
      actualOutput(0, 1) = 0.0D
    } else {
      actualOutput(0, 0) = 0.0D
      actualOutput(0, 1) = 1.0D
    }
    actualOutput
  }

  test("train mlp classifier") {
    val accuracy = trainAndTest
    if (accuracy < 0.92) {
      trainAndTest should be > 0.92
    }
  }

  def trainAndTest = {
    val tic = System.currentTimeMillis()
    println("train set contains " + trainDataSet.size + " samples, test set contains " + testDataSet.size + " samples.")

    val classifier = train()

    val error = test(classifier)

    val numberOfSamples = testDataSet.size.toDouble
    val accuracy = 1.0 - error / numberOfSamples

    println("error = " + error + " total = " + numberOfSamples)
    println("accuracy = " + accuracy)
    val toc = System.currentTimeMillis()
    val second = (toc - tic) / 1000
    println(s"elapsed time: ${second}s")
    accuracy
  }

  private def test(classifier: NeuralNetwork): Double = {
    testDataSet.map(data => {
      val testInput = data._1
      val expectedOutput = data._2

      val actualOutput: Matrix = classify(classifier, testInput)

      if ((expectedOutput - actualOutput).map(abs(_)).sum > 0)
        1.0D
      else
        0.0D
    }).sum
  }
}
