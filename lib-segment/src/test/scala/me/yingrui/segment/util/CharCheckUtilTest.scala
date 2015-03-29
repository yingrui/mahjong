package me.yingrui.segment.util

import org.junit.Assert
import org.junit.Test

class CharCheckUtilTest {

    @Test
    def should_check_word_whether_English() {
        Assert.assertTrue(CharCheckUtil.isEnglish("Hello"))
        Assert.assertTrue(CharCheckUtil.isEnglish("Kates'"))
        Assert.assertTrue(CharCheckUtil.isEnglish("Kate's"))
        Assert.assertFalse(CharCheckUtil.isEnglish("Kate1"))
    }

    @Test
    def should_check_word_whether_white_space() {
        Assert.assertTrue(CharCheckUtil.isWhiteSpace(" "))
        Assert.assertTrue(CharCheckUtil.isWhiteSpace("   "))
        Assert.assertTrue(CharCheckUtil.isWhiteSpace("\t"))
    }

    @Test
    def should_return_true_when_all_word_is_chinese_word() {
        Assert.assertTrue(CharCheckUtil.isChinese("张三和李四"))
    }

    @Test
    def should_return_false_when_contains_digital_or_alphabetical() {
        Assert.assertFalse(CharCheckUtil.isChinese("张三and李四"))
        Assert.assertFalse(CharCheckUtil.isChinese("张3和李4"))
    }
}
