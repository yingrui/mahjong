package me.yingrui.segment.filter

import me.yingrui.segment.util.CharCheckUtil

class ReduplicatingFilter extends AbstractSegmentFilter {

    override def doFilter() {
        val length = segmentResult.length()
        for (index <- 1 until length) {
            val lastWord = getWord(index - 1)
            val word = getWord(index)
            if (isChineseWord(lastWord) && lastWord.equals(word)) {
                if (index - 2 >= 0) {
                    if (getWord(index - 2).equals("一")) {
                        setWordIndexesAndPOSForMerge(index - 2, index, segmentResult.getPOS(index - 1))
                    }
                } else {
                    setWordIndexesAndPOSForMerge(index - 1, index, segmentResult.getPOS(index - 1))
                }
            }

            if(word.length() == 2 && word.charAt(0) == word.charAt(1)) {
                if(lastWord.equals("一")) {
                    setWordIndexesAndPOSForMerge(index - 1, index, segmentResult.getPOS(index))
                }
            }
        }
    }

    private def isChineseWord(lastWord: String) : Boolean = {
        return lastWord.length() == 1 && CharCheckUtil.isChinese(lastWord)
    }

    private def getWord(i: Int) : String = {
        return segmentResult.getWord(i)
    }
}
