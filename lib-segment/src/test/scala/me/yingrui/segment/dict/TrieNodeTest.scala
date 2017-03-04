package me.yingrui.segment.dict

import org.junit.{Assert, Test}

/**
  * Created by twer on 04/03/2017.
  */
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
