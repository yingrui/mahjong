///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package websiteschema.mpsegment;
//
//import org.junit.Assert;
//import org.junit.Test;
//import websiteschema.mpsegment.conf.MPSegmentConfiguration;
//import websiteschema.mpsegment.core.GraphBuilder;
//import websiteschema.mpsegment.dict.DictionaryFactory;
//
//import java.util.Map;
//
///**
// * @author ray
// */
//class GraphBuilderTest {
//
//    @Test
//    def should_scan_for_context_freq() {
//        DictionaryFactory.getInstance().loadDictionary();
//        DictionaryFactory.getInstance().loadDomainDictionary();
//        DictionaryFactory.getInstance().loadUserDictionary();
//        var gBuilder = new GraphBuilder(null, false, MPSegmentConfiguration.getInstance())
//        var sen = "计算机会成本将会大大增加成功的机会"
//        gBuilder.setSentence(sen);
//        gBuilder.scanContextFreq(0);
//        var contextFreq = gBuilder.getContextFreqMap()
//        println(contextFreq);
//        Assert.assertEquals(2, contextFreq.get("机会").intValue());
//    }
//}
