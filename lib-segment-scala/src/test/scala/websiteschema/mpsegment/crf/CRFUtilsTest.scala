package websiteschema.mpsegment.crf

import websiteschema.mpsegment.Assertion._
import org.junit.Test

class CRFUtilsTest {

  import websiteschema.mpsegment.crf.CRFUtils._

  @Test
  def should_calculate_log_sum_0_0_0 {
    val x = Array(0.0D, 0.0D, 0.0D)
    val z = logSum(x)
    shouldBeEqual(1.0986122886681098D, z)
  }

  @Test
  def should_calculate_log_sum_1_1_1 {
    val x = Array(1.0D, 1.0D, 1.0D)
    val z = logSum(x)
    shouldBeEqual(2.09861228866811D, z)
  }

  @Test
  def should_calculate_log_sum_2_2_2 {
    val x = Array(2.0D, 2.0D, 2.0D)
    val z = logSum(x)
    shouldBeEqual(3.09861228866811D, z)
  }

  @Test
  def should_calculate_log_sum_ {
    val x = Array(1.38D, 0.28D)
    val z = logSum(x)
    println(z)
  }

  @Test
  def should_calculate_log_sum_exp_0_0_0 {
    val x = Array(0.0D, 0.0D, 0.0D)
    val z = logSumExp(x)
    shouldBeEqual(1.0986122886681098D, z)
  }

  @Test
  def should_calculate_log_sum_exp_1_1_1 {
    val x = Array(1.0D, 1.0D, 1.0D)
    val z = logSumExp(x)
    shouldBeEqual(2.09861228866811D, z)
  }

  @Test
  def should_calculate_log_sum_exp_2_2_2 {
    val x = Array(2.0D, 2.0D, 2.0D)
    val z = logSumExp(x)
    shouldBeEqual(3.09861228866811D, z)
  }

  @Test
  def should_calculate_log_sum_exp_ {
    val x = Array(0.55D, -0.55D)
    val z = logSumExp(x)
    println(z)
  }
}
