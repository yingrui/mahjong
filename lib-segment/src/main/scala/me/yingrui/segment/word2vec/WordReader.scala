package me.yingrui.segment.word2vec

import java.io._

class WordReader(val input: Reader, val window: Int = 5) {
  private val reader = new BufferedReader(input)
  private var readedWords = List[String]()
  private val random = new scala.util.Random()

  def read(): String = readWord().toLowerCase().trim

  def readWindow(): Seq[String] = {
    if(readedWords.isEmpty) {
      for(i <- 0 until window) readedWords :+= ""
      for(i <- 0 to window) readedWords :+= read()
    } else {
      val word = read()
      readedWords = readedWords.tail :+ word
    }

    if(readedWords(window).isEmpty) {
      List[String]()
    } else {
      readedWords
    }
  }

  private def readWord(): String = {
    val word = new StringBuilder
    var ch = reader.read()
    while(ch != -1) {
      if(ch != ' ') {
        word.append(ch.asInstanceOf[Char])
      } else if(!word.isEmpty) {
        return word.toString()
      }
      ch = reader.read()
    }
    word.toString()
  }

}
