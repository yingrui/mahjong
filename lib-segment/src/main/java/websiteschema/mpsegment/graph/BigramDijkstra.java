package websiteschema.mpsegment.graph;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.dict.IWord;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

public class BigramDijkstra implements IShortestPath {

    private IGraph graph;
    private final static int numOfVertexes = MPSegmentConfiguration.SectionSize();
    private int[] route;
    private DijkstraElement dijk = new DijkstraElement(numOfVertexes);
    private WordBigram wordBigram;

    public BigramDijkstra(WordBigram wordBigram) {
        this.wordBigram = wordBigram;
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
     */
    @Override
    public Path getShortestPath(int start, int end) {
        clear();
        findShortestRoute(start, end, dijk);
        return new Path(backTracking(end));
    }

    private LinkedList<Integer> backTracking(int end) {
        int last = getNearestNeighbor(end);
        if (last > 0) {
            LinkedList<Integer> routes = backTracking(last);
            routes.addLast(end);
            return routes;
        }
        LinkedList<Integer> routes = new LinkedList<Integer>();
        routes.add(end);
        return routes;
    }

    private int getNearestNeighbor(int end) {
        return route[end];
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

    private int getEdgeWeight(int location, int vertex) {
        if(location >= 0) {
            IWord lastWord = getEdgeObject(location);
            IWord word = graph.getEdgeObject(location, vertex);
            if(null != lastWord && null != word) {
                double conditionProb = getConditionProbability(lastWord.getWordName(), word.getWordName());
                if(conditionProb > 0.00000000001) {
                    conditionProb = Math.log(conditionProb);
//                    System.out.println("Found Bigram: " + lastWord.getWordName() + " " + word.getWordName() + " prob: " + conditionProb);
                    return (int)conditionProb;
                }
            }
        }
        return graph.getEdgeWeight(location, vertex);
    }

    @Override
    public void setGraph(IGraph graph) {
        this.graph = graph;
    }

    public IWord getEdgeObject(int tail) {
        if(dijk.hasFoundShortestPathTo(tail)) {
            int head = getNearestNeighbor(tail);
            return graph.getEdgeObject(head, tail);
        }
        return null;
    }

    public double getConditionProbability(String word1, String word2) {
        return wordBigram.getProbability(word1, word2);
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
