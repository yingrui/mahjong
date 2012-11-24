package websiteschema.mpsegment.pinyin;

import websiteschema.mpsegment.hmm.*;
import websiteschema.mpsegment.util.SerializeHandler;

import java.io.*;
import java.util.Map;

public class WordToPinyinModel {

    private Viterbi viterbi = new Viterbi();
    private NodeRepository stateBank = new NodeRepository();
    private NodeRepository observeBank = new NodeRepository();
    private Trie ngram = new Trie();
    private Pi pi = new Pi();
    private Emission emission = new Emission();

    public boolean knwonObserve(String ch) {
        return observeBank.get(ch) != null;
    }

    public void load(String filename) throws IOException {
        File file = new File(filename);
        InputStream is = null;
        if (!file.exists()) {
            is = this.getClass().getClassLoader().getResourceAsStream("websiteschema/mpsegment/wtp.m");
            load(is);
        } else {
            is = new FileInputStream(file);
            load(is);
        }
    }

    public void load(InputStream is) {
        try {
            SerializeHandler handler = new SerializeHandler(new DataInputStream(is));
            observeBank.load(handler);
            stateBank.load(handler);
            pi.load(handler);
            emission.load(handler);
            ngram.load(handler);
            buildViterbi();
            handler.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void save(String file) throws IOException {
        SerializeHandler handler = new SerializeHandler(new File(file), SerializeHandler.MODE_WRITE_ONLY);
        observeBank.save(handler);
        stateBank.save(handler);
        pi.save(handler);
        emission.save(handler);
        ngram.save(handler);
        handler.close();
    }


    protected Viterbi getViterbi() {
        return viterbi;
    }

    protected Trie getNgram() {
        return ngram;
    }

    protected NodeRepository getStateBank() {
        return stateBank;
    }

    protected NodeRepository getObserveBank() {
        return observeBank;
    }

    protected void buildPi(Map<Integer, Integer> pii) {
        pi = new Pi(pii);
    }

    protected void buildEmission(Map<Integer, Map<Integer, Integer>> emisMatrix) {
        emission = new Emission(emisMatrix);
    }

    protected void buildViterbi() {
        viterbi.setObserveBank(observeBank);
        viterbi.setStateBank(stateBank);
        viterbi.setPi(pi);
        Transition tran = new Transition(ngram, stateBank);
        viterbi.setTran(tran);
        viterbi.setE(emission);
    }
}
