/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.graph;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.dict.IWord;

/**
 *
 * @author ray
 */
public class Graph implements IGraph {

    private final static int size = MPSegmentConfiguration.SectionSize();
    private SparseMatrix<IWord> matrix = new SparseMatrix<IWord>(size);

    public Graph() {
    }
    
    @Override
    public void addVertex() {
    }

    @Override
    public void addEdge(int head, int tail, int weight, IWord obj) {
        matrix.set(head, tail, weight, obj);
    }

    @Override
    public int getEdgeWeight(int head, int tail) {
        return matrix.get(head, tail);
    }

    @Override
    public IWord getEdgeObject(int head, int tail) {
        return matrix.getObject(head, tail);
    }

    @Override
    public int[] getAdjacentVertices(int vertex) {
        int[] adjacents = matrix.getNonZeroColumns(vertex);
        return (null != adjacents) ? adjacents : new int[0];
    }

    @Override
    public int getStopVertex(final int start, final int end) {
        int stopVertex = start;
        int gap = (end - start) + 1;
        if (start <= 0 || end <= 0) {
            return -1;
        }
        int maxDistance[] = new int[gap];  // all Zeros at initialization
        for (int rvn = 0; rvn < gap; rvn++) {  //rvn: relative vertex number
            int adjacentVertices[] = getAdjacentVertices(start + rvn);

            //find out the Length of the longest edges of one vertex, and calculate its distance to start vertex, and fill it in maxDistance[]
            for (int adjacent = 0; adjacent < adjacentVertices.length; adjacent++) {
                if (adjacentVertices[adjacent] - start > maxDistance[rvn]) {
                    maxDistance[rvn] = adjacentVertices[adjacent] - start;
                }
            }
        }

        for (int rvn = gap - 1; rvn > 0;) {
            int l1 = rvn;
            for (int k2 = rvn - 1; k2 >= 0; k2--) {
                if (maxDistance[k2] > rvn) {  //vertex's edge before rvn  reach beyond rvn, so rvn is not a stopvertex.
                    l1 = -1;
                    rvn = k2;
                    break;
                }
            }

            if (l1 > 0) {
                stopVertex = l1 + start;
                break;

            }
        }

        return stopVertex;
    }

    @Override
    public void clear() {
        matrix.clear();
    }
}
