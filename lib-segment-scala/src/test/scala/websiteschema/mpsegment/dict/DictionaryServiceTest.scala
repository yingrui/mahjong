package websiteschema.mpsegment.dict

import org.junit.Assert
import org.junit.Test

class DictionaryServiceTest {

  DictionaryFactory().loadDictionary()
  DictionaryFactory().loadEnglishDictionary()

  @Test
  def should_find_multi_words_in_dictionary() {
    val sentence = "不同凡响"
    val service = new DictionaryService(false, false, false, false)
    val result = service.lookup(sentence)
    Assert.assertEquals(2, result.matchedWordCount)
    Assert.assertEquals("不同凡响", result.firstMatchWord.getWordName())
    Assert.assertEquals("不同", result.the2ndMatchWord.getWordName())
  }

  @Test
  def should_return_exactly_same_word() {
    val sentence = "She"
    val service = new DictionaryService(false, false, false, true)
    val result = service.lookup(sentence)
    Assert.assertEquals("She", result.firstMatchWord.getWordName())
  }
}
