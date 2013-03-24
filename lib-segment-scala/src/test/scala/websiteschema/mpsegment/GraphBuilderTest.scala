package websiteschema.mpsegment

import org.junit.Assert
import org.junit.Test
import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.core.GraphBuilder
import websiteschema.mpsegment.dict.DictionaryFactory

class GraphBuilderTest {

    @Test
    def should_scan_for_context_freq() {
        DictionaryFactory().loadDictionary()
        DictionaryFactory().loadDomainDictionary()
        DictionaryFactory().loadUserDictionary()
        val gBuilder = new GraphBuilder(null, false, MPSegmentConfiguration())
        val sen = "计算机会成本将会大大增加成功的机会"
        gBuilder.setSentence(sen)
        gBuilder.scanContextFreq(0)
        val contextFreq = gBuilder.getContextFreqMap()
        println(contextFreq)
        Assert.assertEquals(2, contextFreq("机会"))
    }
}
