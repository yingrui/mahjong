/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.dict.POSUtil;

/**
 *
 * @author ray
 */
public class UnknownPlaceFilter extends AbstractSegmentFilter {

    private static String adminLevels = "省市县区乡镇村旗州";
    private boolean recognizeDiMing = true;

    @Override
    public void doFilter() {
        if (recognizeDiMing) {
            int length = segmentResult.length();
            for (int wordI = 0; wordI < length; wordI++) {
                if (wordPosIndexes[wordI] <= 0) {
                    int pos = segmentResult.getPOS(wordI);
                    if (pos == POSUtil.POS_NS && wordI + 1 < length) {
                        String word = segmentResult.getWord(wordI);
                        if (segmentResult.getWord(wordI + 1).length() == 1 && adminLevels.indexOf(segmentResult.getWord(wordI + 1)) > 0 && word.lastIndexOf(segmentResult.getWord(wordI + 1)) != length - 1) {
                            mergeWordsWithPOS(wordI, wordI + 1, POSUtil.POS_NS);
                        }
                    }
                }
            }
        }
    }
}
