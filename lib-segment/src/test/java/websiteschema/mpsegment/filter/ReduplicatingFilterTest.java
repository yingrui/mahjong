package websiteschema.mpsegment.filter;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.dict.POSUtil;

public class ReduplicatingFilterTest {

    ReduplicatingFilter filter = new ReduplicatingFilter();

    @Test
    public void should_merge_two_reduplicating_words() {
        SegmentResult segmentResult = new SegmentResult(4);
        segmentResult.setWords(new String[]{"一", "件", "件", "物品"});
        segmentResult.setPOSArray(new int[]{POSUtil.POS_M, POSUtil.POS_A, POSUtil.POS_A, POSUtil.POS_N});
        segmentResult.setDomainTypes(new int[]{0, 0, 0, 0});
        segmentResult.setConcepts(new String[]{"N/A", "N/A", "N/A", "N/A"});

        filter.setSegmentResult(segmentResult);
        filter.filtering();
        Assert.assertEquals("一件件", segmentResult.getWord(0));
        Assert.assertEquals(POSUtil.POS_A, segmentResult.getPOS(0));
    }

    @Test
    public void should_merge_three_reduplicating_words() {
        SegmentResult segmentResult = new SegmentResult(3);
        segmentResult.setWords(new String[]{"件", "件", "物品"});
        segmentResult.setPOSArray(new int[]{POSUtil.POS_M, POSUtil.POS_M, POSUtil.POS_N});
        segmentResult.setDomainTypes(new int[]{0, 0, 0});
        segmentResult.setConcepts(new String[]{"N/A", "N/A", "N/A"});

        filter.setSegmentResult(segmentResult);
        filter.filtering();
        Assert.assertEquals("件件", segmentResult.getWord(0));
        Assert.assertEquals(POSUtil.POS_M, segmentResult.getPOS(0));
    }

}
