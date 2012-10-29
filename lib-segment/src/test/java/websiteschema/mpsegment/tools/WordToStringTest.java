package websiteschema.mpsegment.tools;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.POSUtil;

public class WordToStringTest {

    Mockery mockFactory = new Mockery();
    int[][] posAndFreq = new int[2][2];
    int[][] singlePosAndFreq = new int[1][2];


    public WordToStringTest() {
        posAndFreq[0][0] = POSUtil.POS_N;
        posAndFreq[0][1] = 100;
        posAndFreq[1][0] = POSUtil.POS_V;
        posAndFreq[1][1] = 20;

        singlePosAndFreq[0][0] = POSUtil.POS_N;
        singlePosAndFreq[0][1] = 100;
    }

    @Test
    public void should_convert_word_to_string_with_word_name_and_single_POS() {
        final IWord word = createWord("测试", singlePosAndFreq);
        WordStringConverter converter = new WordStringConverter(word);
        String actual = converter.convertToString();
        System.out.println(actual);
        Assert.assertTrue(actual.startsWith("\"测试\" = {") && actual.endsWith("}"));
        Assert.assertTrue(actual.contains("POSTable:{N:100}"));
        Assert.assertTrue(actual.contains("domainType:0"));
    }

    @Test
    public void should_convert_word_to_string_with_word_name_and_POSs() {
        final IWord word = createWord("测试", posAndFreq);
        WordStringConverter converter = new WordStringConverter(word);
        String actual = converter.convertToString();
        System.out.println(actual);
        Assert.assertTrue(actual.startsWith("\"测试\" = {") && actual.endsWith("}"));
        Assert.assertTrue(actual.contains("POSTable:{N:100,V:20}"));
        Assert.assertTrue(actual.contains("domainType:0"));
    }

    @Test
    public void should_convert_word_to_string_with_special_characters_quote() {
        final IWord word = createWord("\"", posAndFreq);
        WordStringConverter converter = new WordStringConverter(word);
        String actual = converter.convertToString();
        System.out.println(actual);
        Assert.assertTrue(actual.startsWith("\"%22\" = {") && actual.endsWith("}"));
        Assert.assertTrue(actual.contains("POSTable:{N:100,V:20}"));
        Assert.assertTrue(actual.contains("domainType:0"));
    }

    @Test
    public void should_convert_word_to_string_with_special_characters_brackets() {
        final IWord word = createWord("()", posAndFreq);
        WordStringConverter converter = new WordStringConverter(word);
        String actual = converter.convertToString();
        System.out.println(actual);
        Assert.assertTrue(actual.startsWith("\"%28%29\" = {") && actual.endsWith("}"));
        Assert.assertTrue(actual.contains("POSTable:{N:100,V:20}"));
        Assert.assertTrue(actual.contains("domainType:0"));
    }

    @Test
    public void should_convert_word_to_string_with_special_characters_square_braces() {
        final IWord word = createWord("[]", posAndFreq);
        WordStringConverter converter = new WordStringConverter(word);
        String actual = converter.convertToString();
        System.out.println(actual);
        Assert.assertTrue(actual.startsWith("\"%5B%5D\" = {") && actual.endsWith("}"));
        Assert.assertTrue(actual.contains("POSTable:{N:100,V:20}"));
        Assert.assertTrue(actual.contains("domainType:0"));
    }

    @Test
    public void should_convert_word_to_string_with_special_characters_braces() {
        final IWord word = createWord("{}", posAndFreq);
        WordStringConverter converter = new WordStringConverter(word);
        String actual = converter.convertToString();
        System.out.println(actual);
        Assert.assertTrue(actual.startsWith("\"%7B%7D\" = {") && actual.endsWith("}"));
        Assert.assertTrue(actual.contains("POSTable:{N:100,V:20}"));
        Assert.assertTrue(actual.contains("domainType:0"));
    }

    private IWord createWord(final String wordName, final int[][] ret) {
        final IWord word = mockFactory.mock(IWord.class);
        mockFactory.checking(new Expectations() {
            {
                atLeast(1).of(word).getWordName();
                will(returnValue(wordName));
            }

            {
                atLeast(1).of(word).getDomainType();
                will(returnValue(0));
            }

            {
                atLeast(1).of(word).getWordPOSTable();
                will(returnValue(ret));
            }
        });
        return word;
    }


}
