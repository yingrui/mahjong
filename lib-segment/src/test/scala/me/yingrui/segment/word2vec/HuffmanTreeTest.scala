package me.yingrui.segment.word2vec

import org.scalatest.{FunSuite, Matchers}

class HuffmanTreeTest extends FunSuite with Matchers {

  test("should build huffman tree") {
    val tree = new HuffmanTree()
    val index1 = 1
    val weight1 = 10
    val index2 = 2
    val weight2 = 20
    tree.add(index1, weight1)
    tree.add(index2, weight2)
    tree.add(3, 40)

    tree.nodes.forall(node => node.parent == None) should be (true)

    val root = tree.build()
    root.weight should be (70)
    tree.nodes.forall(node => node.parent != None) should be (true)

    tree.getCode(1) should be (List(0, 0))
    tree.getCode(2) should be (List(0, 1))
    tree.getCode(3) should be (List(1))

    tree.getParentIndexes(1) should be (List(0, 1))
    tree.getParentIndexes(2) should be (List(0, 1))
    tree.getParentIndexes(3) should be (List(1))
  }
}
