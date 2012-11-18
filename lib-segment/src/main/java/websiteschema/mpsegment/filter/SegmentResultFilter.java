/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.core.SegmentResult;

/**
 *
 * @author ray
 */
public class SegmentResultFilter {

    private QuerySyntaxFilter filter;
    private UnknownPlaceFilter placeFilter;
    private NumberAndTimeFilter numberAndTimeFilter;
    private UnknownNameFilter nameFilter;

    public SegmentResultFilter(MPSegmentConfiguration config) {
        filter = new QuerySyntaxFilter(config);
        nameFilter = new UnknownNameFilter(config);
        placeFilter = new UnknownPlaceFilter();
        numberAndTimeFilter = new NumberAndTimeFilter();
    }

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
