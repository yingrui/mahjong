package websiteschema.mpsegment.neural

trait Activation {
  def activation(input: Double): Double

  def derivative(input: Double): Double
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
    def activation(input: Double) = Boundary(1D / (1 + Math.exp(-1D * input)))

    def derivative(input: Double) = Boundary(input * (1D - input))
  }

}

/**
 * Hyperbolic Tangent
 */
object Tangent {

  def apply(): Activation = new Tangent

  class Tangent extends Activation {
    def activation(input: Double) = {
      val value = (Math.exp(input * 2D) - 1D) / (Math.exp(input * 2d) + 1D)
      if (value.isNaN) 1D else value
    }

    def derivative(input: Double) = (1D - Math.pow(activation(input), 2D))
  }

}