package websiteschema.mpsegment.dict.domain;

import org.apache.commons.logging.*;
import websiteschema.mpsegment.conf.MPSegmentConfiguration;

public class DomainDictFactory {

    private final static DomainDictFactory ins = new DomainDictFactory();
    private final static Log l = LogFactory.getLog("segment");
    private volatile DomainDictionary domainDict;
    private final MPSegmentConfiguration config = MPSegmentConfiguration.getInstance();
    private final boolean loadDomainDictionary = config.isLoadDomainDictionary();

    public static DomainDictFactory getInstance() {
        return ins;
    }

    public final void buildDictionary() {
        if (loadDomainDictionary) {
            DomainDictionary dict = new DomainDictionary();
            initializeDictionaryLoaderThenLoad(dict);
            domainDict = dict;
        } else {
            domainDict = new DomainDictionary();
        }
    }

    public DomainDictionary getDomainDictionary() {
        return domainDict;
    }

    public void initializeDictionaryLoaderThenLoad(DomainDictionary dict) {
        try {
            initializeAndLoad(dict);
        } catch (Exception ex) {
            l.error("Exception thrown when load domain dictionary. " + ex.getMessage());
        }
    }

    private void initializeAndLoad(DomainDictionary dict) {
        String classNames[] = config.getDomainDictLoader().split("[,; ]+");
        for (String className : classNames) {
            DomainDictLoader loader = createDictLoader(className);
            if (null != loader) {
                loader.load(dict);
            }
        }
    }

    private DomainDictLoader createDictLoader(String className) {
        try {
            Class clazz = Class.forName(className);
            DomainDictLoader loader = (DomainDictLoader) clazz.newInstance();
            return loader;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
