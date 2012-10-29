package websiteschema.mpsegment;

import junit.framework.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.SegmentEngine;
import websiteschema.mpsegment.core.SegmentWorker;

public class SegmentEngineTest {

    @Test
    public void should_return_reusable_segment_worker_in_same_thread() {
        SegmentWorker worker1 = SegmentEngine.getInstance().getReusableSegmentWorker();
        SegmentWorker worker2 = SegmentEngine.getInstance().getReusableSegmentWorker();
        Assert.assertEquals(worker1, worker2);
        Assert.assertTrue(worker1.equals(worker2));
    }

    @Test
    public void should_return_new_segment_worker() {
        SegmentWorker worker1 = SegmentEngine.getInstance().getSegmentWorker();
        SegmentWorker worker2 = SegmentEngine.getInstance().getSegmentWorker();
        Assert.assertTrue(!worker1.equals(worker2));
    }
}
