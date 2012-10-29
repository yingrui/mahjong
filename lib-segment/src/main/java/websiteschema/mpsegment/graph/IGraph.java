package websiteschema.mpsegment.graph;

import websiteschema.mpsegment.dict.IWord;

public interface IGraph {

    public void addVertex();

    public void addEdge(int head, int tail, int weight, IWord obj);

    public int getEdgeWeight(int head, int tail);

    public IWord getEdgeObject(int head, int tail);

    public int[] getAdjacentVertices(int vertex);

    public int getStopVertex(int i, int j);

    public void clear();
}
