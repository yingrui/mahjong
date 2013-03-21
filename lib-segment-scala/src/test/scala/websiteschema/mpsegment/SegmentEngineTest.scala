//package websiteschema.mpsegment;
//
//import junit.framework.Assert;
//import org.junit.Test;
//import websiteschema.mpsegment.core.SegmentEngine;
//import websiteschema.mpsegment.core.SegmentWorker;
//
//class SegmentEngineTest {
//
//    @Test
//    def should_return_reusable_segment_worker_in_same_thread() {
//        var worker1 = SegmentEngine.getInstance().getReusableSegmentWorker()
//        var worker2 = SegmentEngine.getInstance().getReusableSegmentWorker()
//        Assert.assertEquals(worker1, worker2);
//        Assert.assertTrue(worker1.equals(worker2));
//    }
//
//    @Test
//    def should_return_new_segment_worker() {
//        var worker1 = SegmentEngine.getInstance().getSegmentWorker()
//        var worker2 = SegmentEngine.getInstance().getSegmentWorker()
//        Assert.assertTrue(!worker1.equals(worker2));
//    }
//}
