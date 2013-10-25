package websiteschema.mpsegment.hmm

/**
 * reference: http://www.cs.umb.edu/~srevilak/viterbi/
 */
class Viterbi {

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

    val relatedStates = e.getStateProbByObserve(o1.getIndex())
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
      ret.delta(0)(index) = Math.log(pi.getPi(s)) + Math.log(e.getProb(s, o1.getIndex()))
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

      val stateSet = e.getStateProbByObserve(oi.getIndex())
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
          val b = Math.log(e.getProb(state, oi.getIndex()))
          val Aij = Math.log(tran.getConditionProb(statePath, state))
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

  def calculateWithLog(listObserve: Seq[String]): List[Node] = {
    if (listObserve.isEmpty) return List[Node]()

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
    val path = statePath.toList.map(stateBank.get(_))
    return path
  }
}
