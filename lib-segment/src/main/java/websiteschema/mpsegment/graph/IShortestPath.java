package websiteschema.mpsegment.graph;

public interface IShortestPath {

    Path getShortestPath(int start, int end);

    void setGraph(IGraph graph);
    
}
