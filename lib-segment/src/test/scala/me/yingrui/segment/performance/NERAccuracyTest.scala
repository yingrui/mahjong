package me.yingrui.segment.performance

import java.io.InputStream

import junit.framework.Assert
import me.yingrui.segment.core.SegmentWorker
import me.yingrui.segment.dict.POSUtil
import me.yingrui.segment.util.FileUtil._
import org.junit.{Ignore, Test}

import scala.io.Source

@Ignore
class NERAccuracyTest {

  val segmentWorker = SegmentWorker(
      "separate.xingming" -> "false",
      "minimize.word" -> "true"
    )

  @Test
  def should_be_higher_than_79_percent_when_recognize_chinese_name() {
    val resourceCnNames: InputStream = getResourceAsStream("chinese_names_all.txt")
    var total, accurate = 1

    for (str <- Source.fromInputStream(resourceCnNames).getLines()) {
      val namePattern = "([FM]) (.+)".r
      namePattern findFirstIn str match {
        case Some(namePattern(gender, name)) => {
          val result = segmentWorker.segment(name)
          if (result.length() == 1 && result.getPOS(0) == POSUtil.POS_NR) {
            accurate += 1
          }
          total += 1
        }
        case None =>
      }
    }

    val accurateRate = accurate.toDouble / total.toDouble
    println(accurateRate)
    Assert.assertTrue(accurateRate >= 0.7909D)
  }
}
