package me.yingrui.segment.core

import org.junit.Assert._
import org.junit.Test

class SegmentWorkerTest {

  @Test
  def should_Know_How_to_Break_ChinaGreatWall() {
    val str = "中国长城"
    val worker = SegmentWorker()
    val words = worker.tokenize(str)
    assertEquals("中国 长城", words.mkString(" "))
  }

}
