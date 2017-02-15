package me.yingrui.segment.core.disambiguation

import java.io.{FileOutputStream, OutputStreamWriter, PrintWriter}

import me.yingrui.segment.core.SegmentWorker
import me.yingrui.segment.core.disambiguation.DisambiguationToSerialLabels.LABEL_A
import me.yingrui.segment.dict.POSUtil._
import me.yingrui.segment.tools.PFRCorpusLoader
import me.yingrui.segment.tools.accurary.SegmentResultComparator
import me.yingrui.segment.util.CharCheckUtil.isChinese
import me.yingrui.segment.util.FileUtil._
import me.yingrui.segment.util.{CharCheckUtil, StringUtil}

object DisambiguationSerialLabelApp extends App {
  val resource = "./lib-segment/src/test/resources/PFR-199801-utf-8.txt"
  val writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream("disambiguation-corpus.txt"), "utf-8"))
  val loader = PFRCorpusLoader(getResourceAsStream(resource))

  val segmenter = SegmentWorker("separate.xingming" -> "true", "minimize.word" -> "true")
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
