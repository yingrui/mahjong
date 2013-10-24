package websiteschema.mpsegment.tools.ner

import org.junit.{Before, Ignore, Test}
import websiteschema.mpsegment.pinyin.{WordToPinyinModel, WordToPinyinClassifier}


class WordToNERTest {

  private val classifier = new WordToPinyinClassifier()

  val model = new WordToPinyinModel()

  @Before
  def loadModel {
    model.load("ner.m")
    classifier.setModel(model)
  }

  @Test
  @Ignore
  def should_return_pinyin_with_given_Chinese_characters() {
    val result = classifier.classify(List("李","鹏", "乔", "石"))
    println(result)
  }
}
