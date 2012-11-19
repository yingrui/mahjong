package websiteschema.mpsegment.core;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.dict.DictionaryFactory;

import java.util.HashMap;
import java.util.Map;

public class SegmentEngine {

    private final static ThreadLocal<SegmentWorker> workers = new ThreadLocal();
    private final static SegmentEngine instance = new SegmentEngine();
    private final MPSegmentConfiguration configuration = MPSegmentConfiguration.getInstance();

    private SegmentEngine() {
        loadDictionary();
    }

    private void loadDictionary() {
        DictionaryFactory.getInstance().loadDictionary();
        DictionaryFactory.getInstance().loadDomainDictionary();
        DictionaryFactory.getInstance().loadUserDictionary();
    }

    public static SegmentEngine getInstance() {
        return instance;
    }

    public SegmentWorker getReusableSegmentWorker() {
        SegmentWorker segmentWorker = workers.get();
        if (null == segmentWorker) {
            segmentWorker = new SegmentWorker(configuration);
            workers.set(segmentWorker);
        }

        return segmentWorker;
    }

    public SegmentWorker getSegmentWorker() {
        return new SegmentWorker(configuration);
    }

    public SegmentWorker getSegmentWorker(Map<String,String> config) {
        return new SegmentWorker(new MPSegmentConfiguration(config));
    }

    public SegmentWorker getSegmentWorker(String ... props) {
        Map<String, String> map = null;
        if(null != props) {
            map = new HashMap<String, String>(props.length);
            for(String p : props) {
                String[] keyAndValue = p.split("(=|->)");
                if(null != keyAndValue && keyAndValue.length == 2) {
                    map.put(keyAndValue[0].trim(), keyAndValue[1].trim());
                }
            }
        }
        return getSegmentWorker(map);
    }
}
