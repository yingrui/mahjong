package me.yingrui.segment.filter

import me.yingrui.segment.core.SegmentResult

trait ISegmentFilter {

    def filtering()

    def setSegmentResult(segmentResult: SegmentResult)
}