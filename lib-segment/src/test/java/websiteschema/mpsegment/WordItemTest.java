/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.dict.DomainWordItem;

/**
 * @author ray
 */
public class WordItemTest {

    @Test
    public void should_Create_a_New_Domain_Word_Item() {
        DomainWordItem word = new DomainWordItem("ABC", 10001);
        System.out.println(word.getWordName() + " " + word.getLog2Freq());
        Assert.assertEquals(word.getOccuredSum(), 0);
        Assert.assertEquals(word.getDomainType(), 10001);
    }
}
