package me.yingrui.segment

import dict.POSUtil
import me.yingrui.segment.core.SegmentWorker
import org.junit.Test

class ExtendPOSInDomainDictionaryTest {

  @Test
  def should_set_extra_POS_to_domain_word() {
    try {
      val str = "我的同学叫高峰,高峰同志,高峰经理,科学高峰"
      val worker = SegmentWorker()
      val words = worker.segment(str)
      for (i <- 0 until words.length) {
        println(words.getWord(i) + " - " + POSUtil.getPOSString(words.getPOS(i)) + " - " + words.getDomainType(i))
      }
    } catch {
      case ex: Throwable =>
        ex.printStackTrace()
    }
  }
}
