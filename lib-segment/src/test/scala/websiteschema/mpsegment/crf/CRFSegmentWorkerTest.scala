package websiteschema.mpsegment.crf

import org.scalatest.{Matchers, FunSuite}
import websiteschema.mpsegment.TestHelper._

class CRFSegmentWorkerTest extends FunSuite with Matchers {

  val trainingText =
    """长  B
      |城  E
      |中  B
      |国  E
    """.stripMargin
  val corpus = CRFCorpus(createTempFile(trainingText))
  val model = CRFModel.build(corpus)

  val segmentWorker = CRFSegmentWorker(model)

  test("CRF Segment Worker") {
    val sen = "中国长城"
    val actual = segmentWorker.segment(sen).map(word => word.name)
    assertResult(Array("中国", "长城"))(actual)
  }

}
