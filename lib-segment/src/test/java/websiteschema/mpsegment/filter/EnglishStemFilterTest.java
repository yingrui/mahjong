package websiteschema.mpsegment.filter;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.dict.POSUtil;

public class EnglishStemFilterTest {

    EnglishStemFilter filter = new EnglishStemFilter();

    @Test
    public void should_merge_two_reduplicating_words() {
        SegmentResult segmentResult = new SegmentResult(5);
        segmentResult.setWords(new String[]{"She", " ", "likes", " ", "hunting"});
        segmentResult.setPOSArray(new int[]{POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN, POSUtil.POS_UNKOWN});
        segmentResult.setDomainTypes(new int[]{0, 0, 0, 0});
        segmentResult.setConcepts(new String[]{"N/A", "N/A", "N/A", "N/A", "N/A"});

        filter.setSegmentResult(segmentResult);
        filter.filtering();
        Assert.assertEquals("like", segmentResult.getWord(1));
        Assert.assertEquals("hunt", segmentResult.getWord(2));
    }

}
