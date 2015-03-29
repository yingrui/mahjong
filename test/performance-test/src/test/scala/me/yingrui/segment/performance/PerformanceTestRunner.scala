package me.yingrui.segment.performance

import me.yingrui.segment.core.SegmentWorker
import io.Source

object PerformanceTestRunner extends App {

  println("In case you want to run for performance profile.")
  println("This program would not stop unless you kill it.")
  var segmentWorker = SegmentWorker("segment.context -> true")
  segmentWorker.segment("世界您好！")
  while (true) {
    val reader = Source.fromFile(getClass().getClassLoader().getResource("Sophie's_World.txt").toURI, "UTF-8")

    for (line <- reader.getLines()) {
      segmentWorker.segment(line)
    }

    reader.close()
  }

}
