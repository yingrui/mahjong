package me.yingrui.segment.tools.accurary

import me.yingrui.segment.core.Word

class OtherErrorAnalyzer extends AbstractErrorAnalyzer {

  override def analysis(expect: Word, possibleErrorWord: String): Boolean = {
    increaseOccur()
    addErrorWord(expect.name)
//    println(possibleErrorWord + " -- " + expect.word)
    return true
  }
}
