package me.yingrui.segment.tools.ner

import java.io.{FileOutputStream, OutputStreamWriter, PrintWriter}

import me.yingrui.segment.core.SegmentWorker
import me.yingrui.segment.tools.CorpusLoader
import me.yingrui.segment.tools.accurary.SegmentResultComparator
import me.yingrui.segment.util.FileUtil._

object PFRCorpusToSerialLabelApp extends App {
  val resource = "./lib-segment/src/test/resources/PFR-199801-utf-8.txt"
  val writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream("ner-corpus.txt"), "utf-8"))
  val loader = CorpusLoader(getResourceAsStream(resource))

  val segmenter = SegmentWorker("recognize.chinesename" -> "false")
  loader.load(expect => {
    val originalString = expect.toOriginalString()
    val actual = segmenter.segment(originalString)

    val hooker = new PRFCorpusToSerialLabelCompareHooker(expect, actual)
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    val labels = hooker.serialLabels
    writer.println("Head"+labels.head._1 + " " + labels.head._2)
    labels.tail.foreach(t => writer.println(t._1 + " " + t._2))
    writer.println()
  })
}

