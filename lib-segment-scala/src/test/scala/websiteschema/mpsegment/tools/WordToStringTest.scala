package websiteschema.mpsegment.tools

import org.junit.Assert
import org.junit.Test
import websiteschema.mpsegment.concept.Concept
import websiteschema.mpsegment.dict.{UnknownWord, IWord, POSUtil}



class WordToStringTest {

  var posAndFreq = new Array[Array[Int]](2)
  posAndFreq(0) = new Array[Int](2)
  posAndFreq(0)(0) = POSUtil.POS_N
  posAndFreq(0)(1) = 100
  posAndFreq(1) = new Array[Int](2)
  posAndFreq(1)(0) = POSUtil.POS_V
  posAndFreq(1)(1) = 20

  var singlePosAndFreq = new Array[Array[Int]](1)
  singlePosAndFreq(0) = new Array[Int](2)
  singlePosAndFreq(0)(0) = POSUtil.POS_N
  singlePosAndFreq(0)(1) = 100

  var concepts = new Array[Concept](2)
  concepts(0) = new Concept(10, "noun")
  concepts(1) = new Concept(10, "verb")

  @Test
  def should_convert_word_to_string_with_concept() {
    val word = createWord("测试", singlePosAndFreq, concepts)
    val converter = new WordStringConverter(word)
    val actual = converter.convertToString()
    println(actual)
    Assert.assertTrue(actual.startsWith("\"测试\" = {") && actual.endsWith("}"))
    Assert.assertTrue(actual.contains("concepts:[noun,verb]"))
  }

  @Test
  def should_convert_word_to_string_with_word_name_and_single_POS() {
    val word = createWord("测试", singlePosAndFreq)
    val converter = new WordStringConverter(word)
    val actual = converter.convertToString()
    println(actual)
    Assert.assertTrue(actual.startsWith("\"测试\" = {") && actual.endsWith("}"))
    Assert.assertTrue(actual.contains("POSTable:{N:100}"))
    Assert.assertTrue(actual.contains("domainType:0"))
  }

  @Test
  def should_convert_word_to_string_with_word_name_and_POSs() {
    val word = createWord("测试", posAndFreq)
    val converter = new WordStringConverter(word)
    val actual = converter.convertToString()
    println(actual)
    Assert.assertTrue(actual.startsWith("\"测试\" = {") && actual.endsWith("}"))
    Assert.assertTrue(actual.contains("POSTable:{N:100,V:20}"))
    Assert.assertTrue(actual.contains("domainType:0"))
  }

  @Test
  def should_convert_word_to_string_with_special_characters_quote() {
    val word = createWord("\"", posAndFreq)
    val converter = new WordStringConverter(word)
    val actual = converter.convertToString()
    println(actual)
    Assert.assertTrue(actual.startsWith("\"%22\" = {") && actual.endsWith("}"))
    Assert.assertTrue(actual.contains("POSTable:{N:100,V:20}"))
    Assert.assertTrue(actual.contains("domainType:0"))
  }

  @Test
  def should_convert_word_to_string_with_special_characters_brackets() {
    val word = createWord("()", posAndFreq)
    val converter = new WordStringConverter(word)
    val actual = converter.convertToString()
    println(actual)
    Assert.assertTrue(actual.startsWith("\"%28%29\" = {") && actual.endsWith("}"))
    Assert.assertTrue(actual.contains("POSTable:{N:100,V:20}"))
    Assert.assertTrue(actual.contains("domainType:0"))
  }

  @Test
  def should_convert_word_to_string_with_special_characters_square_braces() {
    val word = createWord("[]", posAndFreq)
    val converter = new WordStringConverter(word)
    val actual = converter.convertToString()
    println(actual)
    Assert.assertTrue(actual.startsWith("\"%5B%5D\" = {") && actual.endsWith("}"))
    Assert.assertTrue(actual.contains("POSTable:{N:100,V:20}"))
    Assert.assertTrue(actual.contains("domainType:0"))
  }

  @Test
  def should_convert_word_to_string_with_special_characters_braces() {
    val word = createWord("{}", posAndFreq)
    val converter = new WordStringConverter(word)
    val actual = converter.convertToString()
    println(actual)
    Assert.assertTrue(actual.startsWith("\"%7B%7D\" = {") && actual.endsWith("}"))
    Assert.assertTrue(actual.contains("POSTable:{N:100,V:20}"))
    Assert.assertTrue(actual.contains("domainType:0"))
  }

  private def createWord(wordName: String, ret: Array[Array[Int]]): IWord = {
    return createWord(wordName, ret, null)
  }

  private def createWord(wordName: String, ret: Array[Array[Int]], concepts: Array[Concept]): IWord = {

    class Word(wordName: String, ret: Array[Array[Int]], concepts: Array[Concept]) extends UnknownWord {
      override def getWordName() = wordName

      override def getDomainType() = 0

      override def getWordPOSTable = ret

      override def getConcepts = concepts
    }

    return new Word(wordName, ret, concepts)
  }


}
