package websiteschema.mpsegment

import org.junit.Assert
import org.junit.Test
import websiteschema.mpsegment.dict.DomainWordItem

class WordItemTest {

    @Test
    def should_Create_a_New_Domain_Word_Item() {
        val word = DomainWordItem("ABC", 10001)
        println(word.getWordName() + " " + word.getLog2Freq())
        Assert.assertEquals(word.getOccuredSum(), 0)
        Assert.assertEquals(word.getDomainType(), 10001)
    }
}
