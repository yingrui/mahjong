package me.yingrui.segment.filter.ner

import me.yingrui.segment.core.SegmentResult
import me.yingrui.segment.filter.{ForeignName, NameEntityRecognizerStatisticResult}
import me.yingrui.segment.neural._
import me.yingrui.segment.math.Matrix
import me.yingrui.segment.dict.POSUtil
import collection.mutable
import me.yingrui.segment.util.WordUtil

trait RecognizerCreator {
  def create(segmentResult: SegmentResult): NameEntityRecognizer
}