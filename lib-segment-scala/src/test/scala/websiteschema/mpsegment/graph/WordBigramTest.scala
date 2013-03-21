//package websiteschema.mpsegment.graph
//
//import org.junit.Assert
//import org.junit.Test
//import websiteschema.mpsegment.tools.WordBigramBuilder
//
//import java.io.IOException
//
//class WordBigramTest {
//
//    @Test
//    def should_save_and_load_model() throws IOException {
//        var builder = new WordBigramBuilder()
//        builder.train("src/test/resources/PFR-199801-utf-8.txt")
//        builder.build()
//        builder.save("test.dat")
//        var wordBigram = new WordBigram(builder.getTrie(), builder.getNodeRepository())
//        var prob = wordBigram.getProbability("好", "地")
//
//        var bigram = new WordBigram()
//        bigram.load("test.dat")
//        var prob1 = bigram.getProbability("好", "地")
//        Assert.assertTrue(prob1 - prob < 0.0000001D)
//
//        var prob2 = bigram.getProbability("non", "exists")
//        Assert.assertTrue(prob2 < 0.00000000000001D)
//    }
//}
