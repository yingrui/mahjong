/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.POSArray;
import websiteschema.mpsegment.graph.IGraph;
import websiteschema.mpsegment.graph.Path;
import websiteschema.mpsegment.hmm.*;
import websiteschema.mpsegment.util.SerializeHandler;

/**
 *
 * @author twer
 */
public class POSRecognizer implements IPOSRecognizer {

    private final Viterbi viterbi;
    private IGraph graph;
    private Path path;
    private int[] posFreq;
    private List<String> observeList = new ArrayList<String>();

    public POSRecognizer() {
        viterbi = new Viterbi();
        viterbi.setSortor(new TrieNodeQuickSort());
        load();
    }

    private void initViterbiWithEmissionAndObserve() {
        observeList.clear();
        viterbi.setE(new Emission());
        viterbi.setObserveBank(new NodeRepository());
        for (int i = 0; i < path.getLength(); i++) {
            IWord word = getWordIndexAt(i);
            initViterbiWith(word);
        }
    }

    private void initViterbiWith(IWord word) {
        String wordName = word.getWordName();
        Node observe = new Node(wordName);
        observeList.add(wordName);
        viterbi.getObserveBank().add(observe);
        POSArray posArray = word.getPOSArray();
        int[][] posTable = posArray.getWordPOSTable();
        for (int i = 0; i < posTable.length; i++) {
            int pos = posTable[i][0];
            int freq = posTable[i][1];
            viterbi.getE().setProb(pos, observe.getIndex(), getEmissionProb(pos, freq));
        }
    }

    private double getEmissionProb(int pos, int freq) {
        int tagFreq = getTagFreqs(pos);
        if (tagFreq > 0) {
            return (double) (freq + 1) / (double) tagFreq;
        } else {
            return 0.00001D;
        }
    }
    
    private int getTagFreqs(int i) {
        return posFreq[i];
    }

    private IWord getWordIndexAt(int index) {
        return graph.getEdgeObject(path.iget(index), path.iget(index + 1));
    }

    @Override
    public int[] findPOS(Path p, IGraph graph) {
        this.path = p;
        this.graph = graph;
        initViterbiWithEmissionAndObserve();
        try {
            List<Node> stateList = viterbi.calculateWithLog(observeList);
            int[] posArray = new int[stateList.size()];
            int i = 0;
            for (Node state : stateList) {
                posArray[i++] = state.getIndex();
            }
            return posArray;
        } catch (ObserveListException ex) {
            throw new RuntimeException("Could not find POS array." + ex.getMessage(), ex);
        }
    }

    public void save(SerializeHandler writeHandler) throws IOException {
        writeHandler.serializeArrayInt(posFreq);
        viterbi.getTran().save(writeHandler);
        viterbi.getPi().save(writeHandler);
        viterbi.getStateBank().save(writeHandler);
    }

    public final void load() {
        posFreq = POSResources.getInstance().getPosFreq();
        viterbi.setTran(POSResources.getInstance().getTransition());
        viterbi.setPi(POSResources.getInstance().getPi());
        viterbi.setStateBank(POSResources.getInstance().getStateBank());
    }
}
