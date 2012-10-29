/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.core.SegmentResult;

/**
 *
 * @author ray
 */
public class SegmentResultFilter {

    private QuerySyntaxFilter filter = new QuerySyntaxFilter();
    private UnknownPlaceFilter placeFilter = new UnknownPlaceFilter();
    private NumberAndTimeFilter numberAndTimeFilter = new NumberAndTimeFilter();
    private UnknownNameFilter nameFilter = new UnknownNameFilter();

    public void filter(SegmentResult segmentResult) {
        placeFilter.setSegmentResult(segmentResult);
        placeFilter.filtering();

        numberAndTimeFilter.setSegmentResult(segmentResult);
        numberAndTimeFilter.filtering();

        nameFilter.setSegmentResult(segmentResult);
        nameFilter.filtering();

        filter.setSegmentResult(segmentResult);
        filter.filtering();
    }
}
