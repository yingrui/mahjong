package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.core.SegmentResult;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSegmentFilter implements ISegmentFilter {
    interface Operation {
        public void modify(SegmentResult segmentResult);
    }

    class DeleteOperation implements Operation {
        int index;
        DeleteOperation(int index) {
            this.index = index;
        }
        public void modify(SegmentResult segmentResult) {
            segmentResult.markWordToBeDeleted(index);
        }
    }

    class MergeOperation implements Operation {
        int start;
        int end;
        int pos;

        MergeOperation(int start, int end, int pos) {
            this.start = start;
            this.end = end;
            this.pos = pos;
        }

        public void modify(SegmentResult segmentResult) {
            segmentResult.merge(start, end, pos);
        }
    }

    protected SegmentResult segmentResult;
    private int wordPosIndexes[];
    private List<Operation> operationSettings = new ArrayList<Operation>();

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
        operationSettings.add(new MergeOperation(startWordIndex, endWordIndex, POS));
        markWordsHasBeenRecognized(startWordIndex, endWordIndex, POS);
    }

    protected void deleteWordAt(int index) {
        operationSettings.add(new DeleteOperation(index));
    }

    protected void compactSegmentResult() {
        if (operationSettings.size() > 0) {
            for (Operation mergeSetting : operationSettings) {
                mergeSetting.modify(segmentResult);
            }
            segmentResult.compact();
            operationSettings.clear();
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
