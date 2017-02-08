package me.yingrui.segment.core.disambiguation

import me.yingrui.segment.core.SegmentWorker
import me.yingrui.segment.tools.PFRCorpusLoader
import me.yingrui.segment.tools.accurary.SegmentResultComparator
import me.yingrui.segment.util.FileUtil._

object DisambiguationModelApp extends App {
  val resource = "./lib-segment/src/test/resources/PFR-199801-utf-8.txt"
  val writer = System.out
  val loader = PFRCorpusLoader(getResourceAsStream(resource))

  val segmenter = SegmentWorker("separate.xingming" -> "true")
  loader.load(expect => {
    val originalString = expect.toOriginalString()
    val actual = segmenter.segment(originalString)

    val hooker = new DisambiguationToSerialLabels(expect, actual)
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    val labels = hooker.serialLabels
    writer.println(labels.head._1 + " " + labels.head._2)
    labels.tail.foreach(t => writer.println(t._1 + " " + t._2))
    writer.println()
  })
}
