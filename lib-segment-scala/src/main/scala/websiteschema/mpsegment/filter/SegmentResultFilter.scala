///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package websiteschema.mpsegment.filter;
//
//import websiteschema.mpsegment.conf.MPSegmentConfiguration;
//import websiteschema.mpsegment.core.SegmentResult;
//
//import java.util.ArrayList;
//
///**
// * @author ray
// */
//class SegmentResultFilter {
//
//    private var filters : ArrayList[ISegmentFilter] = null
//
//    public SegmentResultFilter(MPSegmentConfiguration config) {
//        filters = new ArrayList();
//        filters.add(new UnknownPlaceFilter());
//        filters.add(new NumberAndTimeFilter());
//        filters.add(new UnknownNameFilter(config));
//        filters.add(new ReduplicatingFilter());
//        filters.add(new QuerySyntaxFilter(config));
//        if (config.is("segment.lang.en")) {
//            filters.add(new EnglishStemFilter());
//        }
//
//        if(config.isHalfShapeAll() || config.isUpperCaseAll()) {
//            filters.add(new UpperCaseAndHalfShapeFilter(config.isHalfShapeAll(), config.isUpperCaseAll()));
//        }
//    }
//
//    def filter(segmentResult: SegmentResult) {
//        for (filter <- filters) {
//            filter.setSegmentResult(segmentResult);
//            filter.filtering();
//        }
//    }
//}
