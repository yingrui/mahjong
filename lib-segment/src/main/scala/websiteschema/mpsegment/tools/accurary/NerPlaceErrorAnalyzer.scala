package websiteschema.mpsegment.tools.accurary

import websiteschema.mpsegment.core.Word
import websiteschema.mpsegment.dict.POSUtil

class NerPlaceErrorAnalyzer extends AbstractErrorAnalyzer {

    override def analysis(expect: Word, possibleErrorWord: String) : Boolean = {
        var foundError = false
        if (possibleErrorWord.replaceAll(" ", "").equals(expect.name)) {
            if (expect.pos == POSUtil.POS_NS) {
                increaseOccur()
                addErrorWord(expect.name)
                foundError = true
            }
        }
        return foundError
    }
}
