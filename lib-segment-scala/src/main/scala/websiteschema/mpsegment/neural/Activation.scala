package websiteschema.mpsegment.neural

object Sigmoid {

  def activation(input: Double) = 1D / (1 + Math.exp(-1D * input))

  def derivative(input: Double) = input * (1D - input)
}

/**
 * Hyperbolic Tangent
 */
object Tangent {
  def activation(input: Double) = (Math.exp(input * 2D) - 1D) / (Math.exp(input * 2d) + 1D)

  def derivative(input: Double) = 1D - Math.pow(activation(input), 2D)
}