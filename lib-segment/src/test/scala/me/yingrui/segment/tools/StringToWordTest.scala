package me.yingrui.segment.tools

import me.yingrui.segment.concept.ConceptRepository
import me.yingrui.segment.dict.POSUtil
import org.junit.Assert.assertEquals
import org.junit.Test

class StringToWordTest {

  @Test
  def should_convert_string_to_word_with_word_name_and_single_POS() {
    val wordString = """{"word" : "测试", "domainType" : 2, "POSTable" : {"N" : 100, "V" : 20}}"""
    val word = new StringWordConverter().convert(wordString)

    assertEquals("测试", word.getWordName())
    assertEquals(2, word.getDomainType())
    val posTable = word.getWordPOSTable()
    assertEquals(2, posTable.length)
    assertEquals(POSUtil.POS_N, posTable(0)(0))
    assertEquals(100, posTable(0)(1))
    assertEquals(POSUtil.POS_V, posTable(1)(0))
    assertEquals(20, word.getOccuredCount(POSUtil.getPOSString(POSUtil.POS_V)))
    assertEquals(120, word.getOccuredSum())
  }

  @Test
  def should_convert_string_to_word_with_word_concepts() {
    val wordString = """{"word":"测试","domainType":2,"POSTable":{"N":100,"V":20},"concepts":["n-insect","n-person"]}"""
    val converter = new StringWordConverter()
    converter.setConceptRepository(ConceptRepository())
    val word = converter.convert(wordString)
    assertEquals(2, word.getConcepts().length)
    assertEquals("n-insect", word.getConcepts()(0).getName())
    assertEquals("n-person", word.getConcepts()(1).getName())
  }

  @Test
  def should_convert_string_to_word() {
    val wordString = """{"word":"\r\n\"\/\\","domainType":2"""
    val converter = new StringWordConverter()
    converter.setConceptRepository(ConceptRepository())
    val word = converter.convert(wordString)
    assertEquals("\r\n\"/\\", word.getWordName())
  }

  @Test
  def should_convert_string_with_whitespace() {
    val wordString = """{"word" : "中国", "domainType" : 0, "POSTable" : {"NS" : 24455, "N" : 35}, "concepts" : ["n-group", "n-space"]},"""
    val converter = new StringWordConverter()
    converter.setConceptRepository(ConceptRepository())
    val word = converter.convert(wordString)
    assertEquals("中国", word.getWordName())

    val posTable = word.getWordPOSTable()
    assertEquals(2, posTable.length)
    assertEquals(POSUtil.POS_NS, posTable(0)(0))
    assertEquals(24455, posTable(0)(1))
    assertEquals(POSUtil.POS_N, posTable(1)(0))
    assertEquals(35, posTable(1)(1))
  }

  @Test
  def should_convert_string_with_no_domain_type() {
    val wordString = """{"word" : "中国", "POSTable" : {"NS" : 24455, "N" : 35}"""
    val converter = new StringWordConverter()
    val word = converter.convert(wordString)
    assertEquals("中国", word.getWordName())
    assertEquals(0, word.getDomainType())
  }
}
