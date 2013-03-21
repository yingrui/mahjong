///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package websiteschema.mpsegment
//
//import websiteschema.mpsegment.dict.POSUtil
//import websiteschema.mpsegment.core.SegmentEngine
//import websiteschema.mpsegment.core.SegmentWorker
//import websiteschema.mpsegment.core.SegmentResult
//import junit.framework.TestCase
//
///**
// * @author taskmgr
// */
//class ExtendPOSInDomainDictionaryTest extends TestCase {
//
//    def testPOS() {
//        try {
//            var str = "我的同学叫高峰,高峰同志,高峰经理,科学高峰"
//            var worker = SegmentEngine.getInstance().getSegmentWorker()
//            var words = worker.segment(str)
//            for (Int i = 0; i < words.length(); i++) {
//                println(words.getWord(i) + " - " + POSUtil.getPOSString(words.getPOS(i)) + " - " + words.getDomainType(i))
//            }
//        } catch {
//            ex.printStackTrace()
//            assert (false)
//        }
//    }
//}
