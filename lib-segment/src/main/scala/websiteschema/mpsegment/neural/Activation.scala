package websiteschema.mpsegment.neural

import websiteschema.mpsegment.math.Matrix

trait Activation {

  def activate(input: Double): Double

  def activate(input: Matrix): Matrix

  def getDerivative(input: Double): Double

  def getDerivative(input: Matrix): Matrix
}

object Boundary {
  def apply(d: Double): Double = {
    if (d == Double.NegativeInfinity) {
      -1E20
    } else if (d == Double.PositiveInfinity) {
      1E20
    } else {
      d
    }
  }
}

object Sigmoid {

  def apply(): Activation = new Sigmoid

  class Sigmoid extends Activation {
    def activate(input: Double) = Boundary(1D / (1 + Math.exp(-1D * input)))

    def getDerivative(input: Double) = Boundary(input * (1D - input))

    def activate(input: Matrix) = input.map(activate(_))

    def getDerivative(input: Matrix) = input.map(getDerivative(_))
  }

}

/**
 * Hyperbolic Tangent
 */
object Tangent {

  def apply(): Activation = new Tangent

  class Tangent extends Activation {
    def activate(input: Double) = {
      val value = (Math.exp(input * 2D) - 1D) / (Math.exp(input * 2d) + 1D)
      if (value.isNaN) 1D else value
    }

    def getDerivative(input: Double) = (1D - Math.pow(activate(input), 2D))

    def activate(input: Matrix) = input.map(activate(_))

    def getDerivative(input: Matrix) = input.map(getDerivative(_))
  }

}

object Softmax {

  def apply(): Activation = new Softmax

  class Softmax extends Activation {
    def activate(input: Double) = {
      0D
    }

    def getDerivative(input: Double) = 0D

    def activate(input: Matrix) = {
      val exp = input.map(x => Boundary(Math.exp(x)))
      val sum = exp.flatten.sum
      exp.map(i => i / sum)
    }

    def getDerivative(input: Matrix) = input
  }
}