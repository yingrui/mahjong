package websiteschema.mpsegment.lang.en;

import org.junit.Assert;
import org.junit.Test;

public class PorterStemmerTest {

    PorterStemmer porterStemmer = new PorterStemmer();

    @Test
    public void should_stem_word_DOING_to_DO() {
        String actualWord = porterStemmer.stem("greeting");
        Assert.assertEquals("greet", actualWord);
    }

    @Test
    public void should_stem_word_DONE_to_DO() {
        String actualWord = porterStemmer.stem("started");
        Assert.assertEquals("start", actualWord);
    }

    @Test
    public void should_stem_word_likes_to_like() {
        String actualWord = porterStemmer.stem("likes");
        Assert.assertEquals("like", actualWord);
    }
}
