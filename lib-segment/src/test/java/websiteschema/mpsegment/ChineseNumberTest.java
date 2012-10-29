/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.util.NumberUtil;


/**
 * @author ray
 */
public class ChineseNumberTest {

    @Test
    public void should_know_how_to_convert_Chinese_number_to_English() {
        Assert.assertEquals(NumberUtil.chineseToEnglishNumberStr("第一"), "第1");
        Assert.assertEquals((new StringBuilder("digital=")).append(NumberUtil.chineseToEnglishNumberStr("1998.8")).toString(), "digital=");
        Assert.assertEquals(NumberUtil.chineseToEnglishNumberStr("千万分之一.五"), "千万分之1.5");
        Assert.assertEquals(NumberUtil.chineseToEnglishNumberStr("七十亿零陆十五万四千三百二十"), "7000654320");
        assert (NumberUtil.chineseToEnglishNumber("负零点二三五六") + 0.2356D < 0.000001D);
        Assert.assertEquals(NumberUtil.chineseToEnglishNumberStr("贰百叁十肆万"), "2340000");
        Assert.assertEquals(NumberUtil.chineseToEnglishNumberStr("零壹贰叁肆伍陆柒捌玖拾"), "123456799");
        Assert.assertEquals(NumberUtil.chineseToEnglishNumberStr("二百五十四"), "254");
//        Assert.assertEquals(NumberUtil.chineseToEnglishNumberStr("1·5"), "1.5");
    }

    @Test
    public void should_know_how_to_convert_English_number_to_Chinese() {
        Assert.assertEquals(NumberUtil.toChineseNumber("111111123,456.12"), "一千一百一十一亿一千一百一十二万三千四百五十六点一二");
        Assert.assertEquals(NumberUtil.toChineseNumber(-10309300932L), "负一百零三亿零九百三十万零九百三十二");
    }
}
