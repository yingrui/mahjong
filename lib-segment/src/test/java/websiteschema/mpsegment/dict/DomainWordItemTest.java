package websiteschema.mpsegment.dict;

import org.junit.Assert;
import org.junit.Test;

public class DomainWordItemTest {

    DomainWordItem word;
    public DomainWordItemTest() {
        word = new DomainWordItem("Test", 0);
        word.setOccuredCount("N", 50);
    }

    @Test
    public void should_return_POS_N_and_Freq() {
        int[][] wordPOSTable = word.getWordPOSTable();
        Assert.assertNotNull(wordPOSTable);
        Assert.assertEquals(1, wordPOSTable.length);
        Assert.assertEquals(POSUtil.POS_N, wordPOSTable[0][0]);
        Assert.assertEquals(50, wordPOSTable[0][1]);
    }

    @Test
    public void should_set_occured_sum() {
        word.setOccuredSum(100);
        int[][] wordPOSTable = word.getWordPOSTable();
        Assert.assertNotNull(wordPOSTable);
        Assert.assertEquals(1, wordPOSTable.length);
        Assert.assertEquals(POSUtil.POS_N, wordPOSTable[0][0]);
        Assert.assertEquals(100, wordPOSTable[0][1]);
    }
}
