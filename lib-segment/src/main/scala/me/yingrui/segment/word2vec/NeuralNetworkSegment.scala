package me.yingrui.segment.word2vec

import java.io.File

import me.yingrui.segment.core.{SegmentResult, SegmentWorker}
import me.yingrui.segment.math.Matrix
import me.yingrui.segment.neural.{NeuralNetwork, SoftmaxLayer}
import me.yingrui.segment.util.SerializeHandler
import me.yingrui.segment.word2vec.apps.MNNSegmentTest._

import scala.collection.mutable.ListBuffer

object NeuralNetworkSegment {

  val word2VecModelFile = "vectors.cn.hs.dat"
  val neuralNetworksModelFile =  "segment-vector.dat"

  val reader = SerializeHandler(new File(word2VecModelFile), SerializeHandler.READ_ONLY)
  val vocab = Vocabulary(reader)
  val word2VecModel = reader.deserialize2DArrayDouble()
  reader.close()

  val numberOfFeatures = word2VecModel(0).length
  val numberOfClasses = Math.pow(4, ngram).toInt

  val deserializer = SerializeHandler(new File(neuralNetworksModelFile), SerializeHandler.READ_ONLY)
  val networks = initialize(numberOfFeatures, numberOfClasses, vocab.size)
  load(deserializer)
  var transitionProb = deserializer.deserializeMatrix()
  deserializer.close()

  private def initialize(numberOfFeatures: Int, numberOfClasses: Int, size: Int) =
    for (i <- 0 until size) yield {
      val network = new NeuralNetwork()
      network.add(SoftmaxLayer(Matrix(numberOfFeatures, numberOfClasses)).layer)
      network
    }

  private def load(deserializer: SerializeHandler): Unit = {
    val size = deserializer.deserializeInt()
    assert(size == networks.size)
    for (i <- 0 until size) {
      networks(i).load(deserializer)
    }
  }

  def appy(): NeuralNetworkSegment = {
    new NeuralNetworkSegment(word2VecModel, vocab, networks, transitionProb)
  }

}

class NeuralNetworkSegment(word2VecModel: Array[Array[Double]], vocab: Vocabulary, networks: Seq[NeuralNetwork], transitionProb: Matrix) extends SegmentWorker {

  val ngram = 2
  val window = ngram - 1
  val vectorSize = word2VecModel(0).length
  val word2vecInputHelper = new Word2VecInputHelper(ngram, vectorSize, word2VecModel)

  def segment(sen: String): SegmentResult = {
    val labels = getWordLabel(sen)
    val words = getWords(sen, labels)

    val segmentResult = new SegmentResult(words.size)
    for(i <- 0 until words.length) {
      segmentResult(i).name = words(i)
    }

    segmentResult
  }

  def tokenize(sen: String): Array[String] = segment(sen).getWords().map(_.name)

  private def getWords(sen: String, labels: Seq[Int]): Seq[String] = {
    val words = new ListBuffer[String]()
    var start = 0
    for (i <- 0 until labels.length) {
      if (word2vecInputHelper.isStartLabel(labels(i))) {
        val word = sen.substring(start, i)
        if (!word.isEmpty) {
          words += word
        }
        start = i
      }
    }
    def appendTheLastWord: Unit = if (start < labels.length) words += sen.substring(start)
    appendTheLastWord
    words
  }

  def getWordLabel(sen: String): Seq[Int] = {
    val array = sen.toCharArray.map(ch => ch.toString)
    val wordIndexes = array.map(ch => vocab.getIndex(ch))

    val inputs = splitByUnknownWords(wordIndexes)
    inputs.map(input => classify(input, networks, transitionProb)).flatten
  }

  private def splitByUnknownWords(document: Seq[Int]): Seq[Seq[Int]] = {
    val inputs = document
    var start = 0
    var unknownWordIndex = inputs.indexWhere(input => input <= 0, start)
    val result = ListBuffer[Seq[Int]]()
    while (start < inputs.length) {
      if (unknownWordIndex < 0) {
        result += inputs.slice(start, inputs.length)
        start = inputs.length
      } else {
        if (start < unknownWordIndex) result += inputs.slice(start, unknownWordIndex)

        result += inputs.slice(unknownWordIndex, unknownWordIndex + 1)
        start = unknownWordIndex + 1
        unknownWordIndex = inputs.indexWhere(input => input <= 0, start)
      }
    }
    result
  }

  private def toMatrixInput(inputCharArray: Seq[Int]): Seq[(Int, Matrix)] = {
    (0 until inputCharArray.length).map(position => {
      val wordIndex = inputCharArray(position)
      if (wordIndex > 0) {
        val context = word2vecInputHelper.getContext(inputCharArray, position, window, true)
        (wordIndex, word2vecInputHelper.toMatrix(context))
      } else {
        (wordIndex, Matrix(1, vectorSize))
      }
    })
  }

  def classify(input: Seq[Int], networks: Seq[NeuralNetwork], transitionProb: Matrix): Seq[Int] = {
    if (input.forall(data => data <= 0)) {
      input.map(ele => 0)
    } else {
      val classifier = new MNNSegmentViterbiClassifier(networks, transitionProb, ngram)
      val result = classifier.classify(toMatrixInput(input))
      result.getBestPath
    }
  }
}
