package websiteschema.mpsegment.performance

import junit.framework.Assert
import org.junit.{Ignore, Test}
import websiteschema.mpsegment.core.SegmentEngine
import websiteschema.mpsegment.tools.accurary.ErrorAnalyzer
import websiteschema.mpsegment.tools.accurary.SegmentAccuracy
import websiteschema.mpsegment.tools.accurary.SegmentErrorType

@Ignore
class AccuracyTest {

    @Test
    def should_be_higher_than_93_percent_with_segment_minimum() {
        val segmentWorker =
                SegmentEngine().getSegmentWorker(
                        "separate.xingming -> true",
                        "minimize.word -> true"
                )
        val segmentAccuracy = new SegmentAccuracy("PFR-199801-utf-8.txt", segmentWorker)
        segmentAccuracy.checkSegmentAccuracy()
        println("Accuracy rate of segment is: " + segmentAccuracy.getAccuracyRate())
        println("There are " + segmentAccuracy.getWrong() + " errors and total expect word is " + segmentAccuracy.getTotalWords() + " when doing accuracy test.")

        println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord).getErrorOccurTimes() + " errors because of new word.")
        println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.NER_NR).getErrorOccurTimes() + " errors because of name recognition.")
        println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.NER_NS).getErrorOccurTimes() + " errors because of place name recognition.")
        println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate).getErrorOccurTimes() + " errors because of contain disambiguate.")
        println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.Other).getErrorOccurTimes() + " other errors")

        println("Possible " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord).getWords().size + " new words")
        println("Total count: " + getWordsCount(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord))+", they are:")
        println(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord).getWords())
        println("Those " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate).getWords().size + " words maybe could delete from dictionary")
        println("Total count: " + getWordsCount(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate)) + ", they are:")
        println(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate).getWords())

        Assert.assertTrue(segmentAccuracy.getAccuracyRate() > 0.94021 * 0.99)
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord).getErrorOccurTimes() <= 23891 * 1.05)
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.NER_NR).getErrorOccurTimes() <= 4156 * 1.05)
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.NER_NS).getErrorOccurTimes() <= 3282 * 1.05)
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate).getErrorOccurTimes() <= 35317 * 1.05)
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.Other).getErrorOccurTimes() <= 3610 * 1.05)
    }

    @Test
    def should_be_higher_than_93_percent() {
        val segmentWorker = SegmentEngine().getSegmentWorker("separate.xingming -> true")
        val segmentAccuracy = new SegmentAccuracy("PFR-199801-utf-8.txt", segmentWorker)
        segmentAccuracy.checkSegmentAccuracy()
        println("Accuracy rate of segment is: " + segmentAccuracy.getAccuracyRate())
        println("There are " + segmentAccuracy.getWrong() + " errors and total expect word is " + segmentAccuracy.getTotalWords() + " when doing accuracy test.")

        println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord).getErrorOccurTimes() + " errors because of new word.")
        println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.NER_NR).getErrorOccurTimes() + " errors because of name recognition.")
        println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.NER_NS).getErrorOccurTimes() + " errors because of place name recognition.")
        println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate).getErrorOccurTimes() + " errors because of contain disambiguate.")
        println("There are " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.Other).getErrorOccurTimes() + " other errors")

        println("Possible " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord).getWords().size + " new words")
        println("Total count: " + getWordsCount(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord))+", they are:")
        println(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord).getWords())
        println("Those " + segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate).getWords().size + " words maybe could delete from dictionary")
        println("Total count: " + getWordsCount(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate)) + ", they are:")
        println(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate).getWords())

        Assert.assertTrue(segmentAccuracy.getAccuracyRate() > 0.93994 * 0.99)
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.UnknownWord).getErrorOccurTimes() <= 23534 * 1.05)
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.NER_NR).getErrorOccurTimes() <= 4155 * 1.05)
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.NER_NS).getErrorOccurTimes() <= 3036 * 1.05)
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.ContainDisambiguate).getErrorOccurTimes() <= 36202 * 1.05)
        Assert.assertTrue(segmentAccuracy.getErrorAnalyzer(SegmentErrorType.Other).getErrorOccurTimes() <= 3612 * 1.05)
    }

    private def getWordsCount(errorAnalyzer: ErrorAnalyzer) : Int = {
        val words = errorAnalyzer.getWords()
        var count = 0
        for(word <- words.keys) {
            count += words(word)
        }
        return count
    }
}
