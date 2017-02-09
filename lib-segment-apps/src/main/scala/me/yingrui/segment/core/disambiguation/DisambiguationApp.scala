package me.yingrui.segment.core.disambiguation

import java.io.{BufferedReader, InputStreamReader}

import me.yingrui.segment.core.SegmentWorker
import me.yingrui.segment.crf.CRFModel

object DisambiguationApp extends App {
  val saveFile = if (args.indexOf("--save-file") >= 0) args(args.indexOf("--save-file") + 1) else "disambiguation.m"

  println("model loading...")
  val model = CRFModel(saveFile)
  println("model loaded...")

  println("\nType QUIT to exit:")
  val inputReader = new BufferedReader(new InputStreamReader(System.in))
  var line = inputReader.readLine()
  val segmentWorker = SegmentWorker()

  while (line != null && !line.equals("QUIT")) {
    if (!line.isEmpty) {
      val result = segmentWorker.tokenize(line)
      println(result.mkString(" "))
    }
    line = inputReader.readLine()
  }

}
