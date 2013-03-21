///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package websiteschema.mpsegment.graph
//
//import websiteschema.mpsegment.conf.MPSegmentConfiguration
//import websiteschema.mpsegment.dict.IWord
//
///**
// *
// * @author ray
// */
//class Graph extends IGraph {
//
//    private static Int size = MPSegmentConfiguration.SectionSize()
//    private var matrix : SparseMatrix[IWord] = new SparseMatrix[IWord](size)
//
//    public Graph() {
//    }
//    
//    override def addVertex() {
//    }
//
//    override def addEdge(head: Int, tail: Int, weight: Int, obj: IWord) {
//        matrix.set(head, tail, weight, obj)
//    }
//
//    override def getEdgeWeight(head: Int, tail: Int) : Int = {
//        return matrix.get(head, tail)
//    }
//
//    override def getEdgeObject(head: Int, tail: Int) : IWord = {
//        return matrix.getObject(head, tail)
//    }
//
//    override def getAdjacentVertices(vertex: Int) : Array[Int] = {
//        var adjacents = matrix.getNonZeroColumns(vertex)
//        return (null != adjacents) ? adjacents : new Array[Int](0)
//    }
//
//    override def getStopVertex(start: Int, end: Int) : Int = {
//        var stopVertex = start
//        var gap = (end - start) + 1
//        if (start <= 0 || end <= 0) {
//            return -1
//        }
//        Array[Int] maxDistance = new Array[Int](gap);  // all Zeros at initialization
//        for (Int rvn = 0; rvn < gap; rvn++) {  //rvn: relative vertex number
//            var adjacentVertices = getAdjacentVertices(start + rvn)
//
//            //find out the Length of the longest edges of one vertex, and calculate its distance to start vertex, and fill it in Array[maxDistance]
//            for (Int adjacent = 0; adjacent < adjacentVertices.length; adjacent++) {
//                if (adjacentVertices(adjacent) - start > maxDistance(rvn)) {
//                    maxDistance(rvn) = adjacentVertices(adjacent) - start
//                }
//            }
//        }
//
//        for (Int rvn = gap - 1; rvn > 0;) {
//            var l1 = rvn
//            for (Int k2 = rvn - 1; k2 >= 0; k2--) {
//                if (maxDistance(k2) > rvn) {  //vertex's edge before rvn  reach beyond rvn, so rvn is not a stopvertex.
//                    l1 = -1
//                    rvn = k2
//                    break
//                }
//            }
//
//            if (l1 > 0) {
//                stopVertex = l1 + start
//                break
//
//            }
//        }
//
//        return stopVertex
//    }
//
//    override def clear() {
//        matrix.clear()
//    }
//}
