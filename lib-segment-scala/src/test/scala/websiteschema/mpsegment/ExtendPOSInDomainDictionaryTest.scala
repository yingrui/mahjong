package websiteschema.mpsegment

import dict.domain.{DomainDictionary, DomainDictFactory}
import dict.{DictionaryFactory, POSUtil}
import websiteschema.mpsegment.core.SegmentEngine
import org.junit.{Test, Assert}

class ExtendPOSInDomainDictionaryTest {

  @Test
  def should_set_extra_POS_to_domain_word() {
    SegmentEngine()

//    val factory: DomainDictFactory = new DomainDictFactory()
//    factory.buildDictionary()
//    val dictionary: DomainDictionary = factory.getDomainDictionary()
//
//    dictionary.pushWord("高峰", null, "NR", 100, 100001)
//
//    val word = dictionary.getWord("高峰")
//    val numberOfPOS = word.getPOSArray().getWordPOSTable().length
//    println(numberOfPOS)
//    Assert.assertTrue(numberOfPOS > 1);

    try {
      val str = "我的同学叫高峰,高峰同志,高峰经理,科学高峰"
      val worker = SegmentEngine().getSegmentWorker()
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
