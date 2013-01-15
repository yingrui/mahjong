package websiteschema.mpsegment.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * "Dijkstra Shortest Path" defined 3 elements.
 */
class DijkstraElement {

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
