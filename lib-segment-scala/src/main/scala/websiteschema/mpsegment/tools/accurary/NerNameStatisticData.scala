package websiteschema.mpsegment.tools.accurary

import websiteschema.mpsegment.core.SegmentResult
import websiteschema.mpsegment.dict.POSUtil

object NerNameStatisticData {

  var nameCount = 0.0
  var recognizedNameCount = 0.0
  var correctRecognizedNameCount = 0.0

  def scanNameWordCount(segmentResult: SegmentResult) {
    segmentResult.foreach(wordAtom => {
      if (wordAtom.pos == POSUtil.POS_NR) nameCount += 1.0
    })
  }

  def scanRecognizedNameWordCount(segmentResult: SegmentResult) {
    segmentResult.foreach(wordAtom => {
      if (wordAtom.pos == POSUtil.POS_NR) recognizedNameCount += 1.0
    })
  }

  def print {
    println("Chinese name recognition precise rate: " + (correctRecognizedNameCount / recognizedNameCount) + " recall rate: " + (correctRecognizedNameCount / nameCount))
  }
}
