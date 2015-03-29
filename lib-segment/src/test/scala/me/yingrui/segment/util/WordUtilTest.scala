package me.yingrui.segment.util

import org.junit.Assert
import org.junit.Test

class WordUtilTest {

  @Test
  def should_return_1_if_input_is_a_numerical_string() {
    Assert.assertEquals(1, WordUtil.isNumerical("123"))
  }
  @Test
  def should_return_2_if_input_is_not_a_numerical_string() {
    Assert.assertEquals(2, WordUtil.isNumerical("abc123"))
  }
}
