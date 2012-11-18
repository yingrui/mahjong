/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.core.SegmentResult;

/**
 * @author ray
 */
public class SegmentResultFilter {

    private final ISegmentFilter[] filters;

    public SegmentResultFilter(MPSegmentConfiguration config) {
        filters = new ISegmentFilter[]{
                new UnknownPlaceFilter(),
                new NumberAndTimeFilter(),
                new UnknownNameFilter(config),
                new QuerySyntaxFilter(config)
        };
    }

    public void filter(SegmentResult segmentResult) {
        for (ISegmentFilter filter : filters) {
            filter.setSegmentResult(segmentResult);
            filter.filtering();
        }
    }
}
