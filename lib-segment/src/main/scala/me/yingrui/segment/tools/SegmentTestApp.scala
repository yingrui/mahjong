package me.yingrui.segment.tools

import me.yingrui.segment.core.SegmentWorker

import scala.io.Source

object SegmentTestApp extends App {

  val inputFile = if (args.indexOf("--input") >= 0) args(args.indexOf("--input") + 1) else "pku_training.utf8"

  val segmentWorker = SegmentWorker(
    "separate.xingming -> true",
    "minimize.word -> true"
  )

  val source = Source.fromFile(inputFile, "utf-8")
  source.getLines().map(_.replaceAll(" ", "")).foreach(line => {
    val result = segmentWorker.segment(line)
    println(result.getWords().map(_.name).mkString("  "))
  })
}
