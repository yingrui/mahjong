package me.yingrui.segment.word2vec

import me.yingrui.segment.math.Matrix
import me.yingrui.segment.neural.{SoftmaxLayer, BPSimpleRecurrentLayer, BackPropagation}
import me.yingrui.segment.neural.errors.CrossEntropyLoss

class Word2VecTrainingNetworkBuilder(val vocab: Vocabulary, val vecSize: Int) {

  val ALPHA = 0.1D
  val loss = new CrossEntropyLoss
  //  val loss = new RMSLoss
  val network = new BackPropagation(vocab.size, vocab.size, ALPHA, 0.0D, loss)

  def initNetwork: BackPropagation = {
    val layer0Weight = Matrix.randomize(vocab.size, vecSize)
    val layer0Bias = Matrix.randomize(1, vecSize)
    val layer1Weight = Matrix.randomize(vecSize, vocab.size)
    val layer1Bias = Matrix.randomize(1, vocab.size)
    val recurrentHiddenLayer = new BPSimpleRecurrentLayer(layer0Weight, layer0Bias)
    //    val outputLayer = new BPSigmoidLayer(layer1Weight, layer1Bias)
    val outputLayer = SoftmaxLayer(layer1Weight)

    network.addLayer(recurrentHiddenLayer)
    network.addLayer(outputLayer)
    network
  }

  def buildNetwork = {
    class SimpleWord2VecNetwork extends Word2VecNetwork {
      val network = initNetwork
      val wordsCount = vocab.size
      val size = vecSize
      val wordVector = matrixTo2DArray(network.getNetwork.layers.head.weight)

      def learn(input: Array[Int], output: Array[(Int, Int)], alpha: Double): Unit = {
        val in = Matrix(1, vocab.size)
        val expectedOutput = Matrix(1, vocab.size)

        input.foreach(w => in(0, w) = 1D)
        output.foreach(t => expectedOutput(0, t._1) = t._2.toDouble)

        val out = network.computeOutput(in)
        network.computeError(out, expectedOutput)
        network.update()
      }

      def clearError = loss.clear

      def getLoss = loss.loss

      private def matrixTo2DArray(m: Matrix): Array[Array[Double]] = {
        val arrays = new Array[Array[Double]](m.row)
        for (i <- 0 until m.row) {
          arrays(i) = m.row(i).flatten
        }
        arrays
      }
    }

    new SimpleWord2VecNetwork()
  }
}
