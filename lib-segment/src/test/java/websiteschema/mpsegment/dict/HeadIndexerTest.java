/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.dict;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author ray
 */
public class HeadIndexerTest {

    @Test
    public void should_build_HeadIndexer() throws DictionaryException {
        HeadIndexer headIndexer = new HeadIndexer(new WordImpl("词12"));
        headIndexer.add(new WordImpl("词3"));
        headIndexer.add(new WordImpl("词2"));
        headIndexer.add(new WordImpl("词4"));
        Assert.assertEquals(3, headIndexer.getMaxWordLength());

        IWord word = headIndexer.findWord("词42");
        Assert.assertEquals("词4", word.getWordName());
    }

    @Test
    public void should_build_HeadIndexer_with_head_length() throws DictionaryException {
        HeadIndexer headIndexer = new HeadIndexer(new WordImpl("词12"), 2);
        headIndexer.add(new WordImpl("词13"));
        headIndexer.add(new WordImpl("词14"));
        Assert.assertEquals(3, headIndexer.getMaxWordLength());

        IWord word = headIndexer.findWord("词142");
        Assert.assertEquals("词14", word.getWordName());

        word = headIndexer.findWord("词122");
        Assert.assertEquals("词12", word.getWordName());

        word = headIndexer.findWord("词242");
        Assert.assertNull(word);
    }

    @Test
    public void should_throw_exception_when_add_word_is_not_start_with_head_str() {
        HeadIndexer headIndexer = new HeadIndexer(new WordImpl("词12"), 2);
        try {
            headIndexer.add(new WordImpl("词33"));
            Assert.fail();
        } catch (DictionaryException e) {
            Assert.assertEquals("Head string is not start with 词1", e.getMessage());
        }
    }
}
