package websiteschema.mpsegment.performance

import websiteschema.mpsegment.core.SegmentEngine
import io.Source

object PerformanceTestRunner extends App {

  println("In case you want to run for performance profile.")
  println("This program would not stop unless you kill it.")
  var segmentWorker = SegmentEngine().getSegmentWorker()
  segmentWorker.setRecognizePOS(true)
  segmentWorker.segment("世界您好！")
  while (true) {
    segmentWorker.setUseContextFreqSegment(true)
    val reader = Source.fromFile(getClass().getClassLoader().getResource("Sophie's_World.txt").toURI, "UTF-8")

    for (line <- reader.getLines()) {
      segmentWorker.segment(line)
    }

    reader.close()
  }

}
