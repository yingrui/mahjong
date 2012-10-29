package websiteschema.mpsegment.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Path
        implements Cloneable {

    public Path() {
        vertexList = new ArrayList();
    }

    public Path(List<Integer> list) {
        vertexList = list;
    }

    public void Clear() {
        vertexList.clear();
    }

    public int getLength() {
        return vertexList.size() - 1;
    }

    public Path addVertex(int vertex) {
        vertexList.add(vertex);
        return this;
    }

    public Path addPath(Path path) {
        vertexList.addAll(path.vertexList);
        return this;
    }

    public Object get(int i) {
        return vertexList.get(i);
    }

    public int iget(int i) {
        Integer integer = (Integer) vertexList.get(i);
        return integer.intValue();
    }

    public Object getLast() {
        return vertexList.get(vertexList.size() - 1);
    }

//    @Override
//    public Object clone() throws CloneNotSupportedException {
//        Path path = null;
//        try {
//            path = (Path) super.clone();
//        } catch (CloneNotSupportedException ex) {
//            throw ex;
//        }
//        path.vertexList = (ArrayList) vertexList.clone();
//        return path;
//    }

    @Override
    public String toString() {
        String s = new String();
        Iterator iterator = vertexList.iterator();
        if (iterator.hasNext()) {
            Object obj = iterator.next();
            s = (new StringBuilder()).append(s).append(obj.toString()).toString();
        }
        while (iterator.hasNext()) {
            Object obj1 = iterator.next();
            s = (new StringBuilder()).append(s).append("-").append(obj1.toString()).toString();
        }
        return s;
    }
    private List<Integer> vertexList;
}
