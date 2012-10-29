/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.dict;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

/**
 * @author ray
 */
public class HashDictionaryTest {

    private HashDictionary hashDictionary = null;

    public HashDictionaryTest() {
        DictionaryFactory.getInstance().loadDictionary();
        hashDictionary = DictionaryFactory.getInstance().getCoreDictionary();
    }

    @Test
    public void should_Load_Core_Dictionary() {
        IWord[] words = hashDictionary.getWords("乒乓球");
        assert (null != words);
        Assert.assertEquals(words[0].getWordName(), "乒乓球");
        Assert.assertEquals(156, words[0].getOccuredSum());
        Assert.assertEquals(156, words[0].getOccuredCount("N"));
        Assert.assertEquals(words[1].getWordName(), "乒乓");
        for (IWord word : words) {
            System.out.println("词：" + word.getWordName() + "\n" + word.getPOSArray());
        }

        Assert.assertEquals(5943, hashDictionary.getCapacity());
        Iterator<IWord> iterator = hashDictionary.iterator();
        int count = 0;
        while(iterator.hasNext()) {
            count++;
            iterator.next();
        }
        System.out.println(count);
        Assert.assertEquals(78681, count);
    }

    @Test
    public void should_contains_word_() {
        IWord[] words = hashDictionary.getWords("丘吉尔");
        for (IWord word : words) {
            System.out.println("词：" + word.getWordName() + "\n" + word.getPOSArray());
        }
    }
}
