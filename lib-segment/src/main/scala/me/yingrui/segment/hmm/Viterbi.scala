package me.yingrui.segment.hmm

/**
 * reference: http://www.cs.umb.edu/~srevilak/viterbi/
 */

trait Viterbi {

  val n = 2

  def getStatesBy(observe: Seq[Int]): java.util.Collection[Int]

  def calculateResult(listObserve: Seq[Array[Int]]): ViterbiResult = {
    val length = listObserve.size

    val ret = new ViterbiResult(length)
    val o1 = listObserve(0)

    val relatedStates = getStatesBy(o1)

    val relatedStatesCount = relatedStates.size
    initResultInPosition(ret, 0, relatedStatesCount)

    var index = 0
    val iterRlatedStates = relatedStates.iterator()
    while (iterRlatedStates.hasNext) {
      val s = iterRlatedStates.next()
      ret.states(0)(index) = s
      ret.delta(0)(index) = calculateFirstState(o1, s)
      ret.psai(0)(index) = 0
      index += 1
    }

    for (currentPositionIndex <- 1 until listObserve.size) {
      updateCurrentPosition(currentPositionIndex)
      val oi = listObserve(currentPositionIndex)

      val stateSet = getStatesBy(oi)
      initResultInPosition(ret, currentPositionIndex, stateSet.size)
      var stateIndex = 0
      val iterStateSet = stateSet.iterator()
      while (iterStateSet.hasNext) {
        val state = iterStateSet.next()
        ret.states(currentPositionIndex)(stateIndex) = state
        var maxDelta = Double.NegativeInfinity
        var maxProb = Double.NegativeInfinity
        var bestStateIndex = 0
        var lastStateIndex = 0
        val lastPositionIndex = currentPositionIndex - 1
        while (lastStateIndex < ret.states(lastPositionIndex).length) {
          val statePath = ret.getStatePath(lastPositionIndex, n - 1, lastStateIndex)
          val calculateResult = calculateProbability(ret.delta(lastPositionIndex)(lastStateIndex), statePath, state, oi)
          val prob = calculateResult._2
          val delta = calculateResult._1
          if (delta > maxDelta) {
            maxDelta = delta
          }

          if (prob > maxProb) {
            maxProb = prob
            bestStateIndex = lastStateIndex
          }

          lastStateIndex += 1
        }

        ret.delta(currentPositionIndex)(stateIndex) = maxDelta
        ret.psai(currentPositionIndex)(stateIndex) = bestStateIndex

        stateIndex += 1
      }
    }

    ret
  }

  private var currentPos = 0
  def currentPosition = currentPos

  private def updateCurrentPosition(currentPosition: Int) {
    this.currentPos = currentPosition
  }

  def calculateProbability(delta: Double, statePath: Array[Int], state: Int, observe: Array[Int]): (Double, Double)

  def calculateFirstState(firstObserve: Array[Int], state: Int): Double

  private def initResultInPosition(ret: ViterbiResult, position: Int, relatedStatesCount: Int) {
    ret.states(position) = new Array[Int](relatedStatesCount)
    ret.delta(position) = new Array[Double](relatedStatesCount)
    ret.psai(position) = new Array[Int](relatedStatesCount)
  }
}

class HmmViterbi extends Viterbi {

  var stateBank = new NodeRepository()
  var observeBank = new NodeRepository()
  var tran: ITransition = Transition()
  var pi = Pi()
  var e = Emission()

  def getE(): Emission = {
    return e
  }

  def setE(e: Emission) {
    this.e = e
  }

  def getObserveBank(): NodeRepository = {
    return observeBank
  }

  def setObserveBank(observeBank: NodeRepository) {
    this.observeBank = observeBank
  }

  def getPi(): Pi = {
    return pi
  }

  def setPi(pi: Pi) {
    this.pi = pi
  }

  def getStateBank(): NodeRepository = {
    return stateBank
  }

  def setStateBank(stateBank: NodeRepository) {
    this.stateBank = stateBank
  }

  def setTran(tran: ITransition) {
    this.tran = tran
  }

  def getConditionProb(statePath: Array[Int], state: Int) = tran.getConditionProb(statePath, state)

  def getProb(state: Int, observe: Int) = e.getProb(state, observe)

  def getPi(state: Int) = pi.getPi(state)

  override def getStatesBy(observe: Seq[Int]): java.util.Collection[Int] = {
    val states = e.getStatesBy(observe(0))
    if (null == states || states.isEmpty) {
      throw new ObserveListException("UNKNOWN observe object " + observe + ".")
    }
    states
  }

  def calculateWithLog(listObserve: Seq[String]): Seq[Node] = {
    if (listObserve.isEmpty) {
      List[Node]()
    } else {
      val ret = calculateResult(getObservedFeatures(listObserve))

      ret.getBestPath.map(getStateBank.get(_))
    }
  }

  private def getObservedFeatures(listObserve: Seq[String]) = listObserve.map(o => Array(getObserveIndex(o)))

  override def calculateProbability(delta: Double, statePath: Array[Int], state: Int, observe: Array[Int]) = {
    // A * delta * b
    val b = Math.log(getProb(state, observe(0)))
    val Aij = Math.log(getConditionProb(statePath, state))
    val prob = delta + Aij
    (prob + b, prob)
  }

  override def calculateFirstState(firstObserve: Array[Int], state: Int): Double = Math.log(getPi(state)) + Math.log(getProb(state, firstObserve(0)))

  private def getObserveIndex(observe: String): Int = {
    val node = observeBank.get(observe)
    if (node != null) {
      node.getIndex()
    } else {
      val newNode = Node(observe)
      observeBank.add(newNode)
      newNode.getIndex()
    }
  }

}
