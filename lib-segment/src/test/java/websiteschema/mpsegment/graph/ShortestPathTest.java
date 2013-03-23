package websiteschema.mpsegment.graph;

import org.junit.Test;
import websiteschema.mpsegment.dict.WordImpl;

public class ShortestPathTest {

    @Test
    public void should_return_edge_object_according_tail() {
        IShortestPath dijkstra = new DijkstraImpl();
        dijkstra.setGraph(createGraph());
        Path path = dijkstra.getShortestPath(0, 4);
        System.out.println(path);
    }

    private IGraph createGraph() {
        IGraph graph = new Graph();
        graph.addEdge(0, 1, 1, new WordImpl("S1"));
        graph.addEdge(0, 2, 5, new WordImpl("S1S2"));
        graph.addEdge(1, 2, 2, new WordImpl("S2"));
        graph.addEdge(1, 3, 2, new WordImpl("S2S3"));
        graph.addEdge(2, 3, 3, new WordImpl("S3"));
        graph.addEdge(0, 3, 5, new WordImpl("S1S2S3"));
        graph.addEdge(3, 4, 1, new WordImpl("S3"));
        return graph;
    }

}
