package me.yingrui.segment.dict

import org.junit.Assert
import org.junit.Test

class DomainWordItemTest {

  var word: DomainWordItem = DomainWordItem("Test", 0)
  word.setOccuredCount("N", 50)

  @Test
  def should_return_POS_N_and_Freq() {
    val wordPOSTable = word.getWordPOSTable()
    Assert.assertNotNull(wordPOSTable)
    Assert.assertEquals(1, wordPOSTable.length)
    Assert.assertEquals(POSUtil.POS_N, wordPOSTable(0)(0))
    Assert.assertEquals(50, wordPOSTable(0)(1))
  }

  @Test
  def should_set_occured_sum() {
    word.setOccuredSum(100)
    val wordPOSTable = word.getWordPOSTable()
    Assert.assertNotNull(wordPOSTable)
    Assert.assertEquals(1, wordPOSTable.length)
    Assert.assertEquals(POSUtil.POS_N, wordPOSTable(0)(0))
    Assert.assertEquals(100, wordPOSTable(0)(1))
  }
}
