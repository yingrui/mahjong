package me.yingrui.segment.dict

import org.junit.Assert
import org.junit.Test

class POSArrayTest {

    @Test
    def one_Word_Could_has_Multi_POSes() {
        val posArray = new POSArray()
        val posV = POS("V", 1)
        val posT = POS("T", 1)
        val posN = POS("N", 3451)
        posArray.add(posV)
        posArray.add(posT)
        posArray.add(posN)
        Assert.assertEquals(posArray.getOccurredCount("V"), 1)
        Assert.assertEquals(posArray.getOccurredCount("N"), 3451)
        Assert.assertEquals(posArray.getOccurredSum(), 3453)

        val posArray2 = new POSArray()
        posArray2.add(posArray)
        Assert.assertEquals(posArray2.getOccurredCount("V"), 1)
        Assert.assertEquals(posArray2.getOccurredCount("N"), 3451)
        Assert.assertEquals(posArray2.getOccurredSum(), 3453)

        val pa = posArray.getWordPOSTable()
        Assert.assertEquals(pa(0)(1) + pa(1)(1) + pa(2)(1), 3453)
    }
}
