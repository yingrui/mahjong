package websiteschema.mpsegment.neural

import websiteschema.mpsegment.math.Matrix
import websiteschema.mpsegment.neural.Layer
import websiteschema.mpsegment.util.{FileUtil, SerializeHandler}

class NeuralNetwork {

  var layers = List[Layer]()

  def add(layer: Layer) {
    assert(layers.length == 0 || layers.last.weight.col == layer.weight.row - 1)
    layers = layers :+ layer
  }

  def computeOutput(input: Matrix): Matrix = {
    layers.foldLeft(input)((inputVertex, layer) => layer.computeOutput(inputVertex))
  }

  override def toString(): String = layers.map(_.weight).mkString("\n\n")

  def save(file: String) {
    val dumper = SerializeHandler(new java.io.File(file), SerializeHandler.WRITE_ONLY)
    dumper.serializeInt(layers.length)
    layers.foreach(layer => dumper.serializeMatrix(layer.weight))
    dumper.close()
  }

  def load(resource: String) {
    val input = new java.io.DataInputStream(FileUtil.getResourceAsStream(resource))
    val serializeHandler = new SerializeHandler(input, null)
    for(i <- 0 until serializeHandler.deserializeInt()) {
      add(SigmoidLayer(serializeHandler.deserializeMatrix()))
    }
    input.close()
  }
}

object NeuralNetwork {

  def apply() = new NeuralNetwork()
}