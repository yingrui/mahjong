/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment;

import websiteschema.mpsegment.dict.POSUtil;
import websiteschema.mpsegment.core.SegmentEngine;
import websiteschema.mpsegment.core.SegmentWorker;
import websiteschema.mpsegment.core.SegmentResult;
import junit.framework.TestCase;

/**
 * @author taskmgr
 */
public class ExtendPOSInDomainDictionaryTest extends TestCase {

    public void testPOS() {
        try {
            String str = "我的同学叫高峰,高峰同志,高峰经理,科学高峰";
            SegmentWorker worker = SegmentEngine.getInstance().getSegmentWorker();
            SegmentResult words = worker.segment(str);
            for (int i = 0; i < words.length(); i++) {
                System.out.println(words.getWord(i) + " - " + POSUtil.getPOSString(words.getPOS(i)) + " - " + words.getConcept(i));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            assert (false);
        }
    }
}
