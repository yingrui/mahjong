/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.dict.ChNameDictionary;

/**
 * @author ray
 */
public class ChineseNameDictionaryTest {

    @Test
    public void should_load_Chinese_name_dictionary_correctly() {
        ChNameDictionary dict = new ChNameDictionary();
        dict.loadNameDict("ChName.dict");
        System.out.println(dict.toText());
        dict.outSummary();
        testChineseName(dict, "张学友");
    }

    private void testChineseName(ChNameDictionary dict, String name) {
        System.out.print(name + " ");
        if (dict.getFuXing().containsKey(name.substring(0, 2))) {
            Assert.fail();
        }
        String xing;
        String ming1;
        String ming2 = "";
        xing = name.substring(0, 1);
        ming1 = name.substring(1, 2);
        ming2 = name.substring(2, 3);
        Assert.assertEquals((new StringBuilder()).append(dict.computeLgLP3(xing, ming1, ming2)).toString(), "2.760293536");
        Assert.assertEquals((new StringBuilder()).append(dict.computeLgLP2(ming1, ming2)).toString(), "0.285352");
        System.out.println((new StringBuilder(String.valueOf(dict.computeLgLP3(xing, ming1, ming2)))).append("  ").append(dict.computeLgLP2(ming1, ming2)).toString());
    }

    @Test
    public void should_save_Chinese_name_dictionary_correctly() {
        ChNameDictionary dict = new ChNameDictionary();
        dict.loadNameDict("ChName.dict");
        dict.saveNameDict("ChName2.dict");
        dict = new ChNameDictionary();
        dict.loadNameDict("ChName2.dict");
        testChineseName(dict, "张学友");
        File f = new File("ChName2.dict");
        f.delete();
    }
}
