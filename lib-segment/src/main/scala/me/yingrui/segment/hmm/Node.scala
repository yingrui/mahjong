package me.yingrui.segment.hmm

import me.yingrui.segment.util.ISerialize
import me.yingrui.segment.util.SerializeHandler

class Node extends ISerialize {

  private var index = -1
  private var name: String = null

  def getIndex(): Int =  index

  def setIndex(index: Int) {
    this.index = index
  }

  def getName(): String = name

  def setName(name: String) {
    this.name = name
  }

  override def save(writeHandler: SerializeHandler) {
    writeHandler.serializeString(name)
    writeHandler.serializeInt(index)
  }

  override def load(readHandler: SerializeHandler) {
    name = readHandler.deserializeString()
    index = readHandler.deserializeInt()
  }
}

object Node {
  def apply() = new Node()

  def apply(name: String) = {
    val node = new Node()
    node.name = name
    node
  }

  def apply(name: String, index: Int) = {
    val node = new Node()
    node.name = name
    node.index = index
    node
  }
}
