package websiteschema.mpsegment.util;

import junit.framework.Assert;
import org.junit.Test;

public class WordUtilTest {

    @Test
    public void should_return_1_if_input_is_a_numerical_string() {
        Assert.assertEquals(1, WordUtil.isNumerical("123"));
    }
    @Test
    public void should_return_2_if_input_is_not_a_numerical_string() {
        Assert.assertEquals(2, WordUtil.isNumerical("abc123"));
    }
}
