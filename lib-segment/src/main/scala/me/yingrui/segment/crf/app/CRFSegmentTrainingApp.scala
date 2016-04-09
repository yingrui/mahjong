package me.yingrui.segment.crf.app

import me.yingrui.segment.crf.{CRFCorpus, CRFModel}

object CRFSegmentTrainingApp extends App {

  val trainFile = if (args.indexOf("--train-file") >= 0) args(args.indexOf("--train-file") + 1) else "lib-segment/training-10000.txt"
  val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "segment-crf.m"

  val corpusForTrain = CRFCorpus(trainFile)
  val model = CRFModel.build(corpusForTrain)
  println("saving model")
  CRFModel.save(model, saveFile)
  println("model saved")
}
