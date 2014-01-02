package websiteschema.mpsegment.dict

import org.junit.Assert
import org.junit.Test
import websiteschema.mpsegment.core.SegmentWorker
import websiteschema.mpsegment.dict.domain.DomainDictFactory

class UserDictionaryTest {

  @Test
  def should_Loaded_Some_Words_from_User_Dictionary() {
    val str = "贝因美是中国品牌"
    val worker = SegmentWorker()
    worker.setUseDomainDictionary(true)
    val words = worker.segment(str)
    println(words)
    Assert.assertEquals(words.length(), 4)
    Assert.assertEquals(words.getWord(0), "贝因美")
    Assert.assertEquals(words.getWord(1), "是")
    val dd = DomainDictFactory().getDomainDictionary()

    assert(dd.iterator().size > 0)
  }
}
