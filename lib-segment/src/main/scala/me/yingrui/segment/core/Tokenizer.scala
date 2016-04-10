package me.yingrui.segment.core

trait Tokenizer {

  def tokenize(sentence: String): Array[String]

}