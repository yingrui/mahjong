package me.yingrui.segment.word2vec.apps

import me.yingrui.segment.word2vec.MLPSegment

object MLPSegmentTest extends App {

  print("loading...\r")
  val segment = new MLPSegment()
  segment.trainAndTest

}
