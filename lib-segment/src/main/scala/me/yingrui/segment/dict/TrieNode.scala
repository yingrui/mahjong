package me.yingrui.segment.dict

/**
  * Created by twer on 04/03/2017.
  */
class TrieNode(key: Char, var index: Int) extends Comparable[TrieNode] {
  def this(index: Int) = this('\0', index)

  var descendant: Array[TrieNode] = new Array[TrieNode](0)

  def getKey = key
  def getIndex = index

  def search(array: Seq[Char]): TrieNode = {
    if (array.length == 1) {
      search(array.head)
    } else {
      val child = search(array.head)
      if (null != child) child.search(array.tail) else null
    }
  }

  def search(ch: Char): TrieNode = {
    val child: TrieNode = new TrieNode(ch, -1)
    val trieIndex = java.util.Arrays.binarySearch(descendant, child, TrieNode.comparator)
    if (trieIndex >= 0) {
      descendant(trieIndex)
    } else {
      null
    }
  }

  def insert(array: Seq[Char], index: Int) {
    if (array.length == 1) {
      findChildAndCreateIfNotExisted(array.head, index)
    } else {
      val child = findChildAndCreateIfNotExisted(array.head, -1)
      child.insert(array.tail, index)
    }
  }

  private def findChildAndCreateIfNotExisted(ch: Char, index: Int): TrieNode = {
    var child: TrieNode = new TrieNode(ch, index)
    val trieIndex = java.util.Arrays.binarySearch(descendant, child, TrieNode.comparator)
    if (trieIndex >= 0) {
      child = descendant(trieIndex)
      if (index >= 0) {
        child.index = index
      }
    } else {
      addTrieIndexNode(child)
    }
    child
  }

  private def addTrieIndexNode(child: TrieNode) {
    descendant = (descendant.toList :+ child).sorted.toArray
  }

  override def compareTo(other: TrieNode) = {
    key.compareTo(other.getKey)
  }
}

object TrieNode {
  val comparator = new java.util.Comparator[TrieNode] {
    override def compare(o1: TrieNode, o2: TrieNode): Int = o1.compareTo(o2)
  }
}