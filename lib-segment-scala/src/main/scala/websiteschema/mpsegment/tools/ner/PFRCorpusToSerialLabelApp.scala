package websiteschema.mpsegment.tools.ner

import websiteschema.mpsegment.tools.PFRCorpusLoader
import websiteschema.mpsegment.util.FileUtil._
import websiteschema.mpsegment.core.SegmentEngine

object PFRCorpusToSerialLabelApp extends App {
  val resource = "./src/test/resources/PFR-199801-utf-8.txt"
  val loader = PFRCorpusLoader(getResourceAsStream(resource))

  val segmenter = SegmentEngine.apply().getSegmentWorker()
  loader.load(segmentResult => {
    val originalString = segmentResult.toOriginalString()
    val actualResult = segmenter.segment(originalString)
  })
}

