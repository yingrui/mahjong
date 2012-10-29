/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.dict.POS;
import websiteschema.mpsegment.dict.POSArray;

/**
 * @author ray
 */
public class POSArrayTest {

    @Test
    public void one_Word_Could_has_Multi_POSes() {
        POSArray posArray = new POSArray();
        POS posV = new POS("V", 1);
        POS posT = new POS("T", 1);
        POS posN = new POS("N", 3451);
        posArray.add(posV);
        posArray.add(posT);
        posArray.add(posN);
        Assert.assertEquals(posArray.getOccurredCount("V"), 1);
        Assert.assertEquals(posArray.getOccurredCount("N"), 3451);
        Assert.assertEquals(posArray.getOccurredSum(), 3453);

        POSArray posArray2 = new POSArray();
        posArray2.add(posArray);
        Assert.assertEquals(posArray2.getOccurredCount("V"), 1);
        Assert.assertEquals(posArray2.getOccurredCount("N"), 3451);
        Assert.assertEquals(posArray2.getOccurredSum(), 3453);

        int pa[][] = posArray.getWordPOSTable();
        Assert.assertEquals(pa[0][1] + pa[1][1] + pa[2][1], 3453);
    }
}
