package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.core.SegmentResult;

public interface ISegmentFilter {

    public void filtering();

    public void setSegmentResult(SegmentResult segmentResult);
}