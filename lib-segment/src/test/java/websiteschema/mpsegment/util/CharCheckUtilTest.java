package websiteschema.mpsegment.util;

import org.junit.Assert;
import org.junit.Test;

public class CharCheckUtilTest {

    @Test
    public void should_check_word_whether_English() {
        Assert.assertTrue(CharCheckUtil.isEnglish("Hello"));
        Assert.assertTrue(CharCheckUtil.isEnglish("Kates'"));
        Assert.assertTrue(CharCheckUtil.isEnglish("Kate's"));
        Assert.assertFalse(CharCheckUtil.isEnglish("Kate1"));
    }

    @Test
    public void should_check_word_whether_white_space() {
        Assert.assertTrue(CharCheckUtil.isWhiteSpace(" "));
        Assert.assertTrue(CharCheckUtil.isWhiteSpace("   "));
        Assert.assertTrue(CharCheckUtil.isWhiteSpace("\t"));
    }
}
