package me.yingrui.segment.word2vec

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}

class SegmentCorpusTest extends FunSuite with Matchers with MockFactory {

  test("should return input and output when ngram is 1") {
    val word2VecModel: Array[Array[Double]] = Array(Array(1D, 2D), Array(1D, 2D))
    val vocab = stub[Vocabulary]

    (vocab.getIndex _).when("中").returns(1)
    (vocab.getIndex _).when("文").returns(1)

    val segmentCorpus = new SegmentCorpus(word2VecModel, vocab, 1, 1)

    val document = List(("中", "B"), ("文", "E"))

    val data = segmentCorpus.convert(document)
    data(0)._1.flatten should be (Array(1D, 2D))
    data(0)._2.flatten should be (Array(0D, 1D, 0D, 0D))

    data(1)._1.flatten should be (Array(1D, 2D))
    data(1)._2.flatten should be (Array(0D, 0D, 0D, 1D))
  }

  test("should return input indexes, current index and output") {
    val word2VecModel: Array[Array[Double]] = Array(Array(1D, 2D), Array(3D, 4D), Array(5D, 6D))
    val vocab = stub[Vocabulary]

    (vocab.getIndex _).when("学").returns(3)
    (vocab.getIndex _).when("中").returns(1)
    (vocab.getIndex _).when("文").returns(2)

    val segmentCorpus = new SegmentCorpus(word2VecModel, vocab, 1, 1)

    val document = List(("学", "S"), ("中", "B"), ("文", "E"))

    val data = segmentCorpus.convertToWordIndexes(document)
    data(0)._1 should be (List(3, 1))
    data(0)._2 should be (3)
    data(0)._3.flatten should be (Array(1D, 0D, 0D, 0D))

    data(1)._1 should be (List(3, 1, 2))
    data(1)._2 should be (1)
    data(1)._3.flatten should be (Array(0D, 1D, 0D, 0D))

    data(2)._1 should be (List(1, 2))
    data(2)._2 should be (2)
    data(2)._3.flatten should be (Array(0D, 0D, 0D, 1D))
  }

  test("should return input matrix given input word indexes") {
    val word2VecModel: Array[Array[Double]] = Array(Array(1D, 2D), Array(3D, 4D), Array(5D, 6D))
    val vocab = stub[Vocabulary]

    val segmentCorpus = new SegmentCorpus(word2VecModel, vocab, 1, 1)

    val input = segmentCorpus.convertToMatrix(List(1, 2))

    input.flatten should be (Array((3D + 5D) / 2D, (4D + 6D) / 2D))
  }

  test("should return input and output when ngram is 2") {
    val word2VecModel: Array[Array[Double]] = Array(Array(1D, 2D), Array(1D, 2D))
    val vocab = stub[Vocabulary]

    (vocab.getIndex _).when("中").returns(1)
    (vocab.getIndex _).when("文").returns(1)

    val segmentCorpus = new SegmentCorpus(word2VecModel, vocab, 2, 2)

    val document = List(("中", "B"), ("文", "E"))

    val data = segmentCorpus.convert(document)
    data(0)._1.flatten should be (Array(1D, 2D))
    data(0)._2.flatten should be (
      Array(0D, 1D, 0D, 0D,
            0D, 0D, 0D, 0D,
            0D, 0D, 0D, 0D,
            0D, 0D, 0D, 0D))

    data(1)._1.flatten should be (Array(1D, 2D))
    data(1)._2.flatten should be (
      Array(0D, 0D, 0D, 0D,
            0D, 0D, 0D, 1D,
            0D, 0D, 0D, 0D,
            0D, 0D, 0D, 0D))
  }
}
