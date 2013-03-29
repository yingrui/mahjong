package websiteschema.mpsegment.graph

import collection.mutable.OpenHashMap

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

  def getObject(i: Int): Obj = {
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
      var i = 0
      keys.foreach(key => {cols(i) = key; i += 1})
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
    for (i <- st.iterator()) {
      s += "(" + i + ", " + st.get(i) + ") "
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
    private var st = OpenHashMap[Int, Pair]()


    /**
     * Put key-value pair into the symbol table. Remove key from table if
     * value is null.
     */
    def put(key: Int, valu: Value, obj: Obj) {
      if (valu == null) {
        st -= (key)
      } else {
        st += (key -> new Pair(valu, obj))
      }
    }

    /**
     * Return the value paired with given key; null if key is not in table.
     */
    def get(key: Int): Value = {
      return st(key).getValue()
    }

    def getOrElse(key: Int, defaultValue: Value): Value = {
      val pair = st.getOrElse(key, null)
      if (null != pair) pair.getValue() else defaultValue
    }

    def getObject(key: Int): Obj = {
      return st(key).getObj()
    }

    def getOrElseObject(key: Int): Obj =
      st.get(key) match {
        case Some(pair) => pair.getObj()
        case _ => None.get
      }

    def delete(key: Int) {
      st -= (key)
    }

    def contains(key: Int): Boolean = {
      return st.contains(key)
    }

    def size() = st.size

    def iterator(): Iterable[Int] = {
      return st.keys
    }
  }

}
