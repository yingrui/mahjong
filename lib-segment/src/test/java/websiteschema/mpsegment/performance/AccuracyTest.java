package websiteschema.mpsegment.performance;

import junit.framework.Assert;
import org.junit.Test;
import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.tools.SegmentAccuracy;

import java.io.IOException;

public class AccuracyTest {

    @Test
    public void should_be_higher_accuracy_rate_than_0_dot_93_with_segment_minimum() throws IOException {
        SegmentAccuracy segmentAccuracy = new SegmentAccuracy("PFR-199801-utf-8.txt");
        boolean segmentMin = MPSegmentConfiguration.getINSTANCE().isSegmentMin();
        MPSegmentConfiguration.getINSTANCE().setSegmentMin(true);
        segmentAccuracy.checkSegmentAccuracy();
        MPSegmentConfiguration.getINSTANCE().setSegmentMin(segmentMin);
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

        Assert.assertTrue(segmentAccuracy.getAccuracyRate() > 0.93812);
        Assert.assertTrue(segmentAccuracy.getErrorNewWord() <= 24458);
        Assert.assertTrue(segmentAccuracy.getErrorNER_NR() <= 4185);
        Assert.assertTrue(segmentAccuracy.getErrorNER_NS() <= 3284);
        Assert.assertTrue(segmentAccuracy.getErrorContain() <= 34069);
        Assert.assertTrue(segmentAccuracy.getErrorOther() <= 3426);
    }
    @Test
    public void should_be_higher_accuracy_rate_than_0_dot_93() throws IOException {
        SegmentAccuracy segmentAccuracy = new SegmentAccuracy("PFR-199801-utf-8.txt");
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

        Assert.assertTrue(segmentAccuracy.getAccuracyRate() > 0.93784);
        Assert.assertTrue(segmentAccuracy.getErrorNewWord() <= 24063);
        Assert.assertTrue(segmentAccuracy.getErrorNER_NR() <= 4184);
        Assert.assertTrue(segmentAccuracy.getErrorNER_NS() <= 3057);
        Assert.assertTrue(segmentAccuracy.getErrorContain() <= 34952);
        Assert.assertTrue(segmentAccuracy.getErrorOther() <= 3427);
    }
}
