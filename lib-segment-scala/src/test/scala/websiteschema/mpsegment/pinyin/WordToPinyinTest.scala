package websiteschema.mpsegment.pinyin

import org.junit.Assert
import org.junit.Test
import websiteschema.mpsegment.core.SegmentResult
import websiteschema.mpsegment.hmm.HmmModel

class WordToPinyinTest {

  val model = new HmmModel()
  model.load("websiteschema/mpsegment/wtp.m")
  private val classifier = new WordToPinyinClassifier(model)

  @Test
  def should_return_pinyin_with_given_Chinese_characters() {
    val result = classifier.classify("我们做到了")
    Assert.assertEquals("wo", result(0))
    Assert.assertEquals("men", result(1))
    Assert.assertEquals("zuo", result(2))
    Assert.assertEquals("dao", result(3))
    Assert.assertEquals("le", result(4))
  }

  @Test
  def should_keep_punctuation_in_result() {
    val result = classifier.classify("天涯啊！海角。。哈！")
    Assert.assertEquals("！", result(3))
    Assert.assertEquals("。", result(6))
    Assert.assertEquals("。", result(7))
    Assert.assertEquals("！", result(9))
  }

  @Test
  def should_keep_digtal_and_alphabetical_in_result() {
    val segmentResult = new SegmentResult(1)
    segmentResult.setWords(List[String]("１２日").toArray)
    classifier.classify(segmentResult)
    Assert.assertEquals("１２'ri", segmentResult.getPinyin(0))
  }

  @Test
  def should_keep_full_sharp_digtal_and_alphabetical_in_result() {
    val result = classifier.classify("AK47很厉害。")
    Assert.assertEquals("A", result(0))
    Assert.assertEquals("K", result(1))
    Assert.assertEquals("4", result(2))
    Assert.assertEquals("7", result(3))
  }

  @Test
  def should_recognize_poly_phones_such_as_zhanbu_luobo() {
    var result = classifier.classify("萝卜占卜")
    Assert.assertEquals("bo", result(1))
    Assert.assertEquals("bu", result(3))
    result = classifier.classify("银行和行走")
    Assert.assertEquals("hang", result(1))
    Assert.assertEquals("xing", result(3))
  }

  @Test
  def should_load_specified_pinyin() {
    classifier loadDictionary "pinyin.txt"
    Assert.assertEquals("nan", classifier.classify("囡")(0))
  }
}
