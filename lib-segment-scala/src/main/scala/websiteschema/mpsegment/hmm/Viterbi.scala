package websiteschema.mpsegment.hmm


/**
 * reference: http://www.cs.umb.edu/~srevilak/viterbi/
 */

trait Viterbi {

  def getConditionProb(statePath: Array[Int], state: Int): Double

  def getProb(state: Int, observe: Node): Double

  def getPi(state: Int): Double

  def getStateProbBy(observe: Node): java.util.Collection[Int]

  def calculateWithLog(listObserve: Seq[String]): Seq[Node]

  def setObserveBank(observeBank: NodeRepository)
  def setStateBank(stateBank: NodeRepository)
  def setTran(tran: ITransition)
  def setE(e: Emission)
  def setPi(pi: Pi)
}

class ViterbiImpl extends Viterbi {

  var stateBank = new NodeRepository()
  var observeBank = new NodeRepository()
  var tran: ITransition = Transition()
  var pi = Pi()
  var e = new Emission()
  var n = 2

  def setN(n: Int) {
    this.n = n
  }

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

  def getStatePath(states: Array[Array[Int]], psai: Array[Array[Int]], end: Int, depth: Int, position: Int): Array[Int] = {
    val maxDepth = if (end + 1 > depth) depth else end + 1
    val ret = new Array[Int](maxDepth)
    var pos = position
    for (i <- 0 until maxDepth) {
      val state = states(end - i)(pos)
      pos = psai(end - i)(pos)
      ret(ret.length - i - 1) = state
    }
    ret
  }

  private def calculateHmmResult(listObserve: Seq[String]): HmmResult = {
    val ret = new HmmResult()

    if (listObserve.isEmpty) {
      throw new ObserveListException("observe list is empty.")
    }

    var o = listObserve(0)
    var o1 = observeBank.get(o)
    if (o1 == null) {
      o1 = Node(o)
      observeBank.add(o1)
    }

    val relatedStates = getStateProbBy(o1)
    if (null == relatedStates || relatedStates.isEmpty) {
      throw new ObserveListException("UNKNOWN observe object " + o + ".")
    }
    ret.states = new Array[Array[Int]](listObserve.size)
    ret.delta = new Array[Array[Double]](listObserve.size)
    ret.psai = new Array[Array[Int]](listObserve.size)
    ret.states(0) = new Array[Int](relatedStates.size)
    ret.delta(0) = new Array[Double](relatedStates.size)
    ret.psai(0) = new Array[Int](relatedStates.size)

    var index = 0
    val iterRlatedStates = relatedStates.iterator()
    while (iterRlatedStates.hasNext) {
      val s = iterRlatedStates.next()
      ret.states(0)(index) = s
      ret.delta(0)(index) = Math.log(getPi(s)) + Math.log(getProb(s, o1))
      ret.psai(0)(index) = 0
      index += 1
    }

    //
    for (p <- 1 until listObserve.size) {
      o = listObserve(p)
      var oi = observeBank.get(o)
      if (oi == null) {
        oi = Node(o)
        observeBank.add(oi)
      }

      val stateSet = getStateProbBy(oi)
      if (stateSet.isEmpty) {
        throw new ObserveListException("UNKNOWN observe object " + o + ".")
      }
      ret.states(p) = new Array[Int](stateSet.size)
      ret.delta(p) = new Array[Double](stateSet.size)
      ret.psai(p) = new Array[Int](stateSet.size)
      var i = 0
      val iterStateSet = stateSet.iterator()
      while (iterStateSet.hasNext) {
        val state = iterStateSet.next()
        ret.states(p)(i) = state
        var maxDelta = Double.NegativeInfinity
        var maxPsai = Double.NegativeInfinity
        var ls = 0
        var j = 0
        while (j < ret.states(p - 1).length) {
          val statePath = getStatePath(ret.states, ret.psai, p - 1, n - 1, j)
          val b = Math.log(getProb(state, oi))
          val Aij = Math.log(getConditionProb(statePath, state))
          val psai_j = ret.delta(p - 1)(j) + Aij
          val delta_j = psai_j + b
          if (delta_j > maxDelta) {
            maxDelta = delta_j
          }

          if (psai_j > maxPsai) {
            maxPsai = psai_j
            ls = j
          }
          j += 1
        }

        ret.delta(p)(i) = maxDelta
        ret.psai(p)(i) = ls

        i += 1
      }
    }

    return ret
  }


  override def getConditionProb(statePath: Array[Int], state: Int) = tran.getConditionProb(statePath, state)

  override def getProb(state: Int, observe: Node) = e.getProb(state, observe.getIndex())

  override def getPi(state: Int) = pi.getPi(state)

  override def getStateProbBy(observe: Node) = e.getStateProbByObserve(observe.getIndex())

  override def calculateWithLog(listObserve: Seq[String]): Seq[Node] = {
    if (listObserve.isEmpty) {
      List[Node]()
    } else {
      val ret = calculateHmmResult(listObserve)
      var maxProb = Double.NegativeInfinity
      var pos = 0
      for (j <- 0 until ret.delta(listObserve.size - 1).length) {
        val p = ret.delta(listObserve.size - 1)(j)
        if (p > maxProb) {
          maxProb = p
          pos = j
        }
      }

      val statePath = getStatePath(ret.states, ret.psai, listObserve.size - 1, listObserve.size, pos)
      statePath.map(stateBank.get(_))
    }
  }
}
