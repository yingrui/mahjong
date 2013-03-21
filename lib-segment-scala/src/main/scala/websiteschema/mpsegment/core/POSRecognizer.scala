///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package websiteschema.mpsegment.core
//
//import java.io.IOException
//import java.util.ArrayList
//import java.util.List
//import websiteschema.mpsegment.dict.IWord
//import websiteschema.mpsegment.dict.POSArray
//import websiteschema.mpsegment.graph.IGraph
//import websiteschema.mpsegment.graph.Path
//import websiteschema.mpsegment.hmm.*
//import websiteschema.mpsegment.util.SerializeHandler
//
///**
// *
// * @author twer
// */
//class POSRecognizer extends IPOSRecognizer {
//
//    private var viterbi : Viterbi = null
//    private var graph : IGraph = null
//    private var path : Path = null
//    private var posFreq : Array[Int] = null
//    private var observeList : List[String] = new ArrayList[String]()
//
//    public POSRecognizer() {
//        viterbi = new Viterbi()
//        viterbi.setSortor(new TrieNodeQuickSort())
//        load()
//    }
//
//    private def initViterbiWithEmissionAndObserve() {
//        observeList.clear()
//        viterbi.setE(new Emission())
//        viterbi.setObserveBank(new NodeRepository())
//        for (Int i = 0; i < path.getLength(); i++) {
//            var word = getWordIndexAt(i)
//            initViterbiWith(word)
//        }
//    }
//
//    private def initViterbiWith(word: IWord) {
//        var wordName = word.getWordName()
//        var observe = new Node(wordName)
//        observeList.add(wordName)
//        viterbi.getObserveBank().add(observe)
//        var posArray = word.getPOSArray()
//        var posTable = posArray.getWordPOSTable()
//        for (Int i = 0; i < posTable.length; i++) {
//            var pos = posTable(i)(0)
//            var freq = posTable(i)(1)
//            viterbi.getE().setProb(pos, observe.getIndex(), getEmissionProb(pos, freq))
//        }
//    }
//
//    private def getEmissionProb(pos: Int, freq: Int) : Double = {
//        var tagFreq = getTagFreqs(pos)
//        if (tagFreq > 0) {
//            return (Double) (freq + 1) / (Double) tagFreq
//        } else {
//            return 0.00001D
//        }
//    }
//    
//    private def getTagFreqs(i: Int) : Int = {
//        return posFreq(i)
//    }
//
//    private def getWordIndexAt(index: Int) : IWord = {
//        return graph.getEdgeObject(path.iget(index), path.iget(index + 1))
//    }
//
//    override def findPOS(p: Path, graph: IGraph) : Array[Int] = {
//        this.path = p
//        this.graph = graph
//        initViterbiWithEmissionAndObserve()
//        try {
//            var stateList = viterbi.calculateWithLog(observeList)
//            var posArray = new Int[stateList.size()]
//            var i = 0
//            for (state <- stateList) {
//                posArray[i++] = state.getIndex()
//            }
//            return posArray
//        } catch {
//            throw new RuntimeException("Could not find POS array." + ex.getMessage(), ex)
//        }
//    }
//
//    def save(writeHandler: SerializeHandler) throws IOException {
//        writeHandler.serializeArrayInt(posFreq)
//        viterbi.getTran().save(writeHandler)
//        viterbi.getPi().save(writeHandler)
//        viterbi.getStateBank().save(writeHandler)
//    }
//
//    def load() {
//        posFreq = POSResources.getInstance().getPosFreq()
//        viterbi.setTran(POSResources.getInstance().getTransition())
//        viterbi.setPi(POSResources.getInstance().getPi())
//        viterbi.setStateBank(POSResources.getInstance().getStateBank())
//    }
//}
