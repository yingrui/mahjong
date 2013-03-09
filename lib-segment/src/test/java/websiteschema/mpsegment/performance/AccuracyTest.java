package websiteschema.mpsegment.performance;

import junit.framework.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.SegmentEngine;
import websiteschema.mpsegment.core.SegmentWorker;
import websiteschema.mpsegment.tools.accurary.ErrorAnalyzer;
import websiteschema.mpsegment.tools.accurary.SegmentAccuracy;
import websiteschema.mpsegment.tools.accurary.SegmentErrorType;

import java.io.IOException;
import java.util.Map;

public class AccuracyTest {

    @Test
    public void should_be_higher_than_93_percent_with_segment_minimum() throws IOException {
        SegmentWorker segmentWorker =
                SegmentEngine.getInstance().getSegmentWorker(
                        "separate.xingming -> true",
                        "minimize.word -> true"
                );
        SegmentAccuracy segmentAccuracy = new SegmentAccuracy("PFR-199801-utf-8.txt", segmentWorker);
        segmentAccuracy.checkSegmentAccuracy();
        System.out.println("Accuracy rate of segment is: " + segmentAccuracy.getAccuracyRate());
        System.out.println("There are " + segmentAccuracy.getWrong() + " errors and total expect word is " + segmentAccuracy.getTotalWords() + " when doing accuracy test.");

        System.out.println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord).getErrorOccurTimes() + " errors because of new word.");
        System.out.println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.NER_NR).getErrorOccurTimes() + " errors because of name recognition.");
        System.out.println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.NER_NS).getErrorOccurTimes() + " errors because of place name recognition.");
        System.out.println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate).getErrorOccurTimes() + " errors because of contain disambiguate.");
        System.out.println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.Other).getErrorOccurTimes() + " other errors");

        System.out.println("Possible " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord).getWords().size() + " new words");
        System.out.println("Total count: " + getWordsCount(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord))+", they are:");
        System.out.println(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord).getWords());
        System.out.println("Those " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate).getWords().size() + " words maybe could delete from dictionary");
        System.out.println("Total count: " + getWordsCount(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate)) + ", they are:");
        System.out.println(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate).getWords());

        Assert.assertTrue(segmentAccuracy.getAccuracyRate() > 0.94021 * 0.99);
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord).getErrorOccurTimes() <= 23891 * 1.05);
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.NER_NR).getErrorOccurTimes() <= 4156 * 1.05);
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.NER_NS).getErrorOccurTimes() <= 3282 * 1.05);
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate).getErrorOccurTimes() <= 35317 * 1.05);
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.Other).getErrorOccurTimes() <= 3610 * 1.05);
    }

    @Test
    public void should_be_higher_than_93_percent() throws IOException {
        SegmentWorker segmentWorker = SegmentEngine.getInstance().getSegmentWorker("separate.xingming -> true");
        SegmentAccuracy segmentAccuracy = new SegmentAccuracy("PFR-199801-utf-8.txt", segmentWorker);
        segmentAccuracy.checkSegmentAccuracy();
        System.out.println("Accuracy rate of segment is: " + segmentAccuracy.getAccuracyRate());
        System.out.println("There are " + segmentAccuracy.getWrong() + " errors and total expect word is " + segmentAccuracy.getTotalWords() + " when doing accuracy test.");

        System.out.println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord).getErrorOccurTimes() + " errors because of new word.");
        System.out.println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.NER_NR).getErrorOccurTimes() + " errors because of name recognition.");
        System.out.println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.NER_NS).getErrorOccurTimes() + " errors because of place name recognition.");
        System.out.println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate).getErrorOccurTimes() + " errors because of contain disambiguate.");
        System.out.println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.Other).getErrorOccurTimes() + " other errors");

        System.out.println("Possible " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord).getWords().size() + " new words");
        System.out.println("Total count: " + getWordsCount(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord))+", they are:");
        System.out.println(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord).getWords());
        System.out.println("Those " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate).getWords().size() + " words maybe could delete from dictionary");
        System.out.println("Total count: " + getWordsCount(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate)) + ", they are:");
        System.out.println(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate).getWords());

        Assert.assertTrue(segmentAccuracy.getAccuracyRate() > 0.93994 * 0.99);
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord).getErrorOccurTimes() <= 23534 * 1.05);
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.NER_NR).getErrorOccurTimes() <= 4155 * 1.05);
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.NER_NS).getErrorOccurTimes() <= 3036 * 1.05);
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate).getErrorOccurTimes() <= 36202 * 1.05);
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.Other).getErrorOccurTimes() <= 3612 * 1.05);
    }

    private int getWordsCount(ErrorAnalyzer errorAnalyzer) {
        Map<String,Integer> words = errorAnalyzer.getWords();
        int count = 0;
        for(String word : words.keySet()) {
            count += words.get(word);
        }
        return count;
    }
}
