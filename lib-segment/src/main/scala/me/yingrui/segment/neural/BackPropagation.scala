package me.yingrui.segment.neural

import me.yingrui.segment.math.Matrix
import me.yingrui.segment.neural.errors.RMSLoss
import me.yingrui.segment.neural.errors.Loss
import me.yingrui.segment.util.Logger._

object BackPropagation {

  def apply(inputSize: Int, outputSize: Int, rate: Double, momentum: Double): BackPropagation = new BackPropagation(inputSize, outputSize, rate, momentum, new RMSLoss)

  def apply(inputSize: Int, outputSize: Int): BackPropagation = {
    new BackPropagation(inputSize, outputSize, 1.0D, 0.0D, new RMSLoss)
  }
}

class BackPropagation(val inputSize: Int, val outputSize: Int, val rate: Double, val momentum: Double, val errorCalculator: Loss) extends Train {

  private var loss = 0D
  private val network = new NeuralNetwork()
  private var layers = List[BackPropagationLayer]()
  private var neuronSizePerLayer = List[Int](inputSize)

  def addSample(input: Matrix, ideal: Matrix) {
    assert(input.isVector && input.col == inputSize)
    assert(ideal.isVector && ideal.col == outputSize)
    inputArray += input
    idealArray += ideal
  }

  def addLayer(neuronSize: Int) {
    neuronSizePerLayer = neuronSizePerLayer ++ List(neuronSize)
  }

  def addLayer(layer: BackPropagationLayer) {
    layers :+= layer
    network add layer
  }

  def computeOutput(input: Matrix): Matrix = network.computeOutput(input)

  def getLoss = errorCalculator.loss

  def getNetwork = network

  def update(): Unit = update(rate)

  def update(rate: Double): Unit = layers.foreach(_.update(rate, momentum))

  def takeARound(iteration: Int): Unit = {
    initLayers
    println("init layers completion.")

    for (it <- 1 to iteration) {
      val outputs = for (i <- 0 until inputArray.length) yield {
        val output = computeOutput(inputArray(i))
        computeError(output, idealArray(i))
        update()
        output
      }
      loss = recalculateError(outputs)
      if (it % 100 == 0) {
        if(iteration-it == 100) {
          debug("Cycles Left: " + (iteration - it) + ", Error: " + loss);
        }
        debug("Cycles Left: " + (iteration - it) + ", Error: " + loss);
      }
    }
  }

  def recalculateError(outputs: Seq[Matrix]): Double = {
    val network = new NeuralNetwork
    layers.foreach(network.add)

    errorCalculator.clear
    var i = 0
    while(i < idealArray.length) {
      val actual = outputs(i)
      val ideal = idealArray(i)
      errorCalculator.updateError(actual, ideal)
      i += 1
    }
    errorCalculator.loss
  }

  def computeError(actual: Matrix, ideal: Matrix) {
    errorCalculator.updateError(actual, ideal)

    layers.foldRight(ideal - actual)((layer, error) => layer.propagateError(error))
  }

  private def initLayers {
    if (layers.length == 0) {
      neuronSizePerLayer = neuronSizePerLayer ++ List(outputSize)
      for(i <- 1 until neuronSizePerLayer.length) {
        layers = layers ++ List(BackPropagationLayer(neuronSizePerLayer(i - 1), neuronSizePerLayer(i)))
      }

      layers.foreach(bp => network.add(bp))
    }
  }
}
