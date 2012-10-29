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
public class WordArrayTest {


    @Test
    public void should_sort_word_array_automatically() {
        IWord word1 = new WordImpl("好1");
        IWord word2 = new WordImpl("好4");
        IWord word3 = new WordImpl("好2");
        IWord word4 = new WordImpl("好3");

        BinaryWordArray wordArray = new BinaryWordArray(new IWord[]{word1,word2,word3});
        wordArray.add(word4);

        IWord words[] = wordArray.getWordItems();
        Assert.assertEquals("好1", words[0].getWordName());
        Assert.assertEquals("好2", words[1].getWordName());
        Assert.assertEquals("好3", words[2].getWordName());
        Assert.assertEquals("好4", words[3].getWordName());
    }


}
