package websiteschema.mpsegment.graph;

import websiteschema.mpsegment.dict.IWord;

trait IGraph {

    def addVertex()

    def addEdge(head: Int, tail: Int, weight: Int, obj: IWord)

    def getEdgeWeight(head: Int, tail: Int) : Int

    def getEdgeObject(head: Int, tail: Int) : IWord

    def getAdjacentVertices(vertex: Int) : Array[Int]

    def getStopVertex(i: Int, j: Int) : Int

    def clear()
}
