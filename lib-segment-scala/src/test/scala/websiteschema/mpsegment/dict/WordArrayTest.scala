package websiteschema.mpsegment.dict;

import org.junit.Assert
import org.junit.Test

class WordArrayTest {

    @Test
    def should_return_sorted_array_in_binary_word_array() {
        val word1 = new WordImpl("好1")
        val word2 = new WordImpl("好4")
        val word3 = new WordImpl("好2")
        val word4 = new WordImpl("好3")


        val wordArray = BinaryWordArray(List[IWord](word1, word2, word3).toArray)
        wordArray.add(word4);

        val words = wordArray.getWordItems()
        Assert.assertEquals("好1", words(0).getWordName());
        Assert.assertEquals("好2", words(1).getWordName());
        Assert.assertEquals("好3", words(2).getWordName());
        Assert.assertEquals("好4", words(3).getWordName());
    }

    @Test
    def should_return_unsorted_array_in_hash_word_array() {
        val word1 = new WordImpl("好1")
        val word2 = new WordImpl("好4")
        val word3 = new WordImpl("好2")
        val word4 = new WordImpl("好3")


        val wordArray = HashWordArray(List[IWord](word1, word2, word3).toArray)
        wordArray.add(word4);

        val words = wordArray.getWordItems()
        Assert.assertEquals("好1", words(0).getWordName());
        Assert.assertEquals("好4", words(1).getWordName());
        Assert.assertEquals("好2", words(2).getWordName());
        Assert.assertEquals("好3", words(3).getWordName());
    }


}
