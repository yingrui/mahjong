package me.yingrui.segment.word2vec

import java.lang.Math._

import scala.util.Random

class Word2VecTrainingWorker(val network: Word2VecNetwork,
                             val totalCount: Long,
                             val batchSize: Int,
                             val sample: Double,
                             val startAlpha: Double,
                             val maxIteration: Int,
                             val window: Int,
                             val vocabularySize: Int,
                             val random: Random) {

  def start(reader: WordIndexReader, currentIteration: Int): Unit = {
    var count = 0L
    var alpha = reduceAlpha(count, currentIteration)

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
          val output = new Array[(Int, Int)](negativeSamples + 1)
          output(0) = (wordIndex, 1)
          for (i <- 1 to negativeSamples) {
            var index = (abs(random.nextLong() / 65536) % vocabularySize).toInt
            if (index == wordIndex) index = random.nextInt(vocabularySize)
            output(i) = (index, 0)
          }

          // train
          network.learn(input.filter(in => in > 0), output, alpha)
        }
      }

      val progress = count.toDouble / totalCount.toDouble
      alpha = reduceAlpha(count, currentIteration)
      print("Iteration: %2d    Alpha %2.5f    progress: %2.5f\r".format(currentIteration, alpha, progress))

      countAndWordList = reader.readWordListAndRandomlyDiscardFrequentWords(batchSize, sample)
      count += countAndWordList._1
      wordList = countAndWordList._2
    }
  }

  private def reduceAlpha(count: Long, currentIteration: Int): Double = {
    val alpha = startAlpha * (1D - (count.toDouble + 1D) / (maxIteration * totalCount.toDouble + 1D)) * (1D / (currentIteration.toDouble + 1D))
    if (alpha < startAlpha * 1e-4)
      startAlpha * 1e-4
    else if (alpha >= startAlpha)
      startAlpha
    else
      alpha
  }

}
