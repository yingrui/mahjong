package websiteschema.mpsegment.dict;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.SegmentEngine;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.core.SegmentWorker;
import websiteschema.mpsegment.dict.domain.DomainDictFactory;
import websiteschema.mpsegment.dict.domain.DomainDictionary;

public class UserDictionaryTest {

    @Test
    public void should_Loaded_Some_Words_from_User_Dictionary() {
        String str = "贝因美是中国品牌";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();
        worker.setUseDomainDictionary(true);
        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.length(), 4);
        Assert.assertEquals(words.getWord(0), "贝因美");
        Assert.assertEquals(words.getWord(1), "是");
        DomainDictionary dd = DomainDictFactory.getInstance().getDomainDictionary();

        assert (dd.iterator().hasNext());
    }
}
