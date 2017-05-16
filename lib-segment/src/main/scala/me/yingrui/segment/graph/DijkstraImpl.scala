package me.yingrui.segment.graph

import me.yingrui.segment.conf.MPSegmentConfiguration
import collection.mutable.ListBuffer

class DijkstraImpl(numOfVertexes: Int) extends IShortestPath {

//  private val numOfVertexes = MPSegmentConfiguration.SectionSize
  private var graph: IGraph = null
  private val route = new Array[Int](numOfVertexes)
  private val dijk = DijkstraElement(numOfVertexes)

  clear()

  private def clear() {
    for (i <- 0 until numOfVertexes) {
      route(i) = 0
    }
    dijk.init()
  }

  /**
   * find the shortest route or get it from the results directly.
   *
   * @param end - In case reuse the results
   * @return
   */
  override def getShortestPath(start: Int, end: Int): Path = {
    clear()
    findShortestRoute(start, end, dijk)
    return Path(backTracking(end).toList)
  }

  private def backTracking(end: Int): ListBuffer[Int] = {
    val last = route(end)
    val routes = if(last > 0) backTracking(last) else ListBuffer[Int]()
    routes += end
    return routes
  }

  private def findShortestRoute(location: Int, dest: Int, dijk: DijkstraElement) {
    val adjacentVertices = graph.getAdjacentVertices(location)
    if (null == adjacentVertices || adjacentVertices.length == 0)
      return
    for (neighbor <- adjacentVertices) {
      findAndSaveShorterRoute(location, neighbor, dijk)
    }
    val nextVertex = dijk.findNewShortestPath()
    if (nextVertex >= 0) {
      if (nextVertex != dest) {
        findShortestRoute(nextVertex, dest, dijk)
      }
    }
  }

  private def findAndSaveShorterRoute(location: Int, vertex: Int, dijk: DijkstraElement) {
    val distance = getEdgeWeight(location, vertex) + (if(dijk.hasFoundShortestPathTo(location)) dijk.getDistanceOfPathTo(location) else 0)
    val knownDistance = dijk.getDistanceOfPathTo(vertex)
    if (distance < knownDistance) {
      dijk.setDistanceOfPathTo(vertex, distance)
      dijk.reached(vertex)
      route(vertex) = location
    }
  }

  def getEdgeWeight(location: Int, vertex: Int): Int = {
    return graph.getEdgeWeight(location, vertex)
  }

  override def setGraph(graph: IGraph) {
    this.graph = graph
  }

  def getGraph(): IGraph = {
    return graph
  }

  def getRouteBackTrace(): Array[Int] = {
    return route
  }

  def getDijkstraElement(): DijkstraElement = {
    return dijk
  }
}
