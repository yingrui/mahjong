package websiteschema.mpsegment.dict

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

class TrieNodeTest {

  @Test
  def should_insert_word_by_key() {
    val root = new TrieNode(0)
    root.insert("ab", 1)
    Assert.assertEquals(1, root.descendant.length)
    Assert.assertEquals(1, root.descendant(0).descendant.length)
    root.insert("ac", 2)
    Assert.assertEquals(1, root.descendant.length)
    Assert.assertEquals(2, root.descendant(0).descendant.length)
  }

  @Test
  def should_insert_word_multi_times() {
    val root = new TrieNode(0)
    root.insert("ab", 1)
    Assert.assertEquals(1, root.descendant.length)
    Assert.assertEquals(1, root.descendant(0).descendant.length)
    root.insert("ab", 1)
    Assert.assertEquals(1, root.descendant.length)
    Assert.assertEquals(1, root.descendant(0).descendant.length)
  }

  @Test
  def should_find_word() {
    val root = new TrieNode(0)
    root.insert("abc", 2)
    root.insert("ab", 1)
    val ab = root.search("ab")
    Assert.assertNotNull(ab)
    Assert.assertEquals(1, ab.getIndex)
    val abc = root.search("abc")
    Assert.assertNotNull(abc)
    Assert.assertEquals(2, abc.getIndex)
  }

  @Test
  def should_compare_trie_index_by_key() {
    val a = new TrieNode('a', 1)
    val c = new TrieNode('c', 1)
    Assert.assertTrue(a.compareTo(c) < 0)
  }
}
