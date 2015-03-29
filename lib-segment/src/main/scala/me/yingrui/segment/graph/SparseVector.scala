package me.yingrui.segment.graph

import java.util

class SparseVector[Obj](N: Int) {

  // initialize the all 0s vector of length N
  private val st: SymbolTable[Int, Obj] = new SymbolTable[Int, Obj]() // the vector, represented by index-value pairs

  // put st(i) = value
  def put(i: Int, value: Int, obj: Obj) {
    if (i < 0 || i >= N) {
      throw new RuntimeException("Illegal index")
    }
    if (value == 0) {
      st.delete(i)
    } else {
      st.put(i, value, obj)
    }
  }

  // return st(i)
  def get(i: Int): Int = {
    if (i < 0 || i >= N) {
      throw new RuntimeException("Illegal index")
    }
    return st.getOrElse(i, 0)
  }

  def getObject(i: Int): Option[Obj] = {
    if (i < 0 || i >= N) {
      throw new RuntimeException("Illegal index")
    }

    st.getOrElseObject(i)
  }

  // return the number of nonzero entries
  def nnz(): Int = {
    return st.size
  }

  def getNonZeroColumns(): Array[Int] = {
    val cols = new Array[Int](nnz())
    val keys = st.iterator()
    if (null != keys) {
      var i = cols.length - 1
      while(i >= 0){cols(i) = keys.get(i); i -= 1}
    }
    return cols
  }

  // return the size of the vector
  def size(): Int = {
    return N
  }

  // return a string representation
  override def toString(): String = {
    var s = ""
    val keys = st.iterator()
    var i = 0
    while (i < keys.size()) {
      s += "(" + i + ", " + st.get(i) + ") "
      i += 1
    }
    return s
  }

  def clear() {
    st.clear()
  }

  /**
   * ***********************************************************************
   *
   * Sorted symbol table implementation using a HashMap. Does not
   * allow duplicate keys.
   *
   * ***********************************************************************
   *
   */
  class SymbolTable[Value, Obj] {

    def clear() {
      st.clear()
    }

    class Pair(value: Value, obj: Obj) {
      def getValue() = value

      def getObj() = obj
    }

    /**
     * Create an empty symbol table.
     */
    private val st = new java.util.TreeMap[Int, Pair]()


    /**
     * Put key-value pair into the symbol table. Remove key from table if
     * value is null.
     */
    def put(key: Int, valu: Value, obj: Obj) {
      if (valu == null) {
        st.remove(key)
      } else {
        st.put(key, new Pair(valu, obj))
      }
    }

    /**
     * Return the value paired with given key; null if key is not in table.
     */
    def get(key: Int): Value = {
      return st.get(key).getValue()
    }

    def getOrElse(key: Int, defaultValue: Value): Value = {
      val pair = st.get(key)
      if (null != pair) pair.getValue() else defaultValue
    }

    def getObject(key: Int): Obj = {
      return st.get(key).getObj()
    }

    def getOrElseObject(key: Int): Option[Obj] = {
      val pair = st.get(key)
      if (pair != null) {
        return Option(pair.getObj())
      }
      return None
    }

    def delete(key: Int) {
      st.remove(key)
    }

    def contains(key: Int): Boolean = {
      return st.containsKey(key)
    }

    def size() = st.size

    def iterator(): java.util.List[Int] = {
      new java.util.ArrayList[Int](st.keySet())
    }
  }

}
