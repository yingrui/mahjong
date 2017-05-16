package me.yingrui.segment.graph

import me.yingrui.segment.conf.SegmentConfiguration$
import me.yingrui.segment.dict.IWord

class Graph(size: Int) extends IGraph {

//  private val size = MPSegmentConfiguration.SectionSize
  private val matrix: SparseMatrix[IWord] = new SparseMatrix[IWord](size)

  override def addVertex() {
  }

  override def addEdge(head: Int, tail: Int, weight: Int, obj: IWord) {
    matrix.set(head, tail, weight, obj)
  }

  override def getEdgeWeight(head: Int, tail: Int): Int = {
    return matrix.get(head, tail)
  }

  override def getEdgeObject(head: Int, tail: Int): IWord = {
    matrix.getObject(head, tail) match {
      case Some(word) => word
      case _ => null
    }
  }

  override def getAdjacentVertices(vertex: Int): Array[Int] = {
    val adjacents = matrix.getNonZeroColumns(vertex)
    return if (null != adjacents) adjacents else new Array[Int](0)
  }

  override def getStopVertex(start: Int, end: Int): Int = {
    var stopVertex = start
    val gap = (end - start) + 1
    if (start <= 0 || end <= 0) {
      return -1
    }
    val maxDistance = new Array[Int](gap); // all Zeros at initialization
    for (rvn <- 0 until gap) {
      //rvn: relative vertex number
      val adjacentVertices = getAdjacentVertices(start + rvn)

      //find out the Length of the longest edges of one vertex, and calculate its distance to start vertex, and fill it in Array[maxDistance]
      for (adjacent <- 0 until adjacentVertices.length) {
        if (adjacentVertices(adjacent) - start > maxDistance(rvn)) {
          maxDistance(rvn) = adjacentVertices(adjacent) - start
        }
      }
    }

    var rvn = gap - 1
    while (rvn > 0) {
      var l1 = rvn
      var k2 = rvn - 1
      while (k2 >= 0) {
        if (maxDistance(k2) > rvn) {
          //vertex's edge before rvn  reach beyond rvn, so rvn is not a stopvertex.
          l1 = -1
          rvn = k2
          k2 = -1 // break;
        }
        k2 -= 1
      }

      if (l1 > 0) {
        stopVertex = l1 + start
        rvn = -1 // break;

      }
      rvn -= 1
    }

    return stopVertex
  }

  override def clear() {
    matrix.clear()
  }
}
