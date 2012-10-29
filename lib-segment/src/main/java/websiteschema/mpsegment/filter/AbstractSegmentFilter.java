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

    void mergeWordsWithPOS(int firstWordIndex, int secondWordIndex, int POS) {
        int length = segmentResult.length();
        int posIndex = POS;
        if (firstWordIndex < 0 || secondWordIndex >= length || firstWordIndex >= secondWordIndex) {
            return;
        }
        if (wordPosIndexes[firstWordIndex] > 0) {
            int posI1 = wordPosIndexes[firstWordIndex];
            do {
                firstWordIndex--;
            } while (firstWordIndex >= 0 && wordPosIndexes[firstWordIndex] == posI1);

            firstWordIndex++;
        }
        if (firstWordIndex >= 1 && wordPosIndexes[firstWordIndex - 1] > 0 && wordPosIndexes[firstWordIndex - 1] < 200) {
            posIndex += 200;
        }
        for (int i = firstWordIndex; i <= secondWordIndex; i++) {
            wordPosIndexes[i] = posIndex;
        }
    }

    void compactSegmentResult() {
        int length = segmentResult.length();
        int i1 = 0;
        int j1 = 0;

        while (i1 < length) {
            if (wordPosIndexes[i1] == 0) {
                if (i1 != j1) {
                    segmentResult.letWord1EqualWord2(j1, i1);
                }
                i1++;
                j1++;
            } else {
                int k1 = wordPosIndexes[i1];
                final int numBegin = i1;
                String s1 = segmentResult.getWord(numBegin);
                for (i1++; i1 < length && wordPosIndexes[i1] == k1; i1++) {
                    s1 = (new StringBuilder(String.valueOf(s1))).append(segmentResult.getWord(i1)).toString();
                }

                if (k1 >= 200) {
                    k1 -= 200;
                }
                segmentResult.setWord(j1, s1, k1);
                j1++;
            }
        }
        for (int i2 = j1; i2 < length; i2++) {
            segmentResult.setWord(i2, "", POSUtil.POS_UNKOWN);
        }
        segmentResult.cutTail(j1);
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
