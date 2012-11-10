package websiteschema.mpsegment;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.dict.POSUtil;
import websiteschema.mpsegment.tools.PFRCorpusLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class PFRCorpusLoaderTest {

    @Test
    public void should_load_word_and_pos_and_domain_type() throws IOException {
        InputStream inputStream = getFakeInputStream("19980101-02-005-001/m  [国务院/nt  侨办/j]nt  发表/v  新年/t  贺词/n ");
        PFRCorpusLoader loader = new PFRCorpusLoader(inputStream);
        SegmentResult result = loader.readLine();
        Assert.assertEquals("国务院", result.getWord(0));
        Assert.assertEquals(POSUtil.POS_NT, result.getPOS(0));
        Assert.assertEquals(POSUtil.POS_J, result.getPOS(1));
        Assert.assertEquals(POSUtil.POS_NT, result.getDomainType(0));
        Assert.assertEquals(POSUtil.POS_NT, result.getDomainType(1));
        Assert.assertEquals(0, result.getDomainType(2));
        inputStream.close();
    }
    @Test
    public void should_load_concepts_and_concept_with_POS_J() throws IOException {
        InputStream inputStream = getFakeInputStream("19980101-02-005-001/m  [国务院/nt  侨办/j]nt  发表/v  新年/t  贺词/n ");
        PFRCorpusLoader loader = new PFRCorpusLoader(inputStream);
        SegmentResult result = loader.readLine();
        Assert.assertEquals("n-organization", result.getConcept(0));
        Assert.assertEquals("n-organization", result.getConcept(1));
        Assert.assertEquals("v-social-activity", result.getConcept(2));
        Assert.assertEquals("N/A", result.getConcept(3));
        Assert.assertEquals("n-creation", result.getConcept(4));
        inputStream.close();
    }

    private InputStream getFakeInputStream(String text) throws UnsupportedEncodingException {
        return new ByteArrayInputStream(text.getBytes("utf-8"));
    }
}
