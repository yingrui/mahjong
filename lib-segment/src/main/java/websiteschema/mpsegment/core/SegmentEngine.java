package websiteschema.mpsegment.core;

import websiteschema.mpsegment.dict.DictionaryFactory;

public class SegmentEngine {

    private final static ThreadLocal<SegmentWorker> workers = new ThreadLocal();
    private final static SegmentEngine instance = new SegmentEngine();

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
            segmentWorker = new SegmentWorker();
            workers.set(segmentWorker);
        }

        return segmentWorker;
    }

    public SegmentWorker getSegmentWorker() {
        return new SegmentWorker();
    }
}
