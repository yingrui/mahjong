package websiteschema.mpsegment.neural

import websiteschema.mpsegment.math.Matrix

class NeuralNetwork {

  type NeuronLayer = {
    def computeOutput(input: Matrix): Matrix
    def weight: Matrix
  }

  var layers = List[NeuronLayer]()

  def add(layer: NeuronLayer) {
    assert(layers.length == 0 || layers.last.weight.col == layer.weight.row)
    layers = layers :+ layer
  }

  def computeOutput(input: Matrix): Matrix = {
    layers.foldLeft(input)((inputVertex, layer) => layer.computeOutput(inputVertex))
  }

}

object NeuralNetwork {

  def apply() = new NeuralNetwork()
}