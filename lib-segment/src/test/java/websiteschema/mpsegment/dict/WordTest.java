package websiteschema.mpsegment.dict;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.tools.StringWordConverter;

public class WordTest {

    @Test
    public void should_return_correct_occured_sum() {
        String wordStr = "\"测试\" = {domainType:2,POSTable:{N:100,V:20}}";
        IWord word = new StringWordConverter().convert(wordStr);
        Assert.assertEquals(120, word.getOccuredSum());
    }

    @Test
    public void should_return_correct_occured_count() {
        String wordStr = "\"测试\" = {domainType:2,POSTable:{N:100,V:20}}";
        IWord word = new StringWordConverter().convert(wordStr);
        Assert.assertEquals(100, word.getOccuredCount("N"));
        Assert.assertEquals(20, word.getOccuredCount("V"));
    }

    @Test
    public void should_return_correct_occured_count_after_increase_the_count() {
        String wordStr = "\"测试\" = {domainType:2,POSTable:{N:100,V:20}}";
        IWord word = new StringWordConverter().convert(wordStr);
        word.incOccuredCount("N");
        word.incOccuredCount("V");
        Assert.assertEquals(101, word.getOccuredCount("N"));
        Assert.assertEquals(21, word.getOccuredCount("V"));
    }

    @Test
    public void should_set_occured_sum() {
        String wordStr = "\"测试\" = {domainType:2,POSTable:{N:100,V:20}}";
        IWord word = new StringWordConverter().convert(wordStr);
        word.setOccuredSum(150);
        Assert.assertEquals(150, word.getOccuredSum());
        Assert.assertEquals(125, word.getOccuredCount("N"));
        Assert.assertEquals(25, word.getOccuredCount("V"));
    }
}
