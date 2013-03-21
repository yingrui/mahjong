//package websiteschema.mpsegment.graph;
//
//import websiteschema.mpsegment.conf.MPSegmentConfiguration;
//
//import java.util.LinkedList;
//
//class DijkstraImpl extends IShortestPath {
//
//    private static Int numOfVertexes = MPSegmentConfiguration.SectionSize();
//    private var graph : IGraph = null
//    private var route : Array[Int] = null
//    private var dijk : DijkstraElement = new DijkstraElement(numOfVertexes);
//
//    public DijkstraImpl() {
//        route = new Array[Int](numOfVertexes);
//        clear();
//    }
//
//    private def clear() {
//        for (Int i = 0; i < numOfVertexes; i++) {
//            route(i) = 0;
//        }
//        dijk.init();
//    }
//
//    /**
//     * find the shortest route or get it from the results directly.
//     *
//     * @param end - In case reuse the results
//     * @return
//     * @throws Exception
//     */
//    override def getShortestPath(start: Int, end: Int) : Path = {
//        clear();
//        findShortestRoute(start, end, dijk);
//        return new Path(backTracking(end));
//    }
//
//    private def backTracking(end: Int) : LinkedList[Int] = {
//        var last = route(end)
//        if (last > 0) {
//            var routes = backTracking(last)
//            routes.addLast(end);
//            return routes;
//        }
//        var routes = new LinkedList[Int]()
//        routes.add(end);
//        return routes;
//    }
//
//    private def findShortestRoute(location: Int, dest: Int, dijk: DijkstraElement) {
//        var adjacentVertices = graph.getAdjacentVertices(location)
//        if (null == adjacentVertices || adjacentVertices.length == 0)
//            return;
//        for (neighbor <- adjacentVertices) {
//            findAndSaveShorterRoute(location, neighbor, dijk);
//        }
//        var nextVertex = dijk.findNewShortestPath()
//        if (nextVertex >= 0) {
//            if (nextVertex != dest) {
//                findShortestRoute(nextVertex, dest, dijk);
//            }
//        }
//    }
//
//    private def findAndSaveShorterRoute(location: Int, vertex: Int, dijk: DijkstraElement) {
//        var distance = getEdgeWeight(location, vertex) + (dijk.hasFoundShortestPathTo(location) ? dijk.getDistanceOfPathTo(location) : 0)
//        var knownDistance = dijk.getDistanceOfPathTo(vertex)
//        if (distance < knownDistance) {
//            dijk.setDistanceOfPathTo(vertex, distance);
//            dijk.reached(vertex);
//            route(vertex) = location;
//        }
//    }
//
//    def getEdgeWeight(location: Int, vertex: Int) : Int = {
//        return graph.getEdgeWeight(location, vertex);
//    }
//
//    override def setGraph(graph: IGraph) {
//        this.graph = graph;
//    }
//
//    def getGraph() : IGraph = {
//        return graph;
//    }
//
//    def getRouteBackTrace() : Array[Int] = {
//        return route;
//    }
//
//    def getDijkstraElement() : DijkstraElement = {
//        return dijk;
//    }
//}
