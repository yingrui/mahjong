package me.yingrui.segment.crf

import me.yingrui.segment.TestHelper._
import org.scalatest.{FunSuite, Matchers}

class CRFClassifierTest extends FunSuite with Matchers {

  val trainingText =
    """Hello  O
      |Jenny  PER
      |,  O
      |This O
      |is O
      |Ben PER
      |
      |Ben PER
      |This O
      |is O
      |Jenny  PER
    """.stripMargin

  val labelRepository = new FeatureRepository(false)
  val corpus = CRFCorpus(createTempFile(trainingText), true, true, new FeatureRepository(true), labelRepository)
  val model = CRFModel.build(corpus)

  test("should find the best labels") {
    val classifier = new CRFClassifier(model)
    val input = List("Hello", "Jenny", ",", "This", "is", "Ben")
    val labels = classifier.findBestLabels(input)
    labels should be(Array("O", "PER", "O", "O", "O", "PER"))
  }

  test("should label unknown word as default label") {
    val classifier = new CRFClassifier(model)
    val input = List("Hello", "Jenny", "UnknownWord", "This", "is", "Ben")
    val labels = classifier.findBestLabels(input)
    labels should be(Array("O", "PER", "O", "O", "O", "PER"))
  }


}
