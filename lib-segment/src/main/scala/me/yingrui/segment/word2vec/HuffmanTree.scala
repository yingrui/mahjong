package me.yingrui.segment.word2vec

import scala.collection.mutable.ListBuffer

class HuffmanTreeNode(val index: Int, val weight: Int, var code: Int, var parent: Option[HuffmanTreeNode], val left: Option[HuffmanTreeNode], val right: Option[HuffmanTreeNode]) {

}

class HuffmanTree {

  val nodes = ListBuffer[HuffmanTreeNode]()

  def add(index: Int, weight: Int): Unit = {
    nodes += new HuffmanTreeNode(index, weight, 0, None, None, None)
  }

  def getParentIndexes(index: Int): List[Int] = {
    nodes.find(node => node.index == index) match {
      case Some(node) => getParentIndexes(node, List())
      case _ => List[Int]()
    }
  }

  private def getParentIndexes(node: HuffmanTreeNode, codes: List[Int]): List[Int] = {
    node.parent match {
      case Some(parent) => getParentIndexes(parent, codes :+ parent.index)
      case _ => codes
    }
  }

  def getCode(index: Int): List[Int] = {
    nodes.find(node => node.index == index) match {
      case Some(node) => getCode(node, List())
      case _ => List[Int]()
    }
  }

  private def getCode(node: HuffmanTreeNode, codes: List[Int]): List[Int] = {
    node.parent match {
      case Some(parent) => getCode(parent, node.code :: codes)
      case _ => codes
    }
  }

  def build(): HuffmanTreeNode = {
    var index = 0
    var nodeList = new ListBuffer[HuffmanTreeNode]()
    nodeList ++= nodes.sortBy(node => node.weight)
    while(nodeList.size > 1) {
      build(index, nodeList)
      index += 1
      if(index % 1000 == 0) {
        print("building huffman tree, progress: %2.2f\r".format(index.toDouble / nodes.size.toDouble))
      }
    }
    println()
    nodeList.head
  }

  private def build(index: Int, nodes: ListBuffer[HuffmanTreeNode]): Unit = {
    val smallest = nodes.take(2)
    nodes.remove(0, 2)

    val merged = merge(index, smallest(0), smallest(1))
    val insertIndex = nodes.indexWhere(node => node.weight > merged.weight)

    if(insertIndex < 0)
      nodes += merged
    else
      nodes.insert(insertIndex, merged)
  }

  private def merge(index: Int, node: HuffmanTreeNode, other: HuffmanTreeNode): HuffmanTreeNode = {
    val left = if(node.weight > other.weight) node else other
    val right = if(node.weight <= other.weight) node else other
    val parent = new HuffmanTreeNode(index, left.weight + right.weight, 0, None, Some(left), Some(right))
    left.parent = Some(parent)
    left.code = 1
    right.parent = Some(parent)
    parent
  }

}
