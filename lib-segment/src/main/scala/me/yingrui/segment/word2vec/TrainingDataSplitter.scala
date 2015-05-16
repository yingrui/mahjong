package me.yingrui.segment.word2vec

import java.io.{InputStreamReader, FileInputStream, File}

import me.yingrui.segment.util.SerializeHandler

import scala.collection.mutable.Map

class TrainingDataSplitter(val trainFile: String, val totalWordCount: Long, val vocab: Vocabulary) {

  def getDataFile(taskId: Int) = trainFile + "." + taskId + ".dat"

  def saveSplitDataWordCount(taskWordTotal: Map[String, Long]) {
    val writer = SerializeHandler(new File("split.word.count.dat"), SerializeHandler.WRITE_ONLY)
    writer.serializeMapStringLong(taskWordTotal)
    writer.close()
  }

  def loadSplitDataWordCount(taskCount: Int): Map[String, Long] = {
    val reader = SerializeHandler(new File("split.word.count.dat"), SerializeHandler.READ_ONLY)
    val wordTotalMap = reader.deserializeScalaMapStringLong()
    reader.close()
    if (wordTotalMap.size == taskCount) wordTotalMap else Map[String, Long]()
  }

  def split(taskWordTotal: Map[String, Long], taskCount: Int) {
    if(taskWordTotal.size != taskCount) {
      taskWordTotal.clear()
      print("Split training data...\r")
      val wordCountForEachTask = totalWordCount / taskCount
      val reader = new InputStreamReader(new FileInputStream(trainFile))
      val wordReader = new WordReader(reader)
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
          print("Split training data taskId: %d, progress: %2.3f\r".format(taskId, count.toDouble / totalWordCount.toDouble))
          taskWordTotal += (taskId.toString -> actualWordCount)
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
      saveSplitDataWordCount(taskWordTotal)
    }
  }

}
