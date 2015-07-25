package me.yingrui.segment.word2vec

import org.scalatest.{Matchers, FunSuite}

class BagOfWordNetworkTest extends FunSuite with Matchers {

  test("should initialize words vec and status vector") {
    val wordsCount = 100
    val vectorSize = 10
    val network = BagOfWordNetwork(wordsCount, vectorSize)
    val m = network.wordVector
    m.length shouldBe wordsCount
    m.forall(row => row.length == vectorSize) shouldBe true

    val v = network.layer0Output
    v.forall(e => e == 0D) shouldBe true

    val layer1Weights = network.layer1Weights
    layer1Weights.length shouldBe wordsCount
    layer1Weights.forall(row => row.length == vectorSize) shouldBe true
  }

  test("should calculate layer0 output for given input") {
    val wordsCount = 3
    val vectorSize = 2
    val wordVector = Array(Array(1D, 1D), Array(1D, 1D), Array(1D, 1D))
    val layer1Weight = Array(Array(1D, 1D), Array(1D, 1D), Array(1D, 1D))
    val network = BagOfWordNetwork(wordsCount, vectorSize, wordVector, layer1Weight)

    val input = Array[Int](0, 1, 2)
    network.computeLayer0Output(input)

    network.layer0Output shouldBe Array(1D, 1D)
  }

  test("should calculate layer1 grads for given input and output") {
    val wordsCount = 3
    val vectorSize = 2
    val wordVector = Array(Array(1D, 1D), Array(1D, 1D), Array(1D, 1D))
    val layer1Weight = Array(Array(1D, 1D), Array(1D, 1D), Array(1D, 1D))
    val network = BagOfWordNetwork(wordsCount, vectorSize, wordVector, layer1Weight)
    val alpha = 0.1D

    val input = Array[Double](1D, 1D)
    val output = Array[(Int, Int)]((0, 1), (1, 0), (2, 0))
    val grad = network.computeLayer1Grads(input, output, alpha)
    grad(0)._2 should be ((1D - SimplifiedActivationUtil.simplifiedSigmoid(2D)) * alpha)
    grad(1)._2 should be ((0D - SimplifiedActivationUtil.simplifiedSigmoid(2D)) * alpha)
    grad(2)._2 should be ((0D - SimplifiedActivationUtil.simplifiedSigmoid(2D)) * alpha)
  }

  test("should update layer1 weights and compute back propagation errors") {
    val wordsCount = 3
    val vectorSize = 2
    val wordVector = Array(Array(1D, 1D), Array(1D, 1D), Array(1D, 1D))
    val layer1Weight = Array(Array(1D, 1D), Array(1D, 1D), Array(1D, 1D))
    val network = BagOfWordNetwork(wordsCount, vectorSize, wordVector, layer1Weight)
    val alpha = 0.1D

    val input = Array[Double](1D, 1D)
    val output = Array[(Int, Int)]((0, 1), (1, 0), (2, 0))
    val grads = network.computeLayer1Grads(input, output, alpha)
    val errors = network.updateLayer1WeightsAndPropagateErrors(grads, input)

    errors.foreach(println)
    val weightX = 1D // all the weights are same here
    errors(0) should be (grads.map(t => t._2 * weightX).sum)
    errors(1) should be (grads.map(t => t._2 * weightX).sum)

    layer1Weight(0)(0) should be (1D + ((1D - SimplifiedActivationUtil.simplifiedSigmoid(2D)) * alpha))
    layer1Weight(0)(1) should be (1D + ((1D - SimplifiedActivationUtil.simplifiedSigmoid(2D)) * alpha))

    layer1Weight(1)(0) should be (1D + ((0D - SimplifiedActivationUtil.simplifiedSigmoid(2D)) * alpha))
    layer1Weight(1)(1) should be (1D + ((0D - SimplifiedActivationUtil.simplifiedSigmoid(2D)) * alpha))

    layer1Weight(2)(0) should be (1D + ((0D - SimplifiedActivationUtil.simplifiedSigmoid(2D)) * alpha))
    layer1Weight(2)(1) should be (1D + ((0D - SimplifiedActivationUtil.simplifiedSigmoid(2D)) * alpha))
  }

  test("should update layer0 weights") {
    val wordsCount = 3
    val vectorSize = 2
    val wordVector = Array(Array(1D, 1D), Array(1D, 1D), Array(1D, 1D))
    val layer1Weight = Array(Array(1D, 1D), Array(1D, 1D), Array(1D, 1D))
    val network = BagOfWordNetwork(wordsCount, vectorSize, wordVector, layer1Weight)

    val input = Array[Double](1D, 1D)
    val output = Array[(Int, Int)]((0, 1), (1, 0), (2, 0))
    val grads = network.computeLayer1Grads(input, output, 0.1D)
    val errors = network.updateLayer1WeightsAndPropagateErrors(grads, input)
    network.updateLayer0Weights(errors, output.map(t => t._1))

    wordVector(0)(0) should be (1D + errors(0))
    wordVector(0)(1) should be (1D + errors(1))

    wordVector(1)(0) should be (1D + errors(0))
    wordVector(1)(1) should be (1D + errors(1))

    wordVector(2)(0) should be (1D + errors(0))
    wordVector(2)(1) should be (1D + errors(1))
  }
}
