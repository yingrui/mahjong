package me.yingrui.segment.neural

import me.yingrui.segment.math.Matrix
import me.yingrui.segment.util.{FileUtil, SerializeHandler}

class NeuralNetwork {

  var layers = List[Layer]()

  def add(layer: Layer) {
    assert(layers.length == 0 || layers.last.weight.col == layer.weight.row)
    layers = layers :+ layer
  }

  def computeOutput(input: Matrix): Matrix = {
    val out = Matrix(1, layers.last.size)
    val result = layers.foldLeft(input)((inputVertex, layer) => layer.computeOutput(inputVertex))
    out := result
    out
  }

  def computeOutput(input: Matrix, hs: Matrix): Matrix = {
    val out = Matrix(1, layers.last.size)
    val result = layers.foldLeft(input)((inputVertex, layer) => {
      if (layer.isInstanceOf[BPRecurrentLayer]) {
        layer.asInstanceOf[BPRecurrentLayer].computeOutput(inputVertex, hs)
      } else {
        layer.computeOutput(inputVertex)
      }
    })
    out := result
    out
  }

  override def toString(): String = layers.map(layer => Array(layer.weight, layer.bias)).flatten.mkString("\n\n")

  def save(file: String) {
    val dumper = SerializeHandler(new java.io.File(file), SerializeHandler.WRITE_ONLY)
    save(dumper)
    dumper.close()
  }

  def save(dumper: SerializeHandler) {
    dumper.serializeInt(layers.length)
    layers.foreach(layer => {
      dumper.serializeMatrix(layer.weight)
      dumper.serializeMatrix(layer.bias)
    })
  }

  def load(resource: String) {
    val input = new java.io.DataInputStream(FileUtil.getResourceAsStream(resource))
    val serializeHandler = new SerializeHandler(input, null)
    load(serializeHandler)
    input.close()
  }

  def load(serializeHandler: SerializeHandler) {
    for (i <- 0 until serializeHandler.deserializeInt()) {
      layers(i).weight := serializeHandler.deserializeMatrix()
      layers(i).bias := serializeHandler.deserializeMatrix()
    }
  }
}

object NeuralNetwork {

  def apply() = new NeuralNetwork()
}