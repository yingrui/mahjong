/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package me.yingrui.segment.filter

import me.yingrui.segment.dict.POSUtil

/**
*
* @author ray
*/
class UnknownPlaceFilter extends AbstractSegmentFilter {

    private val adminLevels = "省市县区乡镇村旗州"
    private val recognizeDiMing : Boolean = true

    override def doFilter() {
        if (recognizeDiMing) {
            val length = segmentResult.length()
            for (wordI <- 0 until length) {
                if (isNotMarked(wordI)) {
                    val pos = segmentResult.getPOS(wordI)
                    if (pos == POSUtil.POS_NS && wordI + 1 < length) {
                        val word = segmentResult.getWord(wordI)
                        if (segmentResult.getWord(wordI + 1).length() == 1 && adminLevels.indexOf(segmentResult.getWord(wordI + 1)) > 0 && word.lastIndexOf(segmentResult.getWord(wordI + 1)) != length - 1) {
                            setWordIndexesAndPOSForMerge(wordI, wordI + 1, POSUtil.POS_NS)
                        }
                    }
                }
            }
        }
    }
}
