package websiteschema.mpsegment.dict;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.SegmentEngine;
import websiteschema.mpsegment.dict.domain.DomainDictFactory;

import java.util.List;

public class DomainDictionaryTest {

    @Test
    public void should_Loaded_Some_Synonyms_Like_PC() {
        SegmentEngine.getInstance();
        try {
            List<IWord> synonymSet = DomainDictFactory.getInstance().getDomainDictionary().getSynonymSet("个人电脑");
            Assert.assertEquals(synonymSet.get(0).getWordName(), "PC机");
            Assert.assertEquals(synonymSet.get(1).getWordName(), "个人电脑");
        } catch (Exception ex) {
            Assert.fail();
        }
        try {
            List<IWord> synonymSet = DomainDictFactory.getInstance().getDomainDictionary().getSynonymSet("PC机");
            Assert.assertEquals(synonymSet.get(0).getWordName(), "PC机");
            Assert.assertEquals(synonymSet.get(1).getWordName(), "个人电脑");
        } catch (Exception ex) {
            Assert.fail();
        }
    }
}
