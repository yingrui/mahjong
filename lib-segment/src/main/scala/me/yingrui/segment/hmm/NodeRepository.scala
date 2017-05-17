package me.yingrui.segment.hmm

import me.yingrui.segment.util.ISerialize
import me.yingrui.segment.util.SerializeHandler

import scala.collection.mutable.ListBuffer

class NodeRepository extends ISerialize {

  private var repo = List[Node]()
  private var size = repo.size
  private val indexMap = new java.util.HashMap[String, Int]()

  def add(node: Node): Node = {
    val name = node.getName()
    if (!indexMap.containsKey(name)) {
      val index = repo.size
      node.setIndex(index)
      repo = repo :+ node
      size = repo.size
      indexMap.put(name, index)
      return node
    } else {
      return repo(indexMap.get(name))
    }
  }

  def contains(name: String): Boolean = indexMap.containsKey(name)

  def get(name: String): Node = {
    val index = indexMap.get(name)
    if (index > 0) repo(index)
    else if (name == repo(0).getName()) repo(0)
    else null
  }

  def get(index: Int): Node = {
    if (size > index) repo(index)
    else null
  }

  def keySet() = indexMap.keySet

  override def save(writeHandler: SerializeHandler) {
    val length = if (null != repo) repo.size else 0
    writeHandler.serializeInt(length)
    for (i <- 0 until length) {
      val node = repo(i)
      node.save(writeHandler)
    }
  }

  override def load(readHandler: SerializeHandler) {
    val length = readHandler.deserializeInt()
    if (length > 0) {
      val buffer = ListBuffer[Node]()
      for (i <- 0 until length) {
        val node = new Node()
        node.load(readHandler)
        buffer += node
        indexMap.put(node.getName(), node.getIndex())
      }
      repo = buffer.toList
      size = repo.size
    }
  }

  override def toString = indexMap.toString
}
