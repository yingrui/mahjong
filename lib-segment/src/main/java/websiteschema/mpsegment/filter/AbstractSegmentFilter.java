package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.core.SegmentResult;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSegmentFilter implements ISegmentFilter {

    class MergeSetting {
        int start;
        int end;
        int pos;

        MergeSetting(int start, int end, int pos) {
            this.start = start;
            this.end = end;
            this.pos = pos;
        }
    }

    protected SegmentResult segmentResult;
    private int wordPosIndexes[];
    private List<MergeSetting> mergeSettings = new ArrayList<MergeSetting>();

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

    protected void setWordIndexesAndPOSForMerge(int startWordIndex, int endWordIndex, int POS) {
        mergeSettings.add(new MergeSetting(startWordIndex, endWordIndex, POS));
        markWordsHasBeenRecognized(startWordIndex, endWordIndex, POS);
    }

    protected void compactSegmentResult() {
        if (mergeSettings.size() > 0) {
            for (MergeSetting mergeSetting : mergeSettings) {
                segmentResult.merge(mergeSetting.start, mergeSetting.end, mergeSetting.pos);
            }
            segmentResult.compact();
            mergeSettings.clear();
        }
    }

    protected boolean isWordConfirmed(int wordIndex) {
        return wordPosIndexes[wordIndex] > 0;
    }

    protected boolean isNotMarked(int index) {
        return wordPosIndexes[index] <= 0;
    }

    private void markWordsHasBeenRecognized(int startWordIndex, int endWordIndex, int POS) {
        for (int i = startWordIndex; i <= endWordIndex; i++) {
            wordPosIndexes[i] = POS;
        }
    }
}
