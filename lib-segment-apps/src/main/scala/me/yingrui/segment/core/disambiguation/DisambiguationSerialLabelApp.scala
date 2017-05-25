package me.yingrui.segment.core.disambiguation

import java.io.{FileOutputStream, OutputStreamWriter, PrintWriter}

import me.yingrui.segment.core.SegmentWorker
import me.yingrui.segment.core.disambiguation.DisambiguationToSerialLabels.LABEL_A
import me.yingrui.segment.dict.POSUtil._
import me.yingrui.segment.tools.CorpusLoader
import me.yingrui.segment.tools.accurary.SegmentResultComparator
import me.yingrui.segment.util.CharCheckUtil.isChinese
import me.yingrui.segment.util.FileUtil._

object DisambiguationSerialLabelApp extends App {

  if (args.isEmpty) {
    println(
      """
        |Usage:
        | --train-file : input text file which contains segmented Chinese sentences (line by line).
        | --save-file  : output file which with disambiguation labels.
        |Default Parameter:
        | --train-file ./lib-segment/src/test/resources/PFR-199801-utf-8.txt --save-file disambiguation-corpus.txt
      """.stripMargin)
  }

  val trainFile = if (args.indexOf("--train-file") >= 0) args(args.indexOf("--train-file") + 1) else "./lib-segment/src/test/resources/PFR-199801-utf-8.txt"
  val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "disambiguation-corpus.txt"
  val writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(saveFile), "utf-8"))
  val loader = CorpusLoader(getResourceAsStream(trainFile))

  val segmenter = SegmentWorker(
    "separate.xingming" -> "true",
    "segment.lang.en" -> "false",
    "recognize.pinyin" -> "false",
    "recognize.partOfSpeech" -> "false",
    "core.dictionaryfile" -> "me/yingrui/segment/dict-minimum.txt",
    "minimize.word" -> "true"
  )
  loader.load(expect => {
    val originalString = expect.toOriginalString()
    val actual = segmenter.segment(originalString)

    val hooker = new DisambiguationToSerialLabels(expect, actual)
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    val labels = hooker.serialLabels
    if (isSegmentResultContainErrors(labels)) {
      labels.filter(byPOS).zipWithIndex.foreach(_ match {
        case ((word, label, pos), index) => {
          val couldSkip = label == LABEL_A && nextLabelsAreCorrect(index, labels) && previousLabelsAreCorrect(index, labels)
          if (!couldSkip) {
            writer.println(word + " " + label)
          }
        }
        case _ =>
      })
      writer.println()
      writer.flush()
    }
  })

  private def byPOS(label:(String, String, Int)): Boolean = {
    val pos = label._3
    val word = label._1
    pos != POS_W && pos != POS_T && isChinese(word)
  }

  private def nextLabelsAreCorrect(index: Int, labels: List[(String, String, Int)]): Boolean = {
    (labels.size - index > 3) && labels(index + 1)._2 == LABEL_A && labels(index + 2)._2 == LABEL_A
  }

  private def previousLabelsAreCorrect(index: Int, labels: List[(String, String, Int)]): Boolean = {
    (index - 2 >= 0) && labels(index - 1)._2 == LABEL_A && labels(index - 2)._2 == LABEL_A
  }

  private def isSegmentResultContainErrors(labels: List[(String, String, Int)]): Boolean = !labels.map(_._2).forall(_ == LABEL_A)
}
