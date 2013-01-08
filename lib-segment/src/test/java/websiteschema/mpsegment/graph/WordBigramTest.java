package websiteschema.mpsegment.graph;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class WordBigramTest {

    @Test
    public void should_save_and_load_model() throws IOException {
        WordBigram wordBigram = new WordBigram();
        wordBigram.train("src/test/resources/PFR-199801-utf-8.txt");
        wordBigram.build();
        double prob = wordBigram.getProbability("好", "地");
        wordBigram.save("test.dat");

        WordBigram bigram = new WordBigram();
        bigram.load("test.dat");
        double prob1 = bigram.getProbability("好", "地");
        System.out.println("prob: " + prob + " prob1: " + prob1);
        Assert.assertTrue(prob - prob1 < 0.0000001D);

        double prob2 = bigram.getProbability("non", "exists");
        Assert.assertTrue(prob2 < 0.00000000000001D);
    }
}
