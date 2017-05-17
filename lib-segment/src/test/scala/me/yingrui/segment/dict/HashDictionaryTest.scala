package me.yingrui.segment.dict

import org.junit.Assert
import org.junit.Test

class HashDictionaryTest {

  DictionaryFactory().loadDictionary()
  private val hashDictionary = DictionaryFactory().getCoreDictionary

  @Test
  def should_Load_Core_Dictionary() {
    val words = hashDictionary.getWords("乒乓球")
    assert(null != words)
    Assert.assertEquals(words(0).getWordName(), "乒乓球")
    Assert.assertEquals(156, words(0).getOccuredSum())
    Assert.assertEquals(156, words(0).getOccuredCount("N"))
    Assert.assertEquals(words(1).getWordName(), "乒乓")
    for (word <- words) {
      println("词：" + word.getWordName() + "\n" + word.getPOSArray())
    }

    val iterator = hashDictionary.iterator()
    val count = iterator.size
    Assert.assertEquals(88027, count)
  }

}
