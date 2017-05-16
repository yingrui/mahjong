package me.yingrui.segment

import me.yingrui.segment.conf.SegmentConfiguration
import me.yingrui.segment.core.GraphBuilder
import me.yingrui.segment.dict.DictionaryFactory
import org.junit.{Assert, Test}

class GraphBuilderTest {

    @Test
    def should_scan_for_context_freq() {
        DictionaryFactory().loadDictionary()
        DictionaryFactory().loadDomainDictionary()
        DictionaryFactory().loadUserDictionary()
        val gBuilder = new GraphBuilder(null, false, SegmentConfiguration())
        val sen = "计算机会成本将会大大增加成功的机会"
        gBuilder.setSentence(sen)
        gBuilder.scanContextFreq(0)
        val contextFreq = gBuilder.getContextFreqMap()
        println(contextFreq)
        Assert.assertEquals(2, contextFreq("机会"))
    }
}
