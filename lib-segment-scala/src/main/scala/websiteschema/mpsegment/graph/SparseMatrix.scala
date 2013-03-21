///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package websiteschema.mpsegment.graph;
//
///**
// * Trie, substitude of sparse matrix. A matrix represents a graph.
// *
// * @author ray
// */
//class SparseMatrix[T] {
//
//    private var N : Int = null           // N-by-N matrix
//    private var rows : SparseVector[T][] = null   // the rows, each row is a sparse vector
//
//    // initialize an N-by-N matrix of all 0s
//    public SparseMatrix(Int N) {
//        this.N = N;
//        rows = new Array[SparseVector](N);
//        for (Int i = 0; i < N; i++) {
//            rows(i) = new SparseVector[T](N);
//        }
//    }
//
//    // put A[i](j) = value
//    def set(i: Int, j: Int, value: Int, obj: T) {
//        if (i < 0 || i >= N) {
//            throw new RuntimeException("Illegal index");
//        }
//        if (j < 0 || j >= N) {
//            throw new RuntimeException("Illegal index");
//        }
//        rows(i).put(j, value, obj);
//    }
//
//    // return A[i](j)
//    def get(i: Int, j: Int) : Int = {
//        if (i < 0 || i >= N) {
//            throw new RuntimeException("Illegal index");
//        }
//        if (j < 0 || j >= N) {
//            throw new RuntimeException("Illegal index");
//        }
//        return rows(i).get(j);
//    }
//
//    def getObject(row: Int, col: Int) : T = {
//        return rows(row).getObject(col);
//    }
//
//    def getNonZeroColumns(row: Int) : Array[Int] = {
//        return rows(row).getNonZeroColumns();
//    }
//    
//    def clear() {
//        for (Int i = 0; i < N; i++) {
//            rows(i).clear();
//        }
//    }
//}
