package websiteschema.mpsegment.util

import org.junit.Assert
import org.junit.Test

class RomanNumberTest {

  @Test
  def should_return_number_when_input_is_single_roman_numeral_character() {
    Assert.assertEquals(1, RomanNumeral.getBasicSymbol("I"));
    Assert.assertEquals(5, RomanNumeral.getBasicSymbol("V"));
    Assert.assertEquals(10, RomanNumeral.getBasicSymbol("X"));
    Assert.assertEquals(50, RomanNumeral.getBasicSymbol("L"));
    Assert.assertEquals(100, RomanNumeral.getBasicSymbol("C"));
    Assert.assertEquals(500, RomanNumeral.getBasicSymbol("D"));
    Assert.assertEquals(1000, RomanNumeral.getBasicSymbol("M"));
  }

  @Test
  def should_get_number_of_roman_numeral() {
    Assert.assertEquals(1, RomanNumeral.convert("I"))
    Assert.assertEquals(39, RomanNumeral.convert("XXXIX"))
    Assert.assertEquals(1944, RomanNumeral.convert("MCMXLIV"))
  }
}