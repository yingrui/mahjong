package me.yingrui.segment.core

import java.io.DataInputStream

import me.yingrui.segment.conf.SegmentConfiguration
import me.yingrui.segment.hmm._
import me.yingrui.segment.util.{FileUtil, SerializeHandler}

object POSResources {

  private var posFreq: Array[Int] = null
  private var transition: Transition = null
  private var pi: Pi = null
  private val root = new Trie()
  private var stateBank: NodeRepository = null

  initialize()

  private def initialize() {
    transition = Transition()
    pi = Pi()
    stateBank = new NodeRepository()

    val resource = SegmentConfiguration().getPOSMatrix()
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
    loadTransition(readHandler)
    pi.load(readHandler)
  }

  private def loadTransition(readHandler: SerializeHandler) {
    root.load(readHandler)
    stateBank.load(readHandler)
  }

  def save(writeHandler: SerializeHandler) {
    writeHandler.serializeArrayInt(posFreq)
    root.save(writeHandler)
    stateBank.save(writeHandler)
    pi.save(writeHandler)
  }

  def getPosFreq(): Array[Int] = {
    return posFreq
  }

  def getTransition(): ITransition = {
    val tran = new BigramTransition(50)
    root.descendant.foreach(node => {
      node.descendant.foreach(child => {
        if (0 < node.getKey() && 0 < child.getKey()) {
          tran.setProb(node.getKey(), child.getKey(), child.getProb())
        }
      })
    })

    return tran
  }

  def getPi(): Pi = {
    return pi
  }

  def getStateBank(): NodeRepository = {
    return stateBank
  }
}
