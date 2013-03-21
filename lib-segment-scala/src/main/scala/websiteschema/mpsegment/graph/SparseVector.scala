///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package websiteschema.mpsegment.graph
//
//import java.util.Iterator
//import java.util.TreeMap
//
///**
// *
// * @author twer
// */
//class SparseVector[Obj] {
//
//    private var N : Int = null             // length
//    private SymbolTable<Int, Int, Obj> st;  // the vector, represented by index-value pairs
//
//    // initialize the all 0s vector of length N
//    public SparseVector(Int N) {
//        this.N = N
//        this.st = new SymbolTable<Int, Int, Obj>()
//    }
//
//    // put st(i) = value
//    def put(i: Int, value: Int, obj: Obj) {
//        if (i < 0 || i >= N) {
//            throw new RuntimeException("Illegal index")
//        }
//        if (value == 0.0) {
//            st.delete(i)
//        } else {
//            st.put(i, value, obj)
//        }
//    }
//
//    // return st(i)
//    def get(i: Int) : Int = {
//        if (i < 0 || i >= N) {
//            throw new RuntimeException("Illegal index")
//        }
//        if (st.contains(i)) {
//            return st.get(i)
//        } else {
//            return 0
//        }
//    }
//
//    def getObject(i: Int) : Obj = {
//        if (i < 0 || i >= N) {
//            throw new RuntimeException("Illegal index")
//        }
//        if (st.contains(i)) {
//            return st.getObject(i)
//        } else {
//            return null
//        }
//    }
//
//    // return the number of nonzero entries
//    def nnz() : Int = {
//        return st.size()
//    }
//
//    def getNonZeroColumns() : Array[Int] = {
//        var cols = new Int[nnz()]
//        var keys = st.iterator()
//        if (null != keys) {
//            var i = 0
//            while (keys.hasNext()) {
//                cols[i++] = keys.next()
//            }
//        }
//        return cols
//    }
//
//    // return the size of the vector
//    def size() : Int = {
//        return N
//    }
//
//    // return a string representation
//    override def toString() : String = {
//        var s = ""
//        for (i <- st) {
//            s += "(" + i + ", " + st.get(i) + ") "
//        }
//        return s
//    }
//
//    void clear() {
//        st.clear()
//    }
//
//    /**
//     * ***********************************************************************
//     * Compilation: javac ST.java Execution: java ST
//     *
//     * Sorted symbol table implementation using a java.util.TreeMap. Does not
//     * allow duplicate keys.
//     *
//     * % java ST
//     *
//     ************************************************************************
//     *
//     */
//    class SymbolTable<Key extends Comparable[Key], Value, Obj> extends Iterable[Key] {
//
//        private def clear() {
//            st.clear()
//        }
//
//        class Pair {
//
//            var value: Value = null
//            var obj: Obj = null
//
//            Pair(Value v, Obj o) {
//                value = v
//                obj = o
//            }
//        }
//        private TreeMap[Key,Pair] st
//
//        /**
//         * Create an empty symbol table.
//         */
//        SymbolTable() {
//            st = new TreeMap[Key,Pair](); // 3 is an optimize number only for mpsegment
//        }
//
//        /**
//         * Put key-value pair into the symbol table. Remove key from table if
//         * value is null.
//         */
//        void put(Key key, Value val, Obj obj) {
//            if (val == null) {
//                st.remove(key)
//            } else {
//                st.put(key, new Pair(val, obj))
//            }
//        }
//
//        /**
//         * Return the value paired with given key; null if key is not in table.
//         */
//        Value get(Key key) {
//            return st.get(key).value
//        }
//
//        Obj getObject(Key key) {
//            return st.get(key).obj
//        }
//
//        Value delete(Key key) {
//            return st.remove(key).value
//        }
//
//        Boolean contains(Key key) {
//            return st.containsKey(key)
//        }
//
//        Int size() {
//            return st.size()
//        }
//
//        override def iterator() : Iterator[Key] = {
//            return st.keySet().iterator()
//        }
//    }
//}
