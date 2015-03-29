package me.yingrui.segment.tools.accurary

import me.yingrui.segment.core.SegmentResult
import me.yingrui.segment.dict.POSUtil

object NerNameStatisticData {

  var nameCount = 0.0
  var recognizedNameCount = 0.0
  var correctRecognizedNameCount = 0.0

  def scanNameWordCount(segmentResult: SegmentResult) {
    segmentResult.foreach(word => {
      if (word.pos == POSUtil.POS_NR) nameCount += 1.0
    })
  }

  def scanRecognizedNameWordCount(segmentResult: SegmentResult) {
    segmentResult.foreach(word => {
      if (word.pos == POSUtil.POS_NR) recognizedNameCount += 1.0
    })
  }

  def print {
    println("Chinese name recognition precise rate: " + (correctRecognizedNameCount / recognizedNameCount) + " recall rate: " + (correctRecognizedNameCount / nameCount))
  }
}
