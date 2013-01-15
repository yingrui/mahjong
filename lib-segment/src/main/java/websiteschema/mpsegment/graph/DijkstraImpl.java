package websiteschema.mpsegment.graph;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;

import java.util.LinkedList;

public class DijkstraImpl implements IShortestPath {

    private final static int numOfVertexes = MPSegmentConfiguration.SectionSize();
    private IGraph graph;
    private int[] route;
    private DijkstraElement dijk = new DijkstraElement(numOfVertexes);

    public DijkstraImpl() {
        route = new int[numOfVertexes];
        clear();
    }

    private void clear() {
        for (int i = 0; i < numOfVertexes; i++) {
            route[i] = 0;
        }
        dijk.init();
    }

    /**
     * find the shortest route or get it from the results directly.
     *
     * @param end - In case reuse the results
     * @return
     * @throws Exception
     */
    @Override
    public Path getShortestPath(int start, int end) {
        clear();
        findShortestRoute(start, end, dijk);
        return new Path(backTracking(end));
    }

    private LinkedList<Integer> backTracking(int end) {
        int last = route[end];
        if (last > 0) {
            LinkedList<Integer> routes = backTracking(last);
            routes.addLast(end);
            return routes;
        }
        LinkedList<Integer> routes = new LinkedList<Integer>();
        routes.add(end);
        return routes;
    }

    private void findShortestRoute(int location, int dest, DijkstraElement dijk) {
        int[] adjacentVertices = graph.getAdjacentVertices(location);
        if (null == adjacentVertices || adjacentVertices.length == 0)
            return;
        for (int neighbor : adjacentVertices) {
            findAndSaveShorterRoute(location, neighbor, dijk);
        }
        int nextVertex = dijk.findNewShortestPath();
        if (nextVertex >= 0) {
            if (nextVertex != dest) {
                findShortestRoute(nextVertex, dest, dijk);
            }
        }
    }

    private void findAndSaveShorterRoute(final int location, final int vertex, DijkstraElement dijk) {
        int distance = getEdgeWeight(location, vertex) + (dijk.hasFoundShortestPathTo(location) ? dijk.getDistanceOfPathTo(location) : 0);
        int knownDistance = dijk.getDistanceOfPathTo(vertex);
        if (distance < knownDistance) {
            dijk.setDistanceOfPathTo(vertex, distance);
            dijk.reached(vertex);
            route[vertex] = location;
        }
    }

    protected int getEdgeWeight(int location, int vertex) {
        return graph.getEdgeWeight(location, vertex);
    }

    @Override
    public void setGraph(IGraph graph) {
        this.graph = graph;
    }

    protected IGraph getGraph() {
        return graph;
    }

    protected int[] getRouteBackTrace() {
        return route;
    }

    protected DijkstraElement getDijkstraElement() {
        return dijk;
    }
}
