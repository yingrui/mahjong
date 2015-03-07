package websiteschema.mpsegment.concept

class ConceptRepository {

  private var verbConceptTree: ConceptTree = null
  private var nounConceptTree: ConceptTree = null
  private var adjConceptTree: ConceptTree = null

  nounConceptTree = ConceptLoader("websiteschema/mpsegment/noun-concepts.txt").getConceptTree()
  verbConceptTree = ConceptLoader("websiteschema/mpsegment/verb-concepts.txt").getConceptTree()
  adjConceptTree = ConceptLoader("websiteschema/mpsegment/adj-concepts.txt").getConceptTree()

  def getNounConceptTree(): ConceptTree = {
    return nounConceptTree
  }

  def setNounConceptTree(nounConceptTree: ConceptTree) {
    this.nounConceptTree = nounConceptTree
  }

  def setVerbConceptTree(verbConceptTree: ConceptTree) {
    this.verbConceptTree = verbConceptTree
  }

  def setAdjConceptTree(adjConceptTree: ConceptTree) {
    this.adjConceptTree = adjConceptTree
  }

  def getAdjConceptTree(): ConceptTree = {
    return adjConceptTree
  }

  def getVerbConceptTree(): ConceptTree = {
    return verbConceptTree
  }

  def getConceptByName(name: String): Concept = {
    if (name.startsWith("n-")) {
      return getNounConceptTree().getConceptByName(name)
    }

    if (name.startsWith("v-")) {
      return getVerbConceptTree().getConceptByName(name)
    }

    if (name.startsWith("a-")) {
      return getAdjConceptTree().getConceptByName(name)
    }
    return null
  }
}

object ConceptRepository {
  val instance = new ConceptRepository()

  def apply() = instance
}
