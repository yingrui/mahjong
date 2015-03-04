package websiteschema.mpsegment.tools.accurary

import websiteschema.mpsegment.core.Word
import websiteschema.mpsegment.dict.POSUtil

class NerNameErrorAnalyzer extends AbstractErrorAnalyzer {

  override def analysis(expect: Word, possibleErrorWord: String): Boolean = {
    var foundError = false
    if (possibleErrorWord.replaceAll(" ", "").equals(expect.name)) {
      if (expect.pos == POSUtil.POS_NR) {
//        println(possibleErrorWord + " -- " + expect.word)
        increaseOccur()
        addErrorWord(expect.name)
        foundError = true
      }
    }
    return foundError
  }
}