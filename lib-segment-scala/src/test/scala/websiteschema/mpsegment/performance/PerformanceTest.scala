//package websiteschema.mpsegment.performance;
//
//import junit.framework.Assert;
//import org.junit.Test;
//import websiteschema.mpsegment.core.SegmentEngine;
//import websiteschema.mpsegment.core.SegmentResult;
//import websiteschema.mpsegment.core.SegmentWorker;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//class PerformanceTest {
//
//    @Test
//    def should_segment_Sophies_World_within_1_seconds() throws IOException {
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(
//                        getClass().getClassLoader().getResourceAsStream("Sophie's_World.txt"), "UTF-8"));
//        var segmentWorker = SegmentEngine.getInstance().getSegmentWorker()
//        segmentWorker.setRecognizePOS(false);
//        segmentWorker.segment("世界您好！");
//        var line: String = null
//        var beginTime = System.currentTimeMillis()
//        var total = 0
//        do {
//            line = reader.readLine();
//            var result = segmentWorker.segment(line)
//            total += result.length();
//        } while (null != line);
//        var endTime = System.currentTimeMillis()
//        var milSeconds = endTime - beginTime
//        println("should_segment_Sophies_World_within_1_seconds");
//        println("    Spend total " + milSeconds + " ms.");
//        println("    Segment words " + total + ", the velocity is " + (total * 1000 / milSeconds) + " num/sec.");
//        Assert.assertTrue(milSeconds < 1000);
//    }
//
//    @Test
//    def should_segment_Sophies_World_with_POS_and_without_Domain_Dictionary_within_2_seconds() throws IOException {
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(
//                        getClass().getClassLoader().getResourceAsStream("Sophie's_World.txt"), "UTF-8"));
//        var segmentWorker = SegmentEngine.getInstance().getSegmentWorker()
//        segmentWorker.setRecognizePOS(true);
//        segmentWorker.setUseDomainDictionary(false);
//        segmentWorker.segment("世界您好！");
//        var line: String = null
//        var beginTime = System.currentTimeMillis()
//        var total = 0
//        do {
//            line = reader.readLine();
//            var result = segmentWorker.segment(line)
//            total += result.length();
//        } while (null != line);
//        segmentWorker.setUseDomainDictionary(true);
//        var endTime = System.currentTimeMillis()
//        var milSeconds = endTime - beginTime
//        println("should_segment_Sophies_World_with_POS_and_without_Domain_Dictionary_within_2_seconds");
//        println("    Spend total " + milSeconds + " ms.");
//        println("    Segment words " + total + " with POS recognition and without Domain dictionary, the velocity is " + (total * 1000 / milSeconds) + " num/sec.");
//        Assert.assertTrue(milSeconds < 2000);
//    }
//
//    @Test
//    def should_segment_Sophies_World_with_POS_within_2_seconds() throws IOException {
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(
//                        getClass().getClassLoader().getResourceAsStream("Sophie's_World.txt"), "UTF-8"));
//        var segmentWorker = SegmentEngine.getInstance().getSegmentWorker()
//        segmentWorker.setRecognizePOS(true);
//        segmentWorker.segment("世界您好！");
//        var line: String = null
//        var beginTime = System.currentTimeMillis()
//        var total = 0
//        do {
//            line = reader.readLine();
//            var result = segmentWorker.segment(line)
//            total += result.length();
//        } while (null != line);
//        var endTime = System.currentTimeMillis()
//        var milSeconds = endTime - beginTime
//        println("should_segment_Sophies_World_with_POS_within_2_seconds");
//        println("    Spend total " + milSeconds + " ms.");
//        println("    Segment words " + total + " with POS recognition, the velocity is " + (total * 1000 / milSeconds) + " num/sec.");
//        Assert.assertTrue(milSeconds < 2000);
//    }
//
//    @Test
//    def should_segment_Sophies_World_with_POS_and_Context_within_2_seconds() throws IOException {
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(
//                        getClass().getClassLoader().getResourceAsStream("Sophie's_World.txt"), "UTF-8"));
//        var segmentWorker = SegmentEngine.getInstance().getSegmentWorker()
//        segmentWorker.setRecognizePOS(true);
//        segmentWorker.setUseContextFreqSegment(true);
//        segmentWorker.segment("世界您好！");
//        var line: String = null
//        var beginTime = System.currentTimeMillis()
//        var total = 0
//        do {
//            line = reader.readLine();
//            var result = segmentWorker.segment(line)
//            total += result.length();
//        } while (null != line);
//        segmentWorker.setUseContextFreqSegment(false);
//        var endTime = System.currentTimeMillis()
//        var milSeconds = endTime - beginTime
//        println("should_segment_Sophies_World_with_POS_and_Context_within_2_seconds");
//        println("    Spend total " + milSeconds + " ms.");
//        println("    Segment words " + total + " with POS recognition and context, the velocity is " + (total * 1000 / milSeconds) + " num/sec.");
//        Assert.assertTrue(milSeconds < 2000);
//    }
//
//    @Test
//    def should_spend_memory_within_25_MB() throws IOException, InterruptedException {
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(
//                        getClass().getClassLoader().getResourceAsStream("Sophie's_World.txt"), "UTF-8"));
//        var segmentWorker = SegmentEngine.getInstance().getSegmentWorker()
//        segmentWorker.setRecognizePOS(true);
//        segmentWorker.setUseContextFreqSegment(true);
//        segmentWorker.segment("世界您好！");
//        var line: String = null
//        do {
//            line = reader.readLine();
//            segmentWorker.segment(line);
//        } while (null != line);
//        segmentWorker.setUseContextFreqSegment(false);
//        Runtime.getRuntime().gc();
//        Thread.sleep(2000);
//        var totalMemory = Runtime.getRuntime().totalMemory()
//        var freeMemory = Runtime.getRuntime().freeMemory()
//        var memorySize = (totalMemory - freeMemory) / 1024 / 1024
//        println("should_spend_memory_within_25_MB");
//        println("    Current application has taken " + memorySize + "MB memory size.");
//        Assert.assertTrue(memorySize < 25);
//    }
//
//    public static void main(Array[String] args) throws IOException {
//        println("In case you want to run for performance profile.");
//        var segmentWorker = SegmentEngine.getInstance().getSegmentWorker()
//        segmentWorker.setRecognizePOS(true);
//        segmentWorker.segment("世界您好！");
//        while (true) {
//            segmentWorker.setUseContextFreqSegment(true);
//            BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(
//                            PerformanceTest.class.getClassLoader().getResourceAsStream("Sophie's_World.txt"), "UTF-8"));
//
//            var line: String = null
//            do {
//                line = reader.readLine();
//                segmentWorker.segment(line);
//            } while (null != line);
//            segmentWorker.setUseContextFreqSegment(false);
//        }
//    }
//}
