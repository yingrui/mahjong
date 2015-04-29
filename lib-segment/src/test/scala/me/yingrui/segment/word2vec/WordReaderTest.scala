package me.yingrui.segment.word2vec

import java.io.StringReader

import org.scalatest.{Matchers, FunSuite}

class WordReaderTest extends FunSuite with Matchers {

  test("should read and lowercase words") {
    val input = new StringReader("Word1   woRd2")
    val wordReader = new WordReader(input)

    wordReader.read() should be ("word1")
    wordReader.read() should be ("word2")
    wordReader.read() should be ("")
  }

  test("should read words in specify window") {
    val input = new StringReader("w0 w1 w2 w3 w4 w5 w6 w7 w8 w9 w10")
    val wordReader = new WordReader(input)
    wordReader.readWindow() should be (Array("", "", "", "", "", "w0", "w1", "w2", "w3", "w4", "w5"))
    wordReader.readWindow() should be (Array("", "", "", "", "w0", "w1", "w2", "w3", "w4", "w5", "w6"))
    wordReader.readWindow() should be (Array("", "", "", "w0", "w1", "w2", "w3", "w4", "w5", "w6","w7"))
    wordReader.readWindow() should be (Array("", "", "w0", "w1", "w2", "w3", "w4", "w5", "w6", "w7", "w8"))
    wordReader.readWindow() should be (Array("", "w0", "w1", "w2", "w3", "w4", "w5", "w6", "w7", "w8", "w9"))
    wordReader.readWindow() should be (Array("w0", "w1", "w2", "w3", "w4", "w5", "w6", "w7", "w8", "w9", "w10"))
    wordReader.readWindow() should be (Array("w1", "w2", "w3", "w4", "w5", "w6", "w7", "w8", "w9", "w10", ""))
  }

  test("should return empty list when read to then end") {
    val input = new StringReader("w0 w1 w2 w3 w4 w5 w6 w7 w8 w9 w10")
    val wordReader = new WordReader(input)
    for(i <- 0 until 10) {
      wordReader.readWindow()
    }
    wordReader.readWindow() should be (Array("w5", "w6", "w7", "w8", "w9", "w10", "", "", "", "", ""))
    wordReader.readWindow() should be (Array())
  }
}
