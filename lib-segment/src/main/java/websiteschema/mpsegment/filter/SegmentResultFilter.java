/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.filter;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.core.SegmentResult;

import java.util.ArrayList;

/**
 * @author ray
 */
public class SegmentResultFilter {

    private final ArrayList<ISegmentFilter> filters;

    public SegmentResultFilter(MPSegmentConfiguration config) {
        filters = new ArrayList();
        filters.add(new UnknownPlaceFilter());
        filters.add(new NumberAndTimeFilter());
        filters.add(new UnknownNameFilter(config));
        filters.add(new ReduplicatingFilter());
        filters.add(new QuerySyntaxFilter(config));
        if (config.is("segment.lang.en")) {
            filters.add(new EnglishStemFilter());
        }
    }

    public void filter(SegmentResult segmentResult) {
        for (ISegmentFilter filter : filters) {
            filter.setSegmentResult(segmentResult);
            filter.filtering();
        }
    }
}
