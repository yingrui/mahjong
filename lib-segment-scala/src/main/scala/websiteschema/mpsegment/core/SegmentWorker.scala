//package websiteschema.mpsegment.core
//
//import websiteschema.mpsegment.conf.MPSegmentConfiguration
//import websiteschema.mpsegment.filter.SegmentResultFilter
//
//class SegmentWorker {
//
//    private var unKnownFilter : SegmentResultFilter = null
//    private var maxSegStrLength : Int = null
//    private var mpSegment : MPSegment = null
//    private var recognizePOS : Boolean = true
//
//    public SegmentWorker(MPSegmentConfiguration config) {
//        unKnownFilter = null
//        maxSegStrLength = 400000
//        mpSegment = new MPSegment(config)
//        unKnownFilter = new SegmentResultFilter(config)
//    }
//
//    def setUseDomainDictionary(flag: Boolean) {
//        mpSegment.setUseDomainDictionary(flag)
//    }
//
//    def isUseDomainDictionary() : Boolean = {
//        return mpSegment.isUseDomainDictionary()
//    }
//
//    def segment(sentence: String) : SegmentResult = {
//        SegmentResult result = null
//        if (sentence != null && sentence.length() > 0) {
//            if (sentence.length() > maxSegStrLength) {
//                sentence = sentence.substring(0, maxSegStrLength)
//            }
//            result = mpSegment.segmentMP(sentence, recognizePOS)
//            if (recognizePOS) {
//                unKnownFilter.filter(result)
//            }
//        } else {
//            result = new SegmentResult(0)
//        }
//        return result
//    }
//
//    def isUseContextFreqSegment() : Boolean = {
//        return mpSegment.isUseContextFreqSegment()
//    }
//
//    def setUseContextFreqSegment(useContextFreqSegment: Boolean) {
//        mpSegment.setUseContextFreqSegment(useContextFreqSegment)
//    }
//
//    def isRecognizePOS() : Boolean = {
//        return recognizePOS
//    }
//
//    def setRecognizePOS(recognizePOS: Boolean) {
//        this.recognizePOS = recognizePOS
//    }
//}
