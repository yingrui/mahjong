/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.tools;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.dict.ChNameDictionary;
import websiteschema.mpsegment.dict.POSUtil;

import java.io.*;

/**
 * @author ray
 */
public class PFRCorpusLoaderTest {

    public PFRCorpusLoader buildPFRCorpusLoader(String corpus) throws UnsupportedEncodingException {
        byte bytes[] = corpus.getBytes("utf-8");
        return new PFRCorpusLoader(new ByteArrayInputStream(bytes), "utf-8");
    }

    @Test
    public void should_load_Chinese_name_dictionary_correctly() throws IOException {
        String corpus = "19980124-12-006-002/m  由/p  [人民/n  出版社/n]nt  、/w  [新华/nz  文摘/n  社/n]nt  、/w  [青岛/ns  双星/nz  集团/n]nt  联合/v  主办/v  的/u  『/w  新华/nz  文摘/n  双星/nz  文学奖/n  』/w  评选/vn  活动/vn  经过/p  近/a  两/m  年/q  读者/n  广泛/ad  推荐/v  和/c  专家/n  无记名/d  投票/v  ，/w  近日/t  揭晓/v  ，/w  梁/nr  晓声/nr  、/w  毕/nr  淑敏/nr  双/m  获/v  『/w  新华/nz  文摘/n  双星/nz  文学奖/n  』/w  。/w";
        PFRCorpusLoader loader = buildPFRCorpusLoader(corpus);
        SegmentResult result = loader.readLine();
        Assert.assertEquals(
                "由人民出版社、新华文摘社、青岛双星集团联合主办的『新华文摘双星文学奖』评选活动经过近两年读者广泛推荐和专家无记名投票，近日揭晓，梁晓声、毕淑敏双获『新华文摘双星文学奖』。",
                result.toOriginalString());
        Assert.assertEquals(result.getConcept(1), POSUtil.getPOSIndex("NT"));
        Assert.assertEquals(result.getConcept(2), POSUtil.getPOSIndex("NT"));
        Assert.assertEquals(result.getPOS(1),POSUtil.getPOSIndex("N"));
        Assert.assertEquals(result.getPOS(2),POSUtil.getPOSIndex("N"));
    }

}
