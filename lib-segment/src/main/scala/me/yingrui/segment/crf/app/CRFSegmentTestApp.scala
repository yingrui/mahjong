package me.yingrui.segment.crf.app

import java.io.{InputStreamReader, BufferedReader}

import me.yingrui.segment.crf.{CRFSegmentWorker, CRFCorpus, CRFModel, CRFViterbi}

object CRFSegmentTestApp extends App {

  val trainFile = if (args.indexOf("--train-file") >= 0) args(args.indexOf("--train-file") + 1) else "lib-segment/training-10000.txt"
  val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "segment-crf.m"

  print("loading model...\r")
  val model = CRFModel(saveFile)
  println("model loaded     ")

  closeTest(model, trainFile)

  println("\nType QUIT to exit:")
  val inputReader = new BufferedReader(new InputStreamReader(System.in))
  var line = inputReader.readLine()
  val segmentWorker = CRFSegmentWorker(model)

  while (line != null && !line.equals("QUIT")) {
    if (!line.isEmpty) {
      val result = segmentWorker.tokenize(line)
      println(result.mkString(" "))
    }
    line = inputReader.readLine()
  }

  def closeTest(model: CRFModel, trainFile: String): Unit = {
    var total = 0
    var correctCount = 0
    val corpus = CRFCorpus(trainFile, false, true, model.featureRepository, model.labelRepository)
    println("test corpus loaded")
    val classifier = new CRFViterbi(model)
    corpus.docs.foreach(doc => {
      val result = classifier.calculateResult(doc.data).getBestPath
      total += result.length
      var success = true
      for (index <- 0 until result.length) yield {
        val label = doc.label(index)
        correctCount += (if (label == result(index)) 1 else 0)

        if (label != result(index)) success = false
      }

      if (!success) {
        try {
          val errors = (0 until result.length)
            .map(index => {
            def isCurrentOrNextFailed(currentIndex: Int) = {
              val line = doc.rowData(currentIndex)
              val label = model.labelRepository.getFeature(result(currentIndex))
              val failed = !line.endsWith(label)
              if (!failed && currentIndex < result.length - 1) {
                val nextLine = doc.rowData(currentIndex + 1)
                val nextLabel = model.labelRepository.getFeature(result(currentIndex + 1))
                !nextLine.endsWith(nextLabel)
              } else {
                failed
              }
            }

            if (isCurrentOrNextFailed(index)) {
              doc.rowData(index) + " " + model.labelRepository.getFeature(result(index)) + " --"
            } else {
              doc.rowData(index) + " " + model.labelRepository.getFeature(result(index))
            }
          })
          errors.foreach(println(_))
          println()
        } catch {
          case _: Exception =>
        }
      }
    })
    println("total: " + total + " correct: " + correctCount + " error: " + (total - correctCount) + " rate: " + correctCount.toDouble / total.toDouble)
  }


}
