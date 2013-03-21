package websiteschema.mpsegment.dict

import org.junit.Assert
import org.junit.Test

class HeadIndexerTest {

    @Test
    def should_build_HeadIndexer() {
        val headIndexer = HeadIndexer(new WordImpl("词12"))
        headIndexer.add(new WordImpl("词3"))
        headIndexer.add(new WordImpl("词2"))
        headIndexer.add(new WordImpl("词4"))
        Assert.assertEquals(3, headIndexer.getMaxWordLength())

        val word = headIndexer.findWord("词42")
        Assert.assertEquals("词4", word.getWordName())
    }

    @Test
    def should_build_HeadIndexer_with_head_length() {
        val headIndexer = HeadIndexer(new WordImpl("词12"), 2)
        headIndexer.add(new WordImpl("词13"))
        headIndexer.add(new WordImpl("词14"))
        Assert.assertEquals(3, headIndexer.getMaxWordLength())

        var word = headIndexer.findWord("词142")
        Assert.assertEquals("词14", word.getWordName())

        word = headIndexer.findWord("词122")
        Assert.assertEquals("词12", word.getWordName())

        word = headIndexer.findWord("词242")
        Assert.assertNull(word)
    }

    @Test
    def should_throw_exception_when_add_word_is_not_start_with_head_str() {
        val headIndexer = HeadIndexer(new WordImpl("词12"), 2)
        try {
            headIndexer.add(new WordImpl("词33"))
            Assert.fail()
        } catch {
          case e: Throwable =>
            Assert.assertEquals("Head string is not start with 词1", e.getMessage())
        }
    }
}
