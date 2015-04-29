package me.yingrui.segment.word2vec

import org.scalatest.{Matchers, FunSuite}

class VocabularyTest extends FunSuite with Matchers {

  test("should add and get word") {
    val vocab = Vocabulary()
    vocab.add("word1")
    vocab.getCount("word1") should be (1)
  }

  test("should return word index") {
    val vocab = Vocabulary()
    vocab.add("word1")
    vocab.getIndex("word1") should be(1)
    vocab.add("word2")
    vocab.getIndex("word2") should be (2)

    vocab.getIndex("not_exists") should be (0)
    vocab.getIndex("") should be (0)
  }

  test("should increase word count") {
    val vocab = Vocabulary()
    vocab.add("word1")
    vocab.add("word1")
    vocab.getCount("word1") should be (2)
  }

  test("should return total word count and vocabulary size") {
    val vocab = Vocabulary()
    vocab.add("word1")
    vocab.add("word1")
    vocab.add("word2")
    vocab.getTotalWordCount should be (3)
    vocab.size should be (3)
  }

  test("should return word count 0 when word doesn't exist") {
    val vocab = Vocabulary()
    vocab.add("word1")
    val index = vocab.getCount("word2")
    index should be (0)
  }

}
