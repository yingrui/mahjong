package websiteschema.mpsegment.pinyin;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.hmm.ObserveListException;

import java.io.IOException;
import java.util.List;

public class WordToPinyinTest {

    private WordToPinyinClassifier classifier = new WordToPinyinClassifier();

    public WordToPinyinTest() throws IOException {
        WordToPinyinModel model = new WordToPinyinModel();
        model.load("wtp.m");
        classifier.setModel(model);
    }

    @Test
    public void should_return_pinyin_with_given_Chinese_characters() throws ObserveListException {
        List<String> result = classifier.classify("我们做到了");
        Assert.assertEquals("wo", result.get(0));
        Assert.assertEquals("men", result.get(1));
        Assert.assertEquals("zuo", result.get(2));
        Assert.assertEquals("dao", result.get(3));
        Assert.assertEquals("le", result.get(4));
    }

    @Test
    public void should_keep_punctuation_in_result() throws ObserveListException {
        List<String> result = classifier.classify("天涯啊！海角。");
        Assert.assertEquals("tian", result.get(0));
        Assert.assertEquals("ya", result.get(1));
        Assert.assertEquals("a", result.get(2));
        Assert.assertEquals("！", result.get(3));
        Assert.assertEquals("hai", result.get(4));
        Assert.assertEquals("jiao", result.get(5));
        Assert.assertEquals("。", result.get(6));
    }

    @Test
    public void should_keep_digtal_and_alphabetical_in_result() throws ObserveListException {
        List<String> result = classifier.classify("AK47很厉害。");
        Assert.assertEquals("A", result.get(0));
        Assert.assertEquals("K", result.get(1));
        Assert.assertEquals("4", result.get(2));
        Assert.assertEquals("7", result.get(3));
        Assert.assertEquals("hen", result.get(4));
        Assert.assertEquals("li", result.get(5));
        Assert.assertEquals("hai", result.get(6));
        Assert.assertEquals("。", result.get(7));
    }

    @Test
    public void should_recognize_poly_phones_such_as_zhanbu_luobo() throws ObserveListException {
        List<String> result = classifier.classify("萝卜占卜");
        Assert.assertEquals("luo", result.get(0));
        Assert.assertEquals("bo", result.get(1));
        Assert.assertEquals("zhan", result.get(2));
        Assert.assertEquals("bu", result.get(3));
    }

}
