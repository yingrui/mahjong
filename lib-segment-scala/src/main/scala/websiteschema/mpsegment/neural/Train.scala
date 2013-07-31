package websiteschema.mpsegment.neural

trait Train {

  def getError: Double

  def getNetwork: NeuralNetwork

  def takeARound(iteration: Int): Unit

}





