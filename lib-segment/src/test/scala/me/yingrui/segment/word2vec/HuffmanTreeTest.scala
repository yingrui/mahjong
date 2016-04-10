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
    tree.add(3, 30)
    tree.add(4, 40)

    tree.nodes.forall(node => node.parent == None) should be (true)

    val root = tree.build()
    println(tree.getCode(1))
    println(tree.getCode(2))
    println(tree.getCode(3))
    println(tree.getCode(4))
    root.weight should be (100)
    tree.nodes.forall(node => node.parent != None) should be (true)

    tree.getCode(1) should be (List(1, 1, 0))
    tree.getCode(2) should be (List(1, 1, 1))
    tree.getCode(3) should be (List(1, 0))
    tree.getCode(4) should be (List(0))

    tree.getParentIndexes(1) should be (List(0, 1, 2))
    tree.getParentIndexes(2) should be (List(0, 1, 2))
    tree.getParentIndexes(3) should be (List(1, 2))
    tree.getParentIndexes(4) should be (List(2))
  }
}
