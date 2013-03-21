//package websiteschema.mpsegment.graph;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.TreeSet;
//
///**
// * "Dijkstra Shortest Path" defined 3 elements.
// */
//class DijkstraElement {
//
//    Collection[Int] S; // Set of vertexes which already found the shortest route.
//    var reachedVertexSet: TreeSet[Element] = null
//    Array[Int] D; // The distance of shortest routes.
//    var size = 1024
//
//    class Element extends Comparable[Element] {
//        var vertex: Int = null
//        var distance: Int = null
//
//        public Element(Int vertex, Int distance) {
//            this.vertex = vertex;
//            this.distance = distance;
//        }
//
//        override def compareTo(other: Element) : Int = {
//            if (distance == other.distance) {
//                return vertex != other.vertex ? -1 : 0;
//            }
//            return distance >= other.distance ? 1 : -1;
//        }
//    }
//
//    DijkstraElement(Int num) {
//        size = num;
//        S = new HashSet[Int](size);
//        reachedVertexSet = new TreeSet[Element]();
//        D = new Array[Int](size);
//    }
//
//    void init() {
//        S.clear();
//        reachedVertexSet.clear();
//        for (Int i = 0; i < size; i++) {
//            D[i] = Int.MAX_VALUE;
//        }
//    }
//
//    Boolean hasFoundShortestPathTo(Int vertex) {
//        return S.contains(vertex);
//    }
//
//    void reached(Int vertex) {
//        var ele = new Element(vertex, D[vertex])
//        reachedVertexSet.add(ele);
//    }
//
//    Int getDistanceOfPathTo(Int vertex) {
//        return D[vertex];
//    }
//
//    void setDistanceOfPathTo(Int vertex, Int distance) {
//        D[vertex] = distance;
//    }
//
//    void foundShortestPathOf(Int vertex) {
//        S.add(vertex);
//    }
//
//    Int findNewShortestPath() {
//        var nextVertex = getResolvedVertex()
//        if (nextVertex >= 0) {
//            foundShortestPathOf(nextVertex);
//        }
//        return nextVertex;
//    }
//
//    Int getResolvedVertex() {
//        if (!reachedVertexSet.isEmpty()) {
//            var ele = reachedVertexSet.first()
//            reachedVertexSet.remove(ele);
//            return ele.vertex;
//        }
//        return -1;
//    }
//
//}
