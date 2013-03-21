package websiteschema.mpsegment.lang.en

import org.junit.Assert
import org.junit.Test

class PorterStemmerTest {

    var porterStemmer = new PorterStemmer()

    @Test
    def should_stem_word_DOING_to_DO() {
        var actualWord = porterStemmer.stem("greeting")
        Assert.assertEquals("greet", actualWord)
    }

    @Test
    def should_stem_word_DONE_to_DO() {
        var actualWord = porterStemmer.stem("started")
        Assert.assertEquals("start", actualWord)
    }
}
