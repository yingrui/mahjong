package websiteschema.mpsegment.core

import websiteschema.mpsegment.conf.MPSegmentConfiguration
import websiteschema.mpsegment.hmm.NodeRepository
import websiteschema.mpsegment.hmm.Pi
import websiteschema.mpsegment.hmm.Transition
import websiteschema.mpsegment.util.FileUtil
import websiteschema.mpsegment.util.SerializeHandler

import java.io.DataInputStream

class POSResources {

  private var posFreq: Array[Int] = null
  private var transition: Transition = null
  private var pi: Pi = null
  private var stateBank: NodeRepository = null

  initialize()

  private def initialize() {
    transition = Transition()
    pi = Pi()
    stateBank = new NodeRepository()

    val resource = MPSegmentConfiguration().getPOSMatrix()
    try {
      val readHandler = SerializeHandler(new DataInputStream(FileUtil.getResourceAsStream(resource)))
      load(readHandler)
    } catch {
      case ex: Throwable =>
        ex.printStackTrace()
    }
  }

  private def load(readHandler: SerializeHandler) {
    posFreq = readHandler.deserializeArrayInt()
    transition.load(readHandler)
    pi.load(readHandler)
    stateBank.load(readHandler)
  }

  def getPosFreq(): Array[Int] = {
    return posFreq
  }

  def getTransition(): Transition = {
    return transition
  }

  def getPi(): Pi = {
    return pi
  }

  def getStateBank(): NodeRepository = {
    return stateBank
  }
}

object POSResources {
  val instance = new POSResources()

  def apply() = instance
}
