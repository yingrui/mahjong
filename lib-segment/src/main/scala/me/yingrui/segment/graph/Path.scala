package me.yingrui.segment.graph

import collection.mutable.ListBuffer

class Path {

  def Clear() {
    vertexList.clear()
  }

  def getLength(): Int = {
    return vertexList.size - 1
  }

  def addVertex(vertex: Int): Path = {
    vertexList += (vertex)
    return this
  }

  def addPath(path: Path): Path = {
    vertexList ++= (path.vertexList)
    return this
  }

  def get(i: Int): Int = {
    return vertexList(i)
  }

  def iget(i: Int): Int = {
    return vertexList(i)
  }

  def getLast(): Int = {
    return vertexList(vertexList.size - 1)
  }

  override def toString(): String = {
    val s = new StringBuilder()

    for (vertex <- vertexList) {
      s.append(vertex).append("-")
    }
    if (s.endsWith("-")) {
      s.deleteCharAt(s.length - 1)
    }
    return s.toString()
  }

  private var vertexList = ListBuffer[Int]()
}

object Path {
  def apply() = new Path()

  def apply(list: List[Int]) = {
    val p = new Path
    p.vertexList ++= list
    p
  }
}
