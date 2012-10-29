/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.graph;

/**
 * Trie, substitude of sparse matrix. A matrix represents a graph.
 *
 * @author ray
 */
public class SparseMatrix<T> {

    private final int N;           // N-by-N matrix
    private SparseVector<T>[] rows;   // the rows, each row is a sparse vector

    // initialize an N-by-N matrix of all 0s
    public SparseMatrix(int N) {
        this.N = N;
        rows = new SparseVector[N];
        for (int i = 0; i < N; i++) {
            rows[i] = new SparseVector<T>(N);
        }
    }

    // put A[i][j] = value
    public void set(int i, int j, int value, T obj) {
        if (i < 0 || i >= N) {
            throw new RuntimeException("Illegal index");
        }
        if (j < 0 || j >= N) {
            throw new RuntimeException("Illegal index");
        }
        rows[i].put(j, value, obj);
    }

    // return A[i][j]
    public int get(int i, int j) {
        if (i < 0 || i >= N) {
            throw new RuntimeException("Illegal index");
        }
        if (j < 0 || j >= N) {
            throw new RuntimeException("Illegal index");
        }
        return rows[i].get(j);
    }

    public T getObject(int row, int col) {
        return rows[row].getObject(col);
    }

    public int[] getNonZeroColumns(int row) {
        return rows[row].getNonZeroColumns();
    }
    
    public void clear() {
        for (int i = 0; i < N; i++) {
            rows[i].clear();
        }
    }
}
