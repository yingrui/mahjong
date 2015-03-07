package websiteschema.mpsegment.neural

import websiteschema.mpsegment.math.Matrix
import collection.mutable.ListBuffer

trait Train {
  val inputArray = ListBuffer[Matrix]()
  val idealArray = ListBuffer[Matrix]()

  def addSample(input: Matrix, ideal: Matrix)

  def getError: Double

  def getNetwork: NeuralNetwork

  def takeARound(iteration: Int): Unit

  def testWithTrainSet: Double = {
    var correct = 0
    for (i <- 0 until inputArray.length) {
      val actual = getNetwork.computeOutput(inputArray(i))
      val error = Matrix.arithmetic(actual.flatten, idealArray(i).flatten, (a, b) => Math.pow(a - b, 2D)).sum
      val th = Math.pow(1.0 - 0.5, 2D)
      if (error < th) {
        correct += 1
      }
    }
    correct.toDouble / inputArray.length.toDouble
  }
}


