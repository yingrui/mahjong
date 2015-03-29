package me.yingrui.segment.filter

import org.junit.{Assert, Test}
import me.yingrui.segment.Assertion

object NameEntityRecognizerBuilderSingleton {
  val builder = new NameEntityRecognizerBuilder()
  builder.load("PFR-199801-utf-8.txt")
  val result = builder.analysis
}

class NameEntityRecognizerBuilderTest {

  val result = NameEntityRecognizerBuilderSingleton.result

  @Test
  def should_load_pfr_corpus_and_analysis_frequency() {
    Assert.assertEquals(1821, result.charFreq("张"))
    Assert.assertEquals(2557, result.wordFreq("说"))
  }

  @Test
  def should_get_frequency_of_each_han_character_as_normal_word() {
    Assert.assertEquals(1010, result.freqAsNormalWord("张"))
    Assert.assertEquals(result.charFreq("张"), result.freqAsNormalWord("张") + result.freqAsNameWord("张"))
  }

  @Test
  def should_get_diff_of_each_han_character() {
    println(result.diff("张"))
    println(result.diff("冯"))
    Assert.assertTrue(result.diff("张") > 0.44D)
    Assert.assertTrue(result.diff("冯") > 0.98D)
  }

  @Test
  def should_get_right_boundary_word_of_name_entity() {
    Assert.assertEquals(1026, result.freqAsRightBoundary("说"))
    println(result.rightBoundaryDiff("说"))
    Assert.assertTrue(result.rightBoundaryDiff("说") > 0.4D)
  }

  @Test
  def should_get_right_boundary_word_of_name_entity_tmp() {
    println(result.freqAsRightBoundary("："))
    println(result.rightBoundaryDiff("："))
    println(result.rightBoundaryMutualInformation("："))
  }

  @Test
  def should_get_left_boundary_word_of_name_entity() {
    val boundaryWord = "和"
    Assert.assertEquals(505, result.freqAsLeftBoundary(boundaryWord))
    println(result.leftBoundaryDiff(boundaryWord))
    Assert.assertTrue(result.leftBoundaryDiff(boundaryWord) > 0.04D)
  }

  @Test
  def should_get_mutual_information_between_left_boundary_and_name_entity() {
    val boundaryWord = "和"
    println(result.leftBoundaryMutualInformation(boundaryWord))
    Assert.assertTrue(result.leftBoundaryMutualInformation(boundaryWord) > 0.556D)
  }

  @Test
  def should_get_mutual_information_between_right_boundary_and_name_entity() {
    val boundaryWord = "和"
    println(result.rightBoundaryMutualInformation(boundaryWord))
    Assert.assertTrue(result.rightBoundaryMutualInformation(boundaryWord) > 0.31D)
  }

  @Test
  def should_get_mutual_information_between_word_and_sentence_start_or_end {
    println(result.rightBoundaryMutualInformation("\0"))
    println(result.leftBoundaryMutualInformation("\0"))
    Assert.assertFalse(result.leftBoundaryMutualInformation("\0").isInfinity)
    Assert.assertFalse(result.rightBoundaryMutualInformation("\0").isInfinity)
  }

  @Test
  def should_get_condition_probability_of_name_entity() {
    val log2Prob = result.conditionProbability(List("邓", "小", "平"))
    println(log2Prob)
    Assertion.shouldBeEqual(-6.843965276915578, log2Prob)

    println(result.conditionProbability(List("毛", "泽", "东")))
    println(result.conditionProbability(List("毛", "泽")))
    println(result.conditionProbability(List("冯", "仁", "代")))
    println(result.conditionProbability(List("明", "代")))
    println(result.conditionProbability(List("青", "简")))
    println(result.conditionProbability(List("您", "好")))
  }

  @Test
  def should_get_internal_mutual_information_of_name_entity() {
    val mutualInformation = result.mutualInformation(List("邓", "小", "平"))
    println(mutualInformation)

    println(result.mutualInformation(List("毛", "泽", "东")))
    println(result.mutualInformation(List("张", "飞")))
    println(result.mutualInformation(List("冯", "仁", "代")))
    println(result.mutualInformation(List("明", "代")))
    println(result.mutualInformation(List("青", "简")))
    println(result.mutualInformation(List("您", "好")))
  }

}