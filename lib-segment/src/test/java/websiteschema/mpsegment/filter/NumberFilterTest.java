package websiteschema.mpsegment.filter;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.dict.POSUtil;

public class NumberFilterTest {

    NumberAndTimeFilter filter = new NumberAndTimeFilter();

    @Test
    public void should_merge_two_word_with_POS_M() {
        SegmentResult segmentResult = new SegmentResult(6);
        segmentResult.setWords(new String[]{"一个", "几十", "万", "人口", "的", "社区"});
        segmentResult.setPOSArray(new int[]{POSUtil.POS_M, POSUtil.POS_M, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_U, POSUtil.POS_N});
        segmentResult.setDomainTypes(new int[]{0, 0, 0, 0, 0, 0});
        segmentResult.setConcepts(new String[]{"N/A", "N/A", "N/A", "N/A", "N/A", "N/A"});

        filter.setSegmentResult(segmentResult);
        filter.filtering();
        Assert.assertEquals("一个", segmentResult.getWord(0));
        Assert.assertEquals("几十万", segmentResult.getWord(1));
        Assert.assertEquals(POSUtil.POS_M, segmentResult.getPOS(0));
        Assert.assertEquals(POSUtil.POS_M, segmentResult.getPOS(1));
    }

}
