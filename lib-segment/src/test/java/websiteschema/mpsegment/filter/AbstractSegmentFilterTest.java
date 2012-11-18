package websiteschema.mpsegment.filter;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.dict.POSUtil;

public class AbstractSegmentFilterTest {

    @Test
    public void should_merge_two_word_with_POS_NR() {
        Filter filter = new Filter();
        SegmentResult result = getSegmentResult();
        filter.setSegmentResult(result);
        filter.mergeWordsWithPOS(1, 2, POSUtil.POS_NR);
        filter.compactSegmentResult();
        Assert.assertEquals("三丰", result.getWord(1));
        Assert.assertEquals(POSUtil.POS_NR, result.getPOS(1));
        Assert.assertEquals(3, result.length());
    }

    @Test
    public void should_merge_adjacent_words_with_POS_NR() {
        Filter filter = new Filter();
        SegmentResult result = getSegmentResult();
        filter.setSegmentResult(result);
        filter.mergeWordsWithPOS(1, 3, POSUtil.POS_NR);
        filter.filtering();
        Assert.assertEquals("三丰南", result.getWord(1));
        Assert.assertEquals(POSUtil.POS_NR, result.getPOS(1));
        Assert.assertEquals(2, result.length());
    }

    private SegmentResult getSegmentResult() {
        SegmentResult result = new SegmentResult(4);
        result.setWords(new String[]{"张", "三", "丰", "南"});
        result.setPOSArray(new int[]{POSUtil.POS_V, POSUtil.POS_M, POSUtil.POS_N, POSUtil.POS_N});
        result.setDomainTypes(new int[]{0, 0, 0, 0});
        result.setConcepts(new String[]{"N/A", "N/A", "N/A", "N/A"});
        return result;
    }

    class Filter extends AbstractSegmentFilter {
        @Override
        public void doFilter() {
        }
    }
}
