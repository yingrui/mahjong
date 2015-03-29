package me.yingrui.segment.graph

import org.junit.{Test, Assert}
import me.yingrui.segment.dict.WordImpl

class GraphTest {

  @Test
  def should_return_edge_object() {
    val graph = createGraph()
    Assert.assertEquals("S1", graph.getEdgeObject(0,1).getWordName())
    Assert.assertEquals("S1S2", graph.getEdgeObject(0,2).getWordName())
  }

  @Test
  def should_return_edge_weight() {
    val graph = createGraph()
    Assert.assertEquals(1, graph.getEdgeWeight(0,1))
    Assert.assertEquals(5, graph.getEdgeWeight(0,2))
  }

  @Test
  def should_return_adjacent_vertexes() {
    val graph = createGraph()
    val vertexes = graph.getAdjacentVertices(0)
    Assert.assertArrayEquals(List(1,2,3).toArray, vertexes.sorted)
  }

  private def createGraph(): IGraph = {
    val graph = new Graph(5)
    graph.addEdge(0, 1, 1, new WordImpl("S1"))
    graph.addEdge(0, 2, 5, new WordImpl("S1S2"))
    graph.addEdge(1, 2, 2, new WordImpl("S2"))
    graph.addEdge(1, 3, 2, new WordImpl("S2S3"))
    graph.addEdge(2, 3, 3, new WordImpl("S3"))
    graph.addEdge(0, 3, 5, new WordImpl("S1S2S3"))
    graph.addEdge(3, 4, 1, new WordImpl("S3"))
    return graph
  }
}
