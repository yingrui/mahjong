package websiteschema.mpsegment.performance;

import junit.framework.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.SegmentEngine;
import websiteschema.mpsegment.core.SegmentWorker;
import websiteschema.mpsegment.tools.SegmentAccuracy;

import java.io.IOException;

public class AccuracyTest {

    @Test
    public void should_be_higher_accuracy_rate_than_0_dot_93_with_segment_minimum() throws IOException {
        SegmentWorker segmentWorker =
                SegmentEngine.getInstance().getSegmentWorker(
                        "segment.xingmingseparate -> true",
                        "segment.min -> true"
                );
        SegmentAccuracy segmentAccuracy = new SegmentAccuracy("PFR-199801-utf-8.txt", segmentWorker);
        segmentAccuracy.checkSegmentAccuracy();
        System.out.println("Accuracy rate of segment is: " + segmentAccuracy.getAccuracyRate());
        System.out.println("There are " + segmentAccuracy.getWrong() + " errors and total expect word is " + segmentAccuracy.getTotalWords() + " when doing accuracy test.");

        System.out.println("There are " + segmentAccuracy.getErrorNewWord() + " errors because of new word.");
        System.out.println("There are " + segmentAccuracy.getErrorNER_NR() + " errors because of name recognition.");
        System.out.println("There are " + segmentAccuracy.getErrorNER_NS() + " errors because of place name recognition.");
        System.out.println("There are " + segmentAccuracy.getErrorContain() + " errors because of contain disambiguate.");
        System.out.println("There are " + segmentAccuracy.getErrorOther() + " other errors");

        System.out.println("Possible " + segmentAccuracy.getPossibleNewWords().size() + " new words, they are:");
//        System.out.println(segmentAccuracy.getPossibleNewWords());
        System.out.println("Those " + segmentAccuracy.getWordsWithContainDisambiguate().size() + " words maybe could delete from dictionary: ");
//        System.out.println(segmentAccuracy.getWordsWithContainDisambiguate());

        Assert.assertTrue(segmentAccuracy.getAccuracyRate() > 0.93671);
        Assert.assertTrue(segmentAccuracy.getErrorNewWord() <= 24024);
        Assert.assertTrue(segmentAccuracy.getErrorNER_NR() <= 4156);
        Assert.assertTrue(segmentAccuracy.getErrorNER_NS() <= 3282);
        Assert.assertTrue(segmentAccuracy.getErrorContain() <= 35909);
        Assert.assertTrue(segmentAccuracy.getErrorOther() <= 3601);
    }

    @Test
    public void should_be_higher_accuracy_rate_than_0_dot_93() throws IOException {
        SegmentWorker segmentWorker = SegmentEngine.getInstance().getSegmentWorker("segment.xingmingseparate -> true");
        SegmentAccuracy segmentAccuracy = new SegmentAccuracy("PFR-199801-utf-8.txt", segmentWorker);
        segmentAccuracy.checkSegmentAccuracy();
        System.out.println("Accuracy rate of segment is: " + segmentAccuracy.getAccuracyRate());
        System.out.println("There are " + segmentAccuracy.getWrong() + " errors and total expect word is " + segmentAccuracy.getTotalWords() + " when doing accuracy test.");

        System.out.println("There are " + segmentAccuracy.getErrorNewWord() + " errors because of new word.");
        System.out.println("There are " + segmentAccuracy.getErrorNER_NR() + " errors because of name recognition.");
        System.out.println("There are " + segmentAccuracy.getErrorNER_NS() + " errors because of place name recognition.");
        System.out.println("There are " + segmentAccuracy.getErrorContain() + " errors because of contain disambiguate.");
        System.out.println("There are " + segmentAccuracy.getErrorOther() + " other errors");

        System.out.println("Possible " + segmentAccuracy.getPossibleNewWords().size() + " new words, they are:");
//        System.out.println(segmentAccuracy.getPossibleNewWords());
        System.out.println("Those " + segmentAccuracy.getWordsWithContainDisambiguate().size() + " words maybe could delete from dictionary: ");
//        System.out.println(segmentAccuracy.getWordsWithContainDisambiguate());

        Assert.assertTrue(segmentAccuracy.getAccuracyRate() > 0.93664);
        Assert.assertTrue(segmentAccuracy.getErrorNewWord() <= 23457);
        Assert.assertTrue(segmentAccuracy.getErrorNER_NR() <= 4155);
        Assert.assertTrue(segmentAccuracy.getErrorNER_NS() <= 3036);
        Assert.assertTrue(segmentAccuracy.getErrorContain() <= 36794);
        Assert.assertTrue(segmentAccuracy.getErrorOther() <= 3603);
    }
}
