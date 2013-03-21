//package websiteschema.mpsegment.pinyin
//
//import org.junit.Assert
//import org.junit.Test
//import websiteschema.mpsegment.core.SegmentResult
//import websiteschema.mpsegment.hmm.ObserveListException
//
//import java.io.IOException
//import java.util.List
//
//class WordToPinyinTest {
//
//    private var classifier : WordToPinyinClassifier = new WordToPinyinClassifier()
//
//    public WordToPinyinTest() throws IOException {
//        var model = new WordToPinyinModel()
//        model.load("websiteschema/mpsegment/wtp.m")
//        classifier.setModel(model)
//    }
//
//    @Test
//    def should_return_pinyin_with_given_Chinese_characters() throws ObserveListException {
//        var result = classifier.classify("我们做到了")
//        Assert.assertEquals("wo", result.get(0))
//        Assert.assertEquals("men", result.get(1))
//        Assert.assertEquals("zuo", result.get(2))
//        Assert.assertEquals("dao", result.get(3))
//        Assert.assertEquals("le", result.get(4))
//    }
//
//    @Test
//    def should_keep_punctuation_in_result() throws ObserveListException {
//        var result = classifier.classify("天涯啊！海角。。哈！")
//        Assert.assertEquals("！", result.get(3))
//        Assert.assertEquals("。", result.get(6))
//        Assert.assertEquals("。", result.get(7))
//        Assert.assertEquals("！", result.get(9))
//    }
//
//    @Test
//    def should_keep_digtal_and_alphabetical_in_result() throws ObserveListException {
//        var segmentResult = new SegmentResult(1)
//        segmentResult.setWords(new Array[String]{"１２日"})
//        classifier.classify(segmentResult)
//        Assert.assertEquals("１２'ri", segmentResult.getPinyin(0))
//    }
//
//    @Test
//    def should_keep_full_sharp_digtal_and_alphabetical_in_result() throws ObserveListException {
//        var result = classifier.classify("AK47很厉害。")
//        Assert.assertEquals("A", result.get(0))
//        Assert.assertEquals("K", result.get(1))
//        Assert.assertEquals("4", result.get(2))
//        Assert.assertEquals("7", result.get(3))
//    }
//
//    @Test
//    def should_recognize_poly_phones_such_as_zhanbu_luobo() throws ObserveListException {
//        var result = classifier.classify("萝卜占卜")
//        Assert.assertEquals("bo", result.get(1))
//        Assert.assertEquals("bu", result.get(3))
//        result = classifier.classify("银行和行走")
//        Assert.assertEquals("hang", result.get(1))
//        Assert.assertEquals("xing", result.get(3))
//    }
//
//}
