package websiteschema.mpsegment.graph

class SparseMatrix[T](N: Int) {

  private val rows: Array[SparseVector[T]] = new Array[SparseVector[T]](N)
  for (i <- 0 until N) {
    rows(i) = new SparseVector[T](N)
  }

  // put A[i](j) = value
  def set(i: Int, j: Int, value: Int, obj: T) {
    if (i < 0 || i >= N) {
      throw new RuntimeException("Illegal index")
    }
    if (j < 0 || j >= N) {
      throw new RuntimeException("Illegal index")
    }
    rows(i).put(j, value, obj)
  }

  // return A[i](j)
  def get(i: Int, j: Int): Int = {
    if (i < 0 || i >= N) {
      throw new RuntimeException("Illegal index")
    }
    if (j < 0 || j >= N) {
      throw new RuntimeException("Illegal index")
    }
    return rows(i).get(j)
  }

  def getObject(row: Int, col: Int): Option[T] = {
    return rows(row).getObject(col)
  }

  def getNonZeroColumns(row: Int): Array[Int] = {
    return rows(row).getNonZeroColumns()
  }

  def clear() {
    rows.foreach(_.clear())
  }
}
