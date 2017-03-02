package me.yingrui.segment.core.disambiguation

import me.yingrui.segment.crf.{CRFCorpus, CRFModel}

object DisambiguationModelTrainingApp extends App {

  if (args.isEmpty) {
    println(
      """
        |Usage:
        | --train-file : input labeled disambiguation sentences
        | --save-file  : trained crf model
        |Default Parameter:
        | --train-file disambiguation-corpus.txt --save-file disambiguation.m
      """.
        stripMargin)
  }

  val trainFile = if (args.indexOf("--train-file") >= 0) args(args.indexOf("--train-file") + 1) else "disambiguation-corpus.txt"
  val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "disambiguation.m"

  val corpusForTrain = CRFCorpus(trainFile)
  val model = CRFModel.build(corpusForTrain)
  println("saving model")
  CRFModel.save(model, saveFile)
  println("model saved")
}
