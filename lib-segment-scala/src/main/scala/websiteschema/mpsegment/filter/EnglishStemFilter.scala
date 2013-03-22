package websiteschema.mpsegment.filter

import websiteschema.mpsegment.lang.en.PorterStemmer
import websiteschema.mpsegment.util.CharCheckUtil

class EnglishStemFilter extends AbstractSegmentFilter {
    val porterStemmer = new PorterStemmer()

    override def doFilter() {
        val length = segmentResult.length()
        for (index <- 0 until length) {
            val word = segmentResult.getWord(index)
            if (CharCheckUtil.isEnglish(word)) {
                segmentResult.setWord(index, porterStemmer.stem(word))
                deleteNextSpaceWord(index, length)
            }
        }
    }

    private def deleteNextSpaceWord(index: Int, length: Int) {
        for (i <- index + 1 until length) {
            if (CharCheckUtil.isWhiteSpace(segmentResult.getWord(i))) {
                deleteWordAt(i)
            } else {
                return
            }
        }
    }
}
