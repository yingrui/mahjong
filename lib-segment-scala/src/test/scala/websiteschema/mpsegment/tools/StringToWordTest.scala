package websiteschema.mpsegment.tools

import org.junit.Assert
import org.junit.Test
import websiteschema.mpsegment.concept.ConceptRepository
import websiteschema.mpsegment.dict.POSUtil

class StringToWordTest {

  @Test
  def should_convert_string_to_word_with_word_name_and_single_POS() {
    val wordString = """{"word" : "测试", "domainType" : 2, "POSTable" : {"N" : 100, "V" : 20}}"""
    val word = new StringWordConverter().convert(wordString)

    Assert.assertEquals("测试", word.getWordName())
    Assert.assertEquals(2, word.getDomainType())
    val posTable = word.getWordPOSTable()
    Assert.assertEquals(2, posTable.length)
    Assert.assertEquals(POSUtil.POS_N, posTable(0)(0))
    Assert.assertEquals(100, posTable(0)(1))
    Assert.assertEquals(POSUtil.POS_V, posTable(1)(0))
    Assert.assertEquals(20, word.getOccuredCount(POSUtil.getPOSString(POSUtil.POS_V)))
    Assert.assertEquals(120, word.getOccuredSum())
  }

  @Test
  def should_convert_string_to_word_with_word_concepts() {
    val wordString = """{"word":"测试","domainType":2,"POSTable":{"N":100,"V":20},"concepts":["n-insect","n-person"]}"""
    val converter = new StringWordConverter()
    converter.setConceptRepository(ConceptRepository())
    val word = converter.convert(wordString)
    Assert.assertEquals(2, word.getConcepts().length)
    Assert.assertEquals("n-insect", word.getConcepts()(0).getName())
    Assert.assertEquals("n-person", word.getConcepts()(1).getName())
  }

  @Test
  def should_convert_string_to_word() {
    val wordString = """{"word":"\r\n\"\/\\","domainType":2"""
    val converter = new StringWordConverter()
    converter.setConceptRepository(ConceptRepository())
    val word = converter.convert(wordString)
    Assert.assertEquals("\r\n\"/\\", word.getWordName())
  }

  @Test
  def should_() {
    val wordString = """{"word" : "中国", "domainType" : 0, "POSTable" : {"NS" : 24455, "N" : 35}, "concepts" : ["n-group", "n-space"]},"""
    val converter = new StringWordConverter()
    converter.setConceptRepository(ConceptRepository())
    val word = converter.convert(wordString)
    Assert.assertEquals("中国", word.getWordName())

    val posTable = word.getWordPOSTable()
    Assert.assertEquals(2, posTable.length)
    Assert.assertEquals(POSUtil.POS_NS, posTable(0)(0))
    Assert.assertEquals(24455, posTable(0)(1))
    Assert.assertEquals(POSUtil.POS_N, posTable(1)(0))
    Assert.assertEquals(35, posTable(1)(1))
  }
}
