/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.dict.POSUtil;

/**
 *
 * @author ray
 */
public abstract class AbstractSegmentFilter implements ISegmentFilter {

    public abstract void doFilter();

    @Override
    public void filtering() {
        doFilter();
        compactSegmentResult();
    }

    @Override
    public void setSegmentResult(SegmentResult segmentResult) {
        this.segmentResult = segmentResult;
        wordPosIndexes = new int[segmentResult.length()];
    }

    void mergeWordsWithPOS(int startWordIndex, int endWordIndex, int POS) {
        int length = segmentResult.length();
        int posIndex = POS;
        if (startWordIndex < 0 || endWordIndex >= length || startWordIndex >= endWordIndex) {
            return;
        }
        if (wordPosIndexes[startWordIndex] > 0) {
            int posI1 = wordPosIndexes[startWordIndex];
            do {
                startWordIndex--;
            } while (startWordIndex >= 0 && wordPosIndexes[startWordIndex] == posI1);

            startWordIndex++;
        }
        if (startWordIndex >= 1 && wordPosIndexes[startWordIndex - 1] > 0 && wordPosIndexes[startWordIndex - 1] < 200) {
            posIndex += 200;
        }
        for (int i = startWordIndex; i <= endWordIndex; i++) {
            wordPosIndexes[i] = posIndex;
        }
    }

    void compactSegmentResult() {
        int length = segmentResult.length();
        int index = 0;
        int endIndex = 0;

        while (index < length) {
            if (wordPosIndexes[index] == 0) {
                if (index != endIndex) {
                    segmentResult.letWord1EqualWord2(endIndex, index);
                }
                index++;
            } else {
                int pos = wordPosIndexes[index];
                final int numBegin = index;
                String wordStr = segmentResult.getWord(numBegin);
                for (index++; index < length && wordPosIndexes[index] == pos; index++) {
                    wordStr = (new StringBuilder(String.valueOf(wordStr))).append(segmentResult.getWord(index)).toString();
                }

                if (pos >= 200) {
                    pos -= 200;
                }
                segmentResult.setWord(endIndex, wordStr, pos);
            }
            endIndex++;
        }
        for (int i = endIndex; i < length; i++) {
            segmentResult.setWord(i, "", POSUtil.POS_UNKOWN);
        }
        segmentResult.cutTail(endIndex);
    }

    public SegmentResult getSegmentResult() {
        return segmentResult;
    }

    public int[] getWordPosIndexes() {
        return wordPosIndexes;
    }
    int wordPosIndexes[];
    SegmentResult segmentResult;
}
