package me.yingrui.segment.dict

import org.junit.Assert._
import org.junit.Test

class DictionaryServiceTest {

  DictionaryFactory().loadDictionary()
  DictionaryFactory().loadDomainDictionary()
  DictionaryFactory().loadEnglishDictionary()

  @Test
  def should_find_multi_words_in_dictionary() {
    val sentence = "不同凡响"
    val service = new DictionaryService(false, false, false, false)
    val result = service.lookup(sentence)
    assertEquals(2, result.matchedWordCount)
    assertEquals("不同凡响", result.firstMatchWord.getWordName())
    assertEquals("不同", result.the2ndMatchWord.getWordName())
  }

  @Test
  def should_find_multi_words_in_domain_dictionary() {
    val service = new DictionaryService(true, true, false, true)
    assertEquals("复方青黛胶囊", service.lookup("复方青黛胶囊").firstMatchWord.getWordName())
    assertEquals("复方", service.lookup("复方阿胶").firstMatchWord.getWordName())
  }

  @Test
  def should_return_exactly_same_word() {
    val sentence = "She"
    val service = new DictionaryService(false, false, false, true)
    val result = service.lookup(sentence)
    assertEquals("She", result.firstMatchWord.getWordName())
  }
}
