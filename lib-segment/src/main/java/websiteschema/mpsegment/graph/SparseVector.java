/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.graph;

import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author twer
 */
public class SparseVector<Obj> {

    private final int N;             // length
    private SymbolTable<Integer, Integer, Obj> st;  // the vector, represented by index-value pairs

    // initialize the all 0s vector of length N
    public SparseVector(int N) {
        this.N = N;
        this.st = new SymbolTable<Integer, Integer, Obj>();
    }

    // put st[i] = value
    public void put(int i, int value, Obj obj) {
        if (i < 0 || i >= N) {
            throw new RuntimeException("Illegal index");
        }
        if (value == 0.0) {
            st.delete(i);
        } else {
            st.put(i, value, obj);
        }
    }

    // return st[i]
    public int get(int i) {
        if (i < 0 || i >= N) {
            throw new RuntimeException("Illegal index");
        }
        if (st.contains(i)) {
            return st.get(i);
        } else {
            return 0;
        }
    }

    public Obj getObject(int i) {
        if (i < 0 || i >= N) {
            throw new RuntimeException("Illegal index");
        }
        if (st.contains(i)) {
            return st.getObject(i);
        } else {
            return null;
        }
    }

    // return the number of nonzero entries
    public int nnz() {
        return st.size();
    }

    public int[] getNonZeroColumns() {
        int cols[] = new int[nnz()];
        Iterator<Integer> keys = st.iterator();
        if (null != keys) {
            int i = 0;
            while (keys.hasNext()) {
                cols[i++] = keys.next();
            }
        }
        return cols;
    }

    // return the size of the vector
    public int size() {
        return N;
    }

    // return a string representation
    @Override
    public String toString() {
        String s = "";
        for (int i : st) {
            s += "(" + i + ", " + st.get(i) + ") ";
        }
        return s;
    }

    void clear() {
        st.clear();
    }

    /**
     * ***********************************************************************
     * Compilation: javac ST.java Execution: java ST
     *
     * Sorted symbol table implementation using a java.util.TreeMap. Does not
     * allow duplicate keys.
     *
     * % java ST
     *
     ************************************************************************
     *
     */
    class SymbolTable<Key extends Comparable<Key>, Value, Obj> implements Iterable<Key> {

        private void clear() {
            st.clear();
        }

        class Pair {

            Value value;
            Obj obj;

            Pair(Value v, Obj o) {
                value = v;
                obj = o;
            }
        }
        private TreeMap<Key, Pair> st;

        /**
         * Create an empty symbol table.
         */
        SymbolTable() {
            st = new TreeMap<Key, Pair>(); // 3 is an optimize number only for mpsegment
        }

        /**
         * Put key-value pair into the symbol table. Remove key from table if
         * value is null.
         */
        void put(Key key, Value val, Obj obj) {
            if (val == null) {
                st.remove(key);
            } else {
                st.put(key, new Pair(val, obj));
            }
        }

        /**
         * Return the value paired with given key; null if key is not in table.
         */
        Value get(Key key) {
            return st.get(key).value;
        }

        Obj getObject(Key key) {
            return st.get(key).obj;
        }

        Value delete(Key key) {
            return st.remove(key).value;
        }

        boolean contains(Key key) {
            return st.containsKey(key);
        }

        int size() {
            return st.size();
        }

        @Override
        public Iterator<Key> iterator() {
            return st.keySet().iterator();
        }
    }
}
