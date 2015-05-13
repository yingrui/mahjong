package me.yingrui.segment.word2vec

import java.io._
import java.lang.Math.abs

import me.yingrui.segment.util.Logger._
import me.yingrui.segment.util.SerializeHandler
import scala.collection.mutable.Map
import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.util.Random

object Word2VecTrainingApp extends App {

  implicit val executionContext = ExecutionContext.Implicits.global

  println("WORD VECTOR estimation toolkit")
  val trainFile = if (args.indexOf("--train-file") >= 0) args(args.indexOf("--train-file") + 1) else "text8"
  val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "vectors.dat"
  val vecSize = if (args.indexOf("-size") >= 0) args(args.indexOf("-size") + 1).toInt else 200
  val window = if (args.indexOf("-window") >= 0) args(args.indexOf("-window") + 1).toInt else 8
  val maxIteration = if (args.indexOf("-iter") >= 0) args(args.indexOf("-iter") + 1).toInt else 15
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
  val taskCount = 4
  val startAlpha = 0.05D
  val sample = 1e-4
  val batchSize = 10000
  val taskWordTotal = Map[Long, Long](2L -> 4181364L, 1L -> 4179509L, 3L -> 4177272L, 0L -> 4180696L)

  def getDataFile(taskId: Int) = trainFile + "." + taskId + ".dat"

  def prepareData(taskCount: Int) {
    val wordCountForEachTask = totalWordCount / taskCount
    val reader = new InputStreamReader(new FileInputStream(trainFile))
    val wordReader = new WordReader(reader, window)
    var count = 0
    var actualWordCount = 0
    var taskId = 0L
    var writer = SerializeHandler(new File(getDataFile(taskId.toInt)), SerializeHandler.WRITE_ONLY)
    while (count < totalWordCount) {
      val wordIndex = vocab.getIndex(wordReader.read())
      if (wordIndex > 0) {
        writer.serializeInt(wordIndex)
        actualWordCount += 1
      }

      count += 1
      if (count % wordCountForEachTask == 0) {
        print("Preprocess train data taskId: %d, progress: %2.3f\r".format(taskId, count.toDouble / totalWordCount.toDouble))
        taskWordTotal += (taskId -> actualWordCount)
        actualWordCount = 0

        taskId = count / wordCountForEachTask
        if (taskId < taskCount) {
          writer.close()
          writer = SerializeHandler(new File(getDataFile(taskId.toInt)), SerializeHandler.WRITE_ONLY)
        }
      }
    }
    println()
    writer.close()
    reader.close()
  }

  prepareData(taskCount)
  println(taskWordTotal)

  def takeARound(iteration: Int): Double = {
    network.clearError

    val futures = (0 until taskCount).map(taskId => Future {
      val totalCount: Long = taskWordTotal.getOrElse(taskId, 1)
      val reader = new WordIndexReader(getDataFile(taskId), vocab, window)

      var count = 0L
      var alpha = startAlpha * (1D - (count.toDouble + 1D) / (maxIteration * totalCount.toDouble + 1D))
      if (alpha < startAlpha * 1e-4) alpha = startAlpha * 1e-4

      var countAndWordList = reader.readWordListAndRandomlyDiscardFrequentWords(batchSize, sample)
      count += countAndWordList._1
      var wordList = countAndWordList._2
      while (!wordList.isEmpty) {
        for (index <- 0 until wordList.size) {
          val randomRightContext = random.nextInt(window)
          val words = reader.readWindow(wordList, index).slice(randomRightContext, 2 * window + 1 - randomRightContext)
          val wordIndex = words(words.size / 2)
          if (wordIndex > 0 && words.size > 2) {
            val input = words.toArray
            input(words.size / 2) = 0

            val negativeSamples = 25
            val output = new Array[(Int, Int)](negativeSamples)
            output(0) = (wordIndex, 1)
            for (i <- 1 until negativeSamples) {
              var index = random.nextInt(vocab.size)
              if (index == wordIndex) index = random.nextInt(vocab.size)
              output(i) = (index, 0)
            }

            // train
            network.learn(input.filter(in => in > 0), output, alpha)
          }
        }

        val progress = count.toDouble / totalCount.toDouble
        //        if(progress > 1D) {
        //          println(s"$endAt, $count, $startAt")
        //        }
        alpha = startAlpha * (1D - (count.toDouble + 1D) / (maxIteration * totalCount.toDouble + 1D))
        if (alpha < startAlpha * 1e-4) alpha = startAlpha * 1e-4
        if (alpha >= startAlpha) alpha = startAlpha
        print("Iteration: %2d    Alpha %2.5f    progress: %2.5f\r".format(iteration, alpha, progress))

        countAndWordList = reader.readWordListAndRandomlyDiscardFrequentWords(batchSize, sample)
        count += countAndWordList._1
        wordList = countAndWordList._2
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
  while (iteration < maxIteration && hasImprovement) {
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