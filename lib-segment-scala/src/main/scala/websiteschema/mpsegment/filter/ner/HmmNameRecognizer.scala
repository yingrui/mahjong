package websiteschema.mpsegment.filter.ner

import websiteschema.mpsegment.core.SegmentResult

class HmmNameRecognizer(val segmentResult: SegmentResult) extends NameEntityRecognizer {

  def recognizeNameWordBetween(begin: Int, end: Int): NameEntityRecognizeResult = {
    null
  }



}
