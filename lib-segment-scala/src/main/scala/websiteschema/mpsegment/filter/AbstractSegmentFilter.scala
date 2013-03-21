//package websiteschema.mpsegment.filter;
//
//import websiteschema.mpsegment.core.SegmentResult;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public abstract class AbstractSegmentFilter extends ISegmentFilter {
//    interface Operation {
//        def modify(segmentResult: SegmentResult) 
//    }
//
//    class DeleteOperation extends Operation {
//        var index: Int = null
//        DeleteOperation(Int index) {
//            this.index = index;
//        }
//        def modify(segmentResult: SegmentResult) {
//            segmentResult.markWordToBeDeleted(index);
//        }
//    }
//
//    class MergeOperation extends Operation {
//        var start: Int = null
//        var end: Int = null
//        var pos: Int = null
//
//        MergeOperation(Int start, Int end, Int pos) {
//            this.start = start;
//            this.end = end;
//            this.pos = pos;
//        }
//
//        def modify(segmentResult: SegmentResult) {
//            segmentResult.merge(start, end, pos);
//        }
//    }
//
//    protected SegmentResult segmentResult;
//    private var wordPosIndexes : Array[Int] = null
//    private var operationSettings : List[Operation] = new ArrayList[Operation]();
//
//    public abstract void doFilter();
//
//    override def filtering() {
//        doFilter();
//        compactSegmentResult();
//    }
//
//    override def setSegmentResult(segmentResult: SegmentResult) {
//        this.segmentResult = segmentResult;
//        wordPosIndexes = new Int[segmentResult.length()];
//    }
//
//    def setWordIndexesAndPOSForMerge(startWordIndex: Int, endWordIndex: Int, POS: Int) {
//        operationSettings.add(new MergeOperation(startWordIndex, endWordIndex, POS));
//        markWordsHasBeenRecognized(startWordIndex, endWordIndex, POS);
//    }
//
//    def deleteWordAt(index: Int) {
//        operationSettings.add(new DeleteOperation(index));
//    }
//
//    def compactSegmentResult() {
//        if (operationSettings.size() > 0) {
//            for (mergeSetting <- operationSettings) {
//                mergeSetting.modify(segmentResult);
//            }
//            segmentResult.compact();
//            operationSettings.clear();
//        }
//    }
//
//    def isWordConfirmed(wordIndex: Int) : Boolean = {
//        return wordPosIndexes(wordIndex) > 0;
//    }
//
//    def isNotMarked(index: Int) : Boolean = {
//        return wordPosIndexes(index) <= 0;
//    }
//
//    private def markWordsHasBeenRecognized(startWordIndex: Int, endWordIndex: Int, POS: Int) {
//        for (Int i = startWordIndex; i <= endWordIndex; i++) {
//            wordPosIndexes(i) = POS;
//        }
//    }
//}
