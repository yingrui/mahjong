/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.GraphBuilder;
import websiteschema.mpsegment.dict.DictionaryFactory;

/**
 * @author ray
 */
public class GraphBuilderTest {

    @Test
    public void should_scan_for_context_freq() {
        DictionaryFactory.getInstance().loadDictionary();
        DictionaryFactory.getInstance().loadDomainDictionary();
        DictionaryFactory.getInstance().loadUserDictionary();
        GraphBuilder gBuilder = new GraphBuilder(null, false);
        String sen = "计算机会成本将会大大增加成功的机会";
        gBuilder.setSentence(sen);
        gBuilder.scanContextFreq(0);
        Map<String, Integer> contextFreq = gBuilder.getContextFreqMap();
        System.out.println(contextFreq);
        Assert.assertEquals(2, contextFreq.get("机会").intValue());
    }
}
