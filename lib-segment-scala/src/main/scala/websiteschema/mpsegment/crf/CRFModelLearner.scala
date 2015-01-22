package websiteschema.mpsegment.crf

import websiteschema.mpsegment.math.Matrix

import scala.collection.mutable.ListBuffer

class CRFModelLearner(model: CRFModel, func: CRFDiffFunc) {

  val m = 20 //saves m updates

  val X = Matrix(model.featuresCount, model.labelCount)
  val gradient = Matrix(model.featuresCount, model.labelCount)
  val newGrad = Matrix(model.featuresCount, model.labelCount)
  val newX = Matrix(model.featuresCount, model.labelCount)
  val dir = Matrix(model.featuresCount, model.labelCount)

  val sList = new ListBuffer[Matrix]() // s(k) = x(k+1) - x(k) = newX - X
  val yList = new ListBuffer[Matrix]() // y(k) = g(k+1) - g(k) = newGrad - gradient
  val roList = new ListBuffer[Double]() // ro(k) = 1 / [ y(k) * s(k) ]

  val previousValues = new ListBuffer[Double]()

  def findDirection(dir: Matrix, grad: Matrix): Unit = {
    dir := grad
    val m = sList.size
    val as = new Array[Double](m)

    (0 until m).reverse.foreach(i => {
      as(i) = roList(i) * (sList(i) * dir)
      dir += (yList(i) x -as(i))
    })

    //hessian approximate
    if (m != 0) {
      val y = yList(m - 1)
      val dotY = y * y
      val gamma = sList(m - 1) * y / dotY
      dir *= gamma
    }

    (0 until m).foreach(i => {
      val b = yList(i) * dir * roList(i)
      dir += (sList(i) x as(i) - b)
    })

    dir *= -1
  }

  def train: Unit = {
    var it = 0
    var value = func.valueAt(X.flatten)
    gradient := func.derivative
    val maxIteration = 300
    while (it < maxIteration) {
      findDirection(dir, gradient)

      releaseHistoryUpdates

      val sum = func.derivative.map(d => Math.abs(d)).sum
      print(s"Iteration $it: $value, $sum")

      val newValue = linearSearch(func.derivative, value)
      newGrad := func.derivative

      val nextS = newX - X
      val nextY = newGrad - gradient
      val ro = 1.0 / (nextS * nextY)

      saveHistoryUpdates(nextS, nextY, ro)

      println()

      previousValues += value
      val size = previousValues.size
      val previousVal = if (size == 10) previousValues.remove(0) else previousValues(0)
      val averageImprovement = (previousVal - newValue) / size.toDouble

      val break = (size > 5 && averageImprovement / newValue < model.tolerance) || (it >= maxIteration)

      if (break) {
        it = Int.MaxValue
      } else {
        value = newValue

        gradient := newGrad
        X := newX
        newX.clear

        it += 1
      }
    }

    model.weights := X
  }

  private def saveHistoryUpdates(nextS: Matrix, nextY: Matrix, ro: Double) {
    sList += nextS
    yList += nextY
    roList += ro
  }

  private def releaseHistoryUpdates {
    if (sList.size == m) sList.remove(0)
    if (yList.size == m) yList.remove(0)
    if (roList.size == m) roList.remove(0)
  }

  private def linearSearch(derivative: Array[Double], lastIterationValue: Double): Double = {

    val normGradInDir = dir * gradient
    var a = 1.0D
    val c1 = 0.1D
    val c = 0.01D

    var value = Double.PositiveInfinity
    var break = false
    var times = 0
    do {
      a = a * c1
      newGrad := (dir x a)
      newX := (X + newGrad)

      value = func.valueAt(newX.flatten)
      print(" " + value.toInt)
      break = (value < lastIterationValue + normGradInDir * c * a) || times > 10

      times += 1
    } while (!break)
    value
  }

}
