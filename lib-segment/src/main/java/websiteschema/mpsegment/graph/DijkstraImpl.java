package websiteschema.mpsegment.graph;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

public class DijkstraImpl implements IShortestPath {

    private IGraph graph;
    private final static int numOfVertexes = MPSegmentConfiguration.SectionSize();
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
        int distance = graph.getEdgeWeight(location, vertex) + (dijk.hasFoundShortestPathTo(location) ? dijk.getDistanceOfPathTo(location) : 0);
        int knownDistance = dijk.getDistanceOfPathTo(vertex);
        if (distance < knownDistance) {
            dijk.setDistanceOfPathTo(vertex, distance);
            dijk.reached(vertex);
            route[vertex] = location;
        }
    }

    @Override
    public void setGraph(IGraph graph) {
        this.graph = graph;
    }

    /**
     * "Dijkstra Shortest Path" defined 3 elements.
     */
    private class DijkstraElement {

        Collection<Integer> S; // Set of vertexes which already found the shortest route.
        TreeSet<Element> reachedVertexSet;
        int[] D; // The distance of shortest routes.
        int size = 1024;

        class Element implements Comparable<Element> {
            int vertex;
            int distance;

            public Element(int vertex, int distance) {
                this.vertex = vertex;
                this.distance = distance;
            }

            @Override
            public int compareTo(Element other) {
                if (distance == other.distance) {
                    return vertex != other.vertex ? -1 : 0;
                }
                return distance >= other.distance ? 1 : -1;
            }
        }

        DijkstraElement(int num) {
            size = num;
            S = new HashSet<Integer>(size);
            reachedVertexSet = new TreeSet<Element>();
            D = new int[size];
        }

        void init() {
            S.clear();
            reachedVertexSet.clear();
            for (int i = 0; i < size; i++) {
                D[i] = Integer.MAX_VALUE;
            }
        }

        boolean hasFoundShortestPathTo(int vertex) {
            return S.contains(vertex);
        }

        void reached(int vertex) {
            Element ele = new Element(vertex, D[vertex]);
            reachedVertexSet.add(ele);
        }

        int getDistanceOfPathTo(int vertex) {
            return D[vertex];
        }

        void setDistanceOfPathTo(int vertex, int distance) {
            D[vertex] = distance;
        }

        void foundShortestPathOf(int vertex) {
            S.add(vertex);
        }

        int findNewShortestPath() {
            int nextVertex = getResolvedVertex();
            if (nextVertex >= 0) {
                foundShortestPathOf(nextVertex);
            }
            return nextVertex;
        }

        int getResolvedVertex() {
            if (!reachedVertexSet.isEmpty()) {
                Element ele = reachedVertexSet.first();
                reachedVertexSet.remove(ele);
                return ele.vertex;
            }
            return -1;
        }

    }
}
