package websiteschema.mpsegment.tools.accurary

import websiteschema.mpsegment.core.Word

class OtherErrorAnalyzer extends AbstractErrorAnalyzer {

  override def analysis(expect: Word, possibleErrorWord: String): Boolean = {
    increaseOccur()
    addErrorWord(expect.name)
//    println(possibleErrorWord + " -- " + expect.word)
    return true
  }
}
