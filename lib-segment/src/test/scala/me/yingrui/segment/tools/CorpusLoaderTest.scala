/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package me.yingrui.segment.tools

import java.io.{ByteArrayInputStream, InputStream}

import me.yingrui.segment.dict.POSUtil
import org.junit.{Assert, Test}

class CorpusLoaderTest {

  @Test
  def should_load_word_and_pos_and_domain_type() {
    val inputStream = getFakeInputStream("19980101-02-005-001/m  [国务院/nt  侨办/j]nt  发表/v  新年/t  贺词/n ")
    val loader = CorpusLoader(inputStream)
    val result = loader.readLine()
    Assert.assertEquals("国务院侨办发表新年贺词", result.toOriginalString())
    Assert.assertEquals("国务院", result.getWord(0))
    Assert.assertEquals(POSUtil.POS_NT, result.getPOS(0))
    Assert.assertEquals(POSUtil.POS_J, result.getPOS(1))
    Assert.assertEquals(POSUtil.POS_NT, result.getDomainType(0))
    Assert.assertEquals(POSUtil.POS_NT, result.getDomainType(1))
    Assert.assertEquals(0, result.getDomainType(2))
    inputStream.close()
  }

  @Test
  def should_load_words_even_there_is_no_pos() {
    val inputStream = getFakeInputStream("国务院 侨办 发表 新年 贺词")
    val loader = CorpusLoader(inputStream)
    val result = loader.readLine()
    Assert.assertEquals("国务院侨办发表新年贺词", result.toOriginalString())
    Assert.assertEquals("国务院", result.getWord(0))
    Assert.assertEquals(0, result.getWordStartAt(0))
    Assert.assertEquals(3, result.getWordEndAt(0))
    Assert.assertEquals("侨办", result.getWord(1))
    Assert.assertEquals(3, result.getWordStartAt(1))
    Assert.assertEquals(5, result.getWordEndAt(1))
    Assert.assertEquals("发表", result.getWord(2))
    Assert.assertEquals(5, result.getWordStartAt(2))
    Assert.assertEquals(7, result.getWordEndAt(2))
    Assert.assertEquals("新年", result.getWord(3))
    Assert.assertEquals(7, result.getWordStartAt(3))
    Assert.assertEquals(9, result.getWordEndAt(3))
    Assert.assertEquals("贺词", result.getWord(4))
    Assert.assertEquals(9, result.getWordStartAt(4))
    Assert.assertEquals(11, result.getWordEndAt(4))
    inputStream.close()
  }

  @Test
  def should_load_concepts_and_concept_with_POS_J() {
    val inputStream = getFakeInputStream("19980101-02-005-001/m  [国务院/nt  侨办/j]nt  发表/v  新年/t  贺词/n ")
    val loader = CorpusLoader(inputStream)
    val result = loader.readLine()
    Assert.assertEquals("n-organization", result.getConcept(0))
    Assert.assertEquals("n-organization", result.getConcept(1))
    Assert.assertEquals("v-social-activity", result.getConcept(2))
    Assert.assertEquals("N/A", result.getConcept(3))
    Assert.assertEquals("n-creation", result.getConcept(4))
    inputStream.close()
  }

  @Test
  def should_load_domain_types_with_POS_NR() {
    val inputStream = getFakeInputStream("19980101-03-008-001/m  钱/nr  其琛/nr  访问/v  德班/ns")
    val loader = CorpusLoader(inputStream)
    val result = loader.readLine()
    Assert.assertEquals("钱", result.getWord(0))
    Assert.assertEquals(POSUtil.POS_NR, result.getPOS(0))
    Assert.assertEquals(POSUtil.POS_NR, result.getPOS(1))
    Assert.assertEquals(POSUtil.POS_NR, result.getDomainType(0))
    Assert.assertEquals(POSUtil.POS_NR, result.getDomainType(1))
    Assert.assertEquals(0, result.getDomainType(2))
    inputStream.close()
  }

  @Test
  def should_not_load_domain_types_with_POS_NR() {
    val inputStream = getFakeInputStream("19980101-03-008-001/m  钱/nr  其琛/nr  访问/v  德班/ns")
    val loader = CorpusLoader(inputStream)
    loader.eliminateDomainType(POSUtil.POS_NR)
    val result = loader.readLine()
    Assert.assertEquals("钱", result.getWord(0))
    Assert.assertEquals(POSUtil.POS_NR, result.getPOS(0))
    Assert.assertEquals(POSUtil.POS_NR, result.getPOS(1))
    Assert.assertEquals(0, result.getDomainType(0))
    Assert.assertEquals(0, result.getDomainType(1))
    Assert.assertEquals(0, result.getDomainType(2))
    inputStream.close()
  }

  private def getFakeInputStream(text: String): InputStream = {
    return new ByteArrayInputStream(text.getBytes("utf-8"))
  }

}
