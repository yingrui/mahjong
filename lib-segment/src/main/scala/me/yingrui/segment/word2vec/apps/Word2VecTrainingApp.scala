package me.yingrui.segment.word2vec.apps

import java.io._

import me.yingrui.segment.util.Logger._
import me.yingrui.segment.util.SerializeHandler
import me.yingrui.segment.word2vec._

import scala.collection.mutable.Map
import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.util.Random

object Word2VecTrainingApp extends App {

  implicit val executionContext = ExecutionContext.Implicits.global

  println("WORD VECTOR estimation toolkit")
  val trainFile = if (args.indexOf("--train-file") >= 0) args(args.indexOf("--train-file") + 1) else "words.txt"
  val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "vectors.dat"
  val vecSize = if (args.indexOf("-size") >= 0) args(args.indexOf("-size") + 1).toInt else 200
  val window = if (args.indexOf("-window") >= 0) args(args.indexOf("-window") + 1).toInt else 8
  val taskCount = if (args.indexOf("-thread") >= 0) args(args.indexOf("-thread") + 1).toInt else 4
  val maxIteration = if (args.indexOf("-iter") >= 0) args(args.indexOf("-iter") + 1).toInt else 15
  val sample = if (args.indexOf("-sample") >= 0) args(args.indexOf("-sample") + 1).toDouble else 1e-4
  val startAlpha = if (args.indexOf("-alpha") >= 0) args(args.indexOf("-alpha") + 1).toDouble else 0.05D
  val hierarchySoftmax = true //if (args.indexOf("-hs") >= 0) true else false
  val random = new Random(System.currentTimeMillis())

  val vocab = Vocabulary(trainFile)
  val totalWordCount = vocab.getTotalWordCount
  println(s"Vocabulary has ${vocab.size} words and total word count is ${vocab.getTotalWordCount}")

  vocab.rebuild(5)
  val tree = vocab.buildHuffmanTree()
  println(s"Rebuild vocabulary and remove lower frequent words, now it contains ${vocab.size} words")

  val network: Word2VecNetwork = BagOfWordNetwork(vocab.size, vecSize, tree, hierarchySoftmax)
  val batchSize = 10000
  val splitter = new TrainingDataSplitter(trainFile, totalWordCount, vocab)
  val taskWordTotal: Map[String, Long] = splitter.loadSplitDataWordCount(taskCount)

  splitter.split(taskWordTotal, taskCount)
  println(taskWordTotal)

  def takeARound(currentIteration: Int): Double = {
    network.clearError

    val futures = (0 until taskCount).map(taskId => Future {
      val taskTotalWordCount = taskWordTotal.getOrElse(taskId.toString, 1L)
      val reader = new WordIndexReader(splitter.getDataFile(taskId), vocab, window)
      val worker = new Word2VecTrainingWorker(network, taskTotalWordCount, batchSize,
        sample, startAlpha, maxIteration,
        window, vocab.size, random)
      worker.start(reader, currentIteration)
      reader.close()
    })

    futures.foreach(f => Await.result(f, Duration.Inf))

    println()
    network.getLoss
  }

  var iteration = 0
  var cost = 0D
  var lastCost = Double.MaxValue
  var hasImprovement = true
  enableConsoleOutput
  while (iteration < maxIteration && hasImprovement) {
    cost = takeARound(iteration)
    debug("Iteration: %2d    cost: %2.5f".format(iteration, cost))
    hasImprovement = lastCost - cost > 1e-6

    lastCost = cost
    iteration += 1
  }

  val writer = SerializeHandler(new File(saveFile), SerializeHandler.WRITE_ONLY)
  println("saving the model...")
  vocab.save(writer)
  writer.serialize2DArrayDouble(network.wordVector)
  writer.close()
}