//package websiteschema.mpsegment.core;
//
//import websiteschema.mpsegment.conf.MPSegmentConfiguration;
//import websiteschema.mpsegment.dict.DictionaryFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
//class SegmentEngine {
//
//    private static ThreadLocal[SegmentWorker] workers = new ThreadLocal();
//    private static SegmentEngine instance = new SegmentEngine();
//    private var configuration : MPSegmentConfiguration = MPSegmentConfiguration.getInstance();
//
//    private SegmentEngine() {
//        loadDictionary();
//    }
//
//    private def loadDictionary() {
//        DictionaryFactory.getInstance().loadDictionary();
//        DictionaryFactory.getInstance().loadDomainDictionary();
//        DictionaryFactory.getInstance().loadUserDictionary();
//    }
//
//    public static SegmentEngine getInstance() {
//        return instance;
//    }
//
//    def getReusableSegmentWorker() : SegmentWorker = {
//        SegmentWorker segmentWorker = workers.get();
//        if (null == segmentWorker) {
//            segmentWorker = new SegmentWorker(configuration);
//            workers.set(segmentWorker);
//        }
//
//        return segmentWorker;
//    }
//
//    def getSegmentWorker() : SegmentWorker = {
//        return new SegmentWorker(configuration);
//    }
//
//    def getSegmentWorker(Map<String,String> config) : SegmentWorker = {
//        return new SegmentWorker(new MPSegmentConfiguration(config));
//    }
//
//    def getSegmentWorker(String ... props) : SegmentWorker = {
//        Map[String,String] map = null;
//        if(null != props) {
//            map = new HashMap[String,String](props.length);
//            for(String p : props) {
//                Array[String] keyAndValue = p.split("(=|->)");
//                if(null != keyAndValue && keyAndValue.length == 2) {
//                    map.put(keyAndValue(0).trim(), keyAndValue(1).trim());
//                }
//            }
//        }
//        return getSegmentWorker(map);
//    }
//}
