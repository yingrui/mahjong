package websiteschema.mpsegment.pinyin

import websiteschema.mpsegment.hmm._
import websiteschema.mpsegment.util.SerializeHandler

import collection.mutable.Map
import java.io.{DataInputStream, InputStream, FileInputStream, File}

class WordToPinyinModel {

    private val viterbi = new Viterbi()
    private val stateBank = new NodeRepository()
    private val observeBank = new NodeRepository()
    private val ngram = new Trie()
    private var pi = Pi()
    private var emission : Emission = new Emission()

    def knwonObserve(ch: String) : Boolean = {
        return observeBank.get(ch) != null
    }

    def load(filename: String) {
        var file = new File(filename)
        var is: InputStream = null
        if (!file.exists()) {
            is = getClass().getClassLoader().getResourceAsStream("websiteschema/mpsegment/wtp.m")
            load(is)
        } else {
            is = new FileInputStream(file)
            load(is)
        }
    }

    def load(is: InputStream) {
        try {
            val handler = SerializeHandler(new DataInputStream(is))
            observeBank.load(handler)
            stateBank.load(handler)
            println(stateBank)
            pi.load(handler)
            emission.load(handler)
            ngram.load(handler)
            buildViterbi()
            handler.close()
        } catch {
          case ex: Throwable =>
            ex.printStackTrace()
        }
    }

    def save(file: String) {
        val handler = SerializeHandler(new File(file), SerializeHandler.MODE_WRITE_ONLY)
        observeBank.save(handler)
        stateBank.save(handler)
        pi.save(handler)
        emission.save(handler)
        ngram.save(handler)
        handler.close()
    }


    def getViterbi() : Viterbi = {
        return viterbi
    }

    def getNgram() : Trie = {
        return ngram
    }

    def getStateBank() : NodeRepository = {
        return stateBank
    }

    def getObserveBank() : NodeRepository = {
        return observeBank
    }

    def buildPi(pii: Map[Int,Int]) {
        pi = Pi(pii)
    }

    def buildEmission(emisMatrix: Map[Int, Map[Int,Int]]) {
        emission = Emission(emisMatrix)
    }

    def buildViterbi() {
        viterbi.setObserveBank(observeBank)
        viterbi.setStateBank(stateBank)
        viterbi.setPi(pi)
        var tran = Transition(ngram, stateBank)
        viterbi.setTran(tran)
        viterbi.setE(emission)
    }
}
