package me.yingrui.segment.dict

class TrieNode(key: Char, var index: Int) {
  def this(index: Int) = this('\0', index)

  val nodes: java.util.HashMap[Char, TrieNode] = new java.util.HashMap[Char, TrieNode]()

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

  def searchPath(array: Seq[Char]): Array[TrieNode] = {
    if (array.length == 1) {
      val node = search(array.head)
      if (node != null) Array(node) else Array()
    } else {
      val head = search(array.head)
      if (head != null) {
        head.searchPath(array.tail) ++ Array(head)
      } else {
        Array()
      }
    }
  }

  private def search(ch: Char): TrieNode = nodes.get(ch)

  def insert(array: Seq[Char], index: Int) {
    if (array.length == 1) {
      findChildAndCreateIfNotExisted(array.head, index)
    } else {
      val child = findChildAndCreateIfNotExisted(array.head, -1)
      child.insert(array.tail, index)
    }
  }

  private def findChildAndCreateIfNotExisted(ch: Char, index: Int): TrieNode = {
    val node = search(ch)
    if (null != node) {
      if (index >= 0) {
        node.index = index
      }
      node
    } else {
      val child = new TrieNode(ch, index)
      addTrieIndexNode(child)
      child
    }
  }

  private def addTrieIndexNode(child: TrieNode) {
    nodes.put(child.getKey, child)
  }
}