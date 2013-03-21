package websiteschema.mpsegment.concept

import scala.collection.mutable.Map

class ConceptTree {

  var rootConcept: Concept = null
  var mapIntConcept = Map[Long, Concept]()
  var mapStringConcept = Map[String, Concept]()
  var mapDescriptionConcept = Map[String, Concept]()

  def getRootConcept(): Concept = {
    return rootConcept
  }

  def getConceptById(id: Long): Concept = {
    mapIntConcept.get(id) match {
      case Some(c) => c
      case None => null
    }
  }

  def getConceptByName(name: String): Concept = {
    mapStringConcept.get(name) match {
      case Some(c) => c
      case None => null
    }
  }

  def getConceptByDescription(name: String): Concept = {
    mapDescriptionConcept.get(name) match {
      case Some(c) => c
      case None => null
    }
  }

  def addConcept(concept: Concept) {
    if (null == rootConcept) {
      rootConcept = concept
      buildIndex(concept)
    } else {
      addConcept(concept, rootConcept)
    }
  }

  def addConcept(concept: Concept, parent: Concept) {
    verifyUniqConcept(concept)
    buildIndex(concept)
    parent.addChild(concept)
  }

  private def verifyUniqConcept(concept: Concept) {
    var existed = getConceptById(concept.getId())
    if (null != existed) {
      throw new DuplicateConceptException(existed)
    }
    existed = getConceptByName(concept.getName())
    if (null != existed) {
      throw new DuplicateConceptException(existed)
    }
  }

  private def buildIndex(concept: Concept) {
    mapIntConcept += (concept.getId() -> concept)
    mapStringConcept += (concept.getName() -> concept)
    mapDescriptionConcept += (concept.getDescription() -> concept)
  }
}
