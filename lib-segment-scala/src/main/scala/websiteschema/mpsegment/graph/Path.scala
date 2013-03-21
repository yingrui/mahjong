//package websiteschema.mpsegment.graph;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//class Path
//        extends Cloneable {
//
//    public Path() {
//        vertexList = new ArrayList();
//    }
//
//    public Path(List[Int] list) {
//        vertexList = list;
//    }
//
//    def Clear() {
//        vertexList.clear();
//    }
//
//    def getLength() : Int = {
//        return vertexList.size() - 1;
//    }
//
//    def addVertex(vertex: Int) : Path = {
//        vertexList.add(vertex);
//        return this;
//    }
//
//    def addPath(path: Path) : Path = {
//        vertexList.addAll(path.vertexList);
//        return this;
//    }
//
//    def get(i: Int) : Object = {
//        return vertexList.get(i);
//    }
//
//    def iget(i: Int) : Int = {
//        Int integer = (Int) vertexList.get(i);
//        return integer.intValue();
//    }
//
//    def getLast() : Object = {
//        return vertexList.get(vertexList.size() - 1);
//    }
//
////    override //    def clone() : Object = throws CloneNotSupportedException {
////        Path path = null;
////        try {
////            path = (Path) super.clone();
////        } catch {
////            throw ex;
////        }
////        path.vertexList = (ArrayList) vertexList.clone();
////        return path;
////    }
//
//    override def toString() : String = {
//        String s = new String();
//        Iterator iterator = vertexList.iterator();
//        if (iterator.hasNext()) {
//            Object obj = iterator.next();
//            s = (new StringBuilder()).append(s).append(obj.toString()).toString();
//        }
//        while (iterator.hasNext()) {
//            Object obj1 = iterator.next();
//            s = (new StringBuilder()).append(s).append("-").append(obj1.toString()).toString();
//        }
//        return s;
//    }
//    private var vertexList : List[Int] = null
//}
