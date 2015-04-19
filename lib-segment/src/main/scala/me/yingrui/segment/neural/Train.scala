package me.yingrui.segment.neural

import java.lang.Math._

import me.yingrui.segment.math.Matrix
import collection.mutable.ListBuffer

trait Train {
  val inputArray = ListBuffer[Matrix]()
  val idealArray = ListBuffer[Matrix]()

  def addSample(input: Matrix, ideal: Matrix)

  def getLoss: Double

  def getNetwork: NeuralNetwork

  def takeARound(iteration: Int): Unit

  def testWithTrainSet: Double = {
    var correct = 0
    for (i <- 0 until inputArray.length) {
      val actual = getNetwork.computeOutput(inputArray(i))
      val error = (actual - idealArray(i)).map(Eij => pow(Eij, 2D)).sum
      val th = pow(1.0 - 0.5, 2D)
      if (error < th) {
        correct += 1
      }
    }
    correct.toDouble / inputArray.length.toDouble
  }
}


