package me.yingrui.segment.dict

import org.junit.Assert
import org.junit.Test

class TrieDictionaryTest {

  @Test
  def should_get_word_by_word_string() {
    val dictionary = new TrieDictionary()
    dictionary.addWord(new WordImpl("abc"))
    val wordABC = dictionary.getWord("abc")
    Assert.assertNotNull(wordABC)
    Assert.assertEquals("abc", wordABC.getWordName())
  }

  @Test
  def should_get_multi_words_by_word_string() {
    val dictionary = new TrieDictionary()
    dictionary.addWord(new WordImpl("a"))
    dictionary.addWord(new WordImpl("ab"))
    dictionary.addWord(new WordImpl("abc"))
    val words = dictionary.getWords("abc")
    Assert.assertNotNull(words)
    Assert.assertEquals(3, words.length)
  }

  @Test
  def should_return_all_words_as_an_iterator() {
    val dictionary = new TrieDictionary()

    val a: WordImpl = new WordImpl("a")
    val ab: WordImpl = new WordImpl("ab")
    val abc: WordImpl = new WordImpl("abc")
    dictionary.addWord(a)
    dictionary.addWord(ab)
    dictionary.addWord(abc)

    val words = dictionary.iterator()
    Assert.assertNotNull(words)
    Assert.assertEquals(3, words.length)
    Assert.assertEquals(a, words(0))
    Assert.assertEquals(ab, words(1))
    Assert.assertEquals(abc, words(2))
  }
}


