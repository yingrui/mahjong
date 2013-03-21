//package websiteschema.mpsegment.core
//
//import websiteschema.mpsegment.conf.MPSegmentConfiguration
//import websiteschema.mpsegment.hmm.NodeRepository
//import websiteschema.mpsegment.hmm.Pi
//import websiteschema.mpsegment.hmm.Transition
//import websiteschema.mpsegment.util.FileUtil
//import websiteschema.mpsegment.util.SerializeHandler
//
//import java.io.DataInputStream
//import java.io.IOException
//
//class POSResources {
//
//    private static POSResources instance = new POSResources()
//
//    private var posFreq : Array[Int] = null
//    private var transition : Transition = null
//    private var pi : Pi = null
//    private var stateBank : NodeRepository = null
//
//    public static POSResources getInstance() {
//        return instance
//    }
//
//    private POSResources() {
//        initialize()
//    }
//
//    private def initialize() {
//        transition = new Transition()
//        pi = new Pi()
//        stateBank = new NodeRepository()
//
//        var resource = MPSegmentConfiguration.getInstance().getPOSMatrix()
//        try {
//            SerializeHandler readHandler = new SerializeHandler(
//                    new DataInputStream(FileUtil.getResourceAsStream(resource)))
//            load(readHandler)
//        } catch {
//            ex.printStackTrace()
//        }
//    }
//
//    private def load(readHandler: SerializeHandler) throws IOException {
//        posFreq = readHandler.deserializeArrayInt()
//        transition.load(readHandler)
//        pi.load(readHandler)
//        stateBank.load(readHandler)
//    }
//
//    def getPosFreq() : Array[Int] = {
//        return posFreq
//    }
//
//    def getTransition() : Transition = {
//        return transition
//    }
//
//    def getPi() : Pi = {
//        return pi
//    }
//
//    def getStateBank() : NodeRepository = {
//        return stateBank
//    }
//}
