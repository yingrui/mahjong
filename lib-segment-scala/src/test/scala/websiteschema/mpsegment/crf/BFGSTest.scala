package websiteschema.mpsegment.crf

import org.junit.Test
import websiteschema.mpsegment.math.Matrix

trait Function {
  def valueAt(x: Matrix): Double
  def derivative: Matrix
}

object LBFGS {

  def apply() = new LBFGS()

}

class LBFGS {

  def search(func: Function, o: Matrix): Matrix = {
    val fx = func.valueAt(o)
    println(fx)
    val gradient = func.derivative

    val direction: Matrix = findDirection(gradient)

    val newX: Matrix = search(o, direction)

    println(func.valueAt(newX))
    newX
  }

  def search(o: Matrix, direction: Matrix): Matrix = {
    val step = 1
    val newX = o + (direction x step)
    newX
  }

  private def findDirection(gradient: Matrix): Matrix = {
    val direction = gradient x -1.0
    direction
  }
}

class F extends Function {

  private var currentX: Matrix = null

  def valueAt(x: Matrix): Double = {
    currentX = x
    x.flatten.map(x_i => x_i * x_i).sum
  }

  def derivative: Matrix = currentX x 2
}

class BFGSTest {

  @Test
  def should {
    val optimizedPoint = LBFGS().search(new F(), Matrix(Array(2.0, 3.0)))
    println(optimizedPoint)
  }

}
