package websiteschema.mpsegment.performance

import junit.framework.Assert
import org.junit.{Ignore, Test}
import websiteschema.mpsegment.core.{SegmentResult, SegmentEngine}
import websiteschema.mpsegment.tools.accurary.ErrorAnalyzer
import websiteschema.mpsegment.tools.accurary.SegmentAccuracy
import websiteschema.mpsegment.tools.accurary.SegmentErrorType
import java.io.InputStream
import websiteschema.mpsegment.util.FileUtil._
import scala.Some
import io.Source
import websiteschema.mpsegment.pinyin.WordToPinyinClassfierFactory
import websiteschema.mpsegment.dict.POSUtil

@Ignore
class NERAccuracyTest {

  val segmentWorker =
    SegmentEngine().getSegmentWorker(
      "separate.xingming -> false",
      "minimize.word -> true"
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
