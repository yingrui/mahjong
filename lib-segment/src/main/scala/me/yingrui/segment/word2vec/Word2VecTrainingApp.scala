package me.yingrui.segment.word2vec

import java.io._
import java.lang.Math.abs

import me.yingrui.segment.util.Logger._
import me.yingrui.segment.util.SerializeHandler

import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.util.Random

object Word2VecTrainingApp extends App {

  implicit val executionContext = ExecutionContext.Implicits.global

  println("WORD VECTOR estimation toolkit")
  val trainFile = if (args.indexOf("--train-file") >= 0) args(args.indexOf("--train-file") + 1) else "text8"
  val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "vectors.dat"
  val vecSize = if (args.indexOf("-size") >= 0) args(args.indexOf("-size") + 1).toInt else 100
  val window = if (args.indexOf("-window") >= 0) args(args.indexOf("-window") + 1).toInt else 5
  val random = new Random()

  def readVocabulary = {
    val reader = new InputStreamReader(new FileInputStream(trainFile))
    val wordReader = new WordReader(reader, window)
    val vocab = Vocabulary(wordReader)
    reader.close()
    vocab
  }

  val vocab = readVocabulary
  val totalWordCount = vocab.getTotalWordCount
  println(s"Vocabulary has ${vocab.size} words and total word count is ${vocab.getTotalWordCount}")

  vocab.rebuild(5)
  println(s"Rebuild vocabulary and remove lower frequent words, now it contains ${vocab.size} words")

  val network: Word2VecNetwork = BagOfWordNetwork(vocab.size, vecSize)

  def takeARound(iteration: Int): Double = {
    network.clearError

    val startAlpha = 1e-3
    var totalTrainedWords = 0
    val taskCount = 4
    val futures = (0 until taskCount).map(taskId => Future {

      val startAt = totalWordCount / taskCount * taskId
      val endAt = startAt + totalWordCount / taskCount
      val reader = new InputStreamReader(new FileInputStream(trainFile))
      val wordReader = new WordReader(reader, window)
      var words = wordReader.readWindow()
      var count = 0
      var alpha = startAlpha * (1D - 1D / (iteration + 1D))
      if(alpha < startAlpha * 1e-4) alpha = startAlpha * 1e-4

      while (!words.isEmpty && count <= endAt) {
        val wordIndex = vocab.getIndex(words(window))
        if (wordIndex > 0 && count >= startAt) {
          val input = words.map(w => vocab.getIndex(w))

          val negativeSamples = 25
          val output = new Array[(Int, Int)](negativeSamples)
          output(0) = (wordIndex, 1)
          for (i <- 1 until negativeSamples) {
            var index = random.nextInt(vocab.size)
            if (index == wordIndex) index = random.nextInt(vocab.size)
            output(i) = (index, 0)
          }

          val inputArray = input.toArray
          inputArray(window) = 0
          // train
          network.learn(input.filter(in => in > 0).toArray, output, startAlpha)
        }

        count += 1
        val progress = 1D - (endAt - count).toDouble / (endAt - startAt).toDouble
        if (count > startAt && count % 10000 == 0) {
          print("progress: %2.5f\r".format(progress))
          alpha = startAlpha * (1D - (1D / (iteration + 1D)) * progress)

          if(alpha < startAlpha * 1e-4) alpha = startAlpha * 1e-4
          if(alpha >= startAlpha) alpha = startAlpha
        }
        words = wordReader.readWindow()
      }
      reader.close()
    })

    futures.foreach(f => Await.result(f, Duration.Inf))

    println()
    network.getLoss
  }

  var iteration = 0
  var cost = 0D
  var lastCost = 0.0D
  var hasImprovement = true
  enableConsoleOutput
  while (iteration < 25 && hasImprovement) {
    cost = takeARound(iteration)
    debug(s"iter: ${iteration} cost: ${cost}")
    hasImprovement = abs(cost - lastCost) > 1e-5

    lastCost = cost
    iteration += 1
  }

  val writer = SerializeHandler(new File(saveFile), SerializeHandler.WRITE_ONLY)
  println("saving the model")
  vocab.save(writer)
  writer.serialize2DArrayDouble(network.wordVector)
}