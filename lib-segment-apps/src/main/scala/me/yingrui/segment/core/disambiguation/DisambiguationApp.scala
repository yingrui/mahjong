package me.yingrui.segment.core.disambiguation

import java.io.{BufferedReader, InputStreamReader}

import me.yingrui.segment.conf.SegmentConfiguration
import me.yingrui.segment.core.SegmentWorker
import me.yingrui.segment.crf.{CRFClassifier, CRFCorpus, CRFModel, CRFViterbi}
import me.yingrui.segment.filter.SegmentResultFilter
import me.yingrui.segment.filter.disambiguation.CRFDisambiguationFilter
import me.yingrui.segment.tools.accurary.SegmentAccuracy
import me.yingrui.segment.tools.accurary.SegmentErrorType._

object DisambiguationApp extends App {

  if (args.isEmpty) {
    println(
      """
        |Usage:
        | --corpus-file : input text file which contains segmented Chinese sentences (line by line).
        | --train-file  : input labeled disambiguation sentences
        | --model       : trained crf model, the model could be empty
        | --debug       : whether print debug messages
        |Default Parameter:
        | --corpus-file ./lib-segment/src/test/resources/PFR-199801-utf-8.txt --train-file disambiguation-corpus.txt
      """.
        stripMargin)
  }

  val corpusFile = if (args.indexOf("--corpus-file") >= 0) args(args.indexOf("--corpus-file") + 1) else "./lib-segment/src/test/resources/PFR-199801-utf-8.txt"
  val trainFile = if (args.indexOf("--train-file") >= 0) args(args.indexOf("--train-file") + 1) else "disambiguation-corpus.txt"
  val modelFile = if (args.indexOf("--model") >= 0) args(args.indexOf("--model") + 1) else ""
  val debug = args.indexOf("--debug") >= 0

  val config = SegmentConfiguration(Map("separate.xingming" -> "true", "minimize.word" -> "true"))
  val segmentWorker = if (!modelFile.isEmpty) {
    println("model loading...")
    val model = CRFModel(modelFile)
    println("model loaded...")
    closeTest(model, trainFile)


    val filter = new SegmentResultFilter(config)
    filter.addFilter(new CRFDisambiguationFilter(new CRFClassifier(model)))
    SegmentWorker(config, filter)
  } else {
    SegmentWorker(config)
  }

  val segmentAccuracy = new SegmentAccuracy(corpusFile, segmentWorker)
  segmentAccuracy.checkSegmentAccuracy()
  println("Recall rate of segment is: " + segmentAccuracy.getRecallRate())
  println("Precision rate of segment is: " + segmentAccuracy.getPrecisionRate())
  println("F is: " + segmentAccuracy.F())
  println("There are " + segmentAccuracy.getWrong() + " errors and total expect word is " + segmentAccuracy.getTotalWords() + " when doing accuracy test.")

  if (debug) {
    println("There are " + segmentAccuracy.getErrorAnalyzer(UnknownWord).getErrorOccurTimes() + " errors because of new word.")
    println("There are " + segmentAccuracy.getErrorAnalyzer(NER_NR).getErrorOccurTimes() + " errors because of name recognition.")
    println("There are " + segmentAccuracy.getErrorAnalyzer(NER_NS).getErrorOccurTimes() + " errors because of place name recognition.")
    println("There are " + segmentAccuracy.getErrorAnalyzer(ContainDisambiguate).getErrorOccurTimes() + " errors because of contain disambiguate.")
    println("There are " + segmentAccuracy.getErrorAnalyzer(Other).getErrorOccurTimes() + " other errors")
  }
  println("\nType QUIT to exit:")
  val inputReader = new BufferedReader(new InputStreamReader(System.in))
  var line = inputReader.readLine()
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
    var index = 0D
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
          if (debug) {
            errors.foreach(println(_))
            println()
          }
        } catch {
          case _: Exception =>
        }
      }

      if (!debug) {
        index = index + 1
        val progress = (index / corpus.docs.size.toDouble * 100D).toInt
        print(s"\rProgress: $progress %")
      }
    })
    println("\ntotal: " + total + " correct: " + correctCount + " error: " + (total - correctCount) + " rate: " + correctCount.toDouble / total.toDouble)
  }

}
