package websiteschema.mpsegment.lang.en

import org.junit.Assert
import org.junit.Test

class PorterStemmerTest {

    var porterStemmer = new PorterStemmer()

    @Test
    def should_stem_word_DOING_to_DO() {
        val actualWord = porterStemmer.stem("greeting")
        Assert.assertEquals("greet", actualWord)
    }

    @Test
    def should_stem_word_DONE_to_DO() {
        val actualWord = porterStemmer.stem("started")
        Assert.assertEquals("start", actualWord)
    }

    @Test
    def should_stem_word_likes_to_like() {
        val actualWord = porterStemmer.stem("likes")
        Assert.assertEquals("like", actualWord)
    }
}
