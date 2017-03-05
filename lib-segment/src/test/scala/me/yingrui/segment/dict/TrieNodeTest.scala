package me.yingrui.segment.dict

import org.junit.Assert.{assertArrayEquals, assertEquals, assertNotNull}
import org.junit.{Assert, Test}

class TrieNodeTest {

  @Test
  def should_insert_word_by_key() {
    val root = new TrieNode(0)
    root.insert("ab", 1)
    assertEquals(1, root.nodes.size)
    assertEquals(1, root.nodes.entrySet().iterator().next().getValue.nodes.size())
    root.insert("ac", 2)
    assertEquals(1, root.nodes.size)
    assertEquals(2, root.nodes.entrySet().iterator().next().getValue.nodes.size())
  }

  @Test
  def should_insert_word_multi_times() {
    val root = new TrieNode(0)
    root.insert("ab", 1)
    assertEquals(1, root.nodes.size)
    assertEquals(1, root.nodes.entrySet().iterator().next().getValue.nodes.size())
    root.insert("ab", 1)
    assertEquals(1, root.nodes.size)
    assertEquals(1, root.nodes.entrySet().iterator().next().getValue.nodes.size())
  }

  @Test
  def should_find_word() {
    val root = new TrieNode(0)
    root.insert("abc", 2)
    root.insert("ab", 1)
    val ab = root.search("ab")
    assertNotNull(ab)
    assertEquals(1, ab.getIndex)
    val abc = root.search("abc")
    assertNotNull(abc)
    assertEquals(2, abc.getIndex)
  }

  @Test
  def should_find_path() {
    val root = new TrieNode(0)
    root.insert("abc", 2)
    root.insert("ab", 1)
    val path = root.searchPath("abc")
    assertEquals(3, path.length)
    assertEquals("cba", path.map(_.getKey).mkString)
    assertArrayEquals(Array(2,1,-1), path.map(_.getIndex))
  }

  @Test
  def should_find_partial_path() {
    val root = new TrieNode(0)
    root.insert("abc", 2)
    root.insert("ab", 1)
    val path = root.searchPath("abd")
    assertEquals(2, path.length)
    assertEquals("ba", path.map(_.getKey).mkString)
    assertArrayEquals(Array(1,-1), path.map(_.getIndex))

    val path2 = root.searchPath("ad")
    assertEquals(1, path2.length)
    assertEquals("a", path2.map(_.getKey).mkString)
    assertArrayEquals(Array(-1), path2.map(_.getIndex))

    val path3 = root.searchPath("a")
    assertEquals(1, path3.length)
    assertEquals("a", path3.map(_.getKey).mkString)
    assertArrayEquals(Array(-1), path3.map(_.getIndex))
  }
}
