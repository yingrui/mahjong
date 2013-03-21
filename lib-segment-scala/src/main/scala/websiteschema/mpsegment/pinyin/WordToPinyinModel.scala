//package websiteschema.mpsegment.pinyin;
//
//import websiteschema.mpsegment.hmm.*;
//import websiteschema.mpsegment.util.SerializeHandler;
//
//import java.io.*;
//import java.util.Map;
//
//class WordToPinyinModel {
//
//    private var viterbi : Viterbi = new Viterbi();
//    private var stateBank : NodeRepository = new NodeRepository();
//    private var observeBank : NodeRepository = new NodeRepository();
//    private var ngram : Trie = new Trie();
//    private var pi : Pi = new Pi();
//    private var emission : Emission = new Emission();
//
//    def knwonObserve(ch: String) : Boolean = {
//        return observeBank.get(ch) != null;
//    }
//
//    def load(filename: String) throws IOException {
//        var file = new File(filename)
//        var is = null
//        if (!file.exists()) {
//            is = this.getClass().getClassLoader().getResourceAsStream("websiteschema/mpsegment/wtp.m");
//            load(is);
//        } else {
//            is = new FileInputStream(file);
//            load(is);
//        }
//    }
//
//    def load(is: InputStream) {
//        try {
//            var handler = new SerializeHandler(new DataInputStream(is))
//            observeBank.load(handler);
//            stateBank.load(handler);
//            pi.load(handler);
//            emission.load(handler);
//            ngram.load(handler);
//            buildViterbi();
//            handler.close();
//        } catch {
//            ex.printStackTrace();
//        }
//    }
//
//    def save(file: String) throws IOException {
//        var handler = new SerializeHandler(new File(file), SerializeHandler.MODE_WRITE_ONLY)
//        observeBank.save(handler);
//        stateBank.save(handler);
//        pi.save(handler);
//        emission.save(handler);
//        ngram.save(handler);
//        handler.close();
//    }
//
//
//    def getViterbi() : Viterbi = {
//        return viterbi;
//    }
//
//    def getNgram() : Trie = {
//        return ngram;
//    }
//
//    def getStateBank() : NodeRepository = {
//        return stateBank;
//    }
//
//    def getObserveBank() : NodeRepository = {
//        return observeBank;
//    }
//
//    def buildPi(pii: Map[Int,Int]) {
//        pi = new Pi(pii);
//    }
//
//    def buildEmission(Map<Int, Map[Int,Int]> emisMatrix) {
//        emission = new Emission(emisMatrix);
//    }
//
//    def buildViterbi() {
//        viterbi.setObserveBank(observeBank);
//        viterbi.setStateBank(stateBank);
//        viterbi.setPi(pi);
//        var tran = new Transition(ngram, stateBank)
//        viterbi.setTran(tran);
//        viterbi.setE(emission);
//    }
//}
