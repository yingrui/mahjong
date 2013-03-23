package websiteschema.mpsegment.graph

import org.junit.Assert
import org.junit.Test

class SparseVectorTest {

  @Test
  def should_return_value_and_object_by_key() {
    val vector = new SparseVector[Double](5)
    vector.put(1, 2, 2.0D);
    Assert.assertEquals(2.0D, vector.getObject(1), 0.000001D)
    Assert.assertEquals(2, vector.get(1))
  }

  @Test
  def should_release_space_when_set_zero_to_vector() {
    val vector = new SparseVector[Double](5)
    vector.put(1, 2, 2.0D);
    Assert.assertEquals(1, vector.nnz())
    vector.put(1, 0, 2.0D);
    Assert.assertEquals(0, vector.nnz())
  }
}
