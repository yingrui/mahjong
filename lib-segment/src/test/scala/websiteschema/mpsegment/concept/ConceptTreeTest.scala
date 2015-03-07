/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package websiteschema.mpsegment.concept

import org.junit.Assert
import org.junit.Test

class ConceptTreeTest {

  var conceptTree: ConceptTree = null
  val nounRoot = new Concept(100000, "noun")
  val nounObject = new Concept(100001, "object")
  val nounProcess = new Concept(100002, "process")
  val nounSpace = new Concept(100003, "space")
  val nounTime = new Concept(100004, "time")

  conceptTree = new ConceptTree()
  buildTestData()

  @Test
  def should_get_concept_by_id_or_name() {
    Assert.assertEquals(nounRoot, conceptTree.getConceptById(100000))
    Assert.assertEquals(nounObject, conceptTree.getConceptById(100001))
    Assert.assertEquals(nounProcess, conceptTree.getConceptByName("process"))
    Assert.assertEquals(nounSpace, conceptTree.getConceptByName("space"))
  }

  @Test
  def should_return_error_when_add_a_concept_which_id_already_existed() {
    val nounDuplicate = new Concept(100004, "duplicate")
    try {
      conceptTree.addConcept(nounDuplicate, nounRoot)
      Assert.fail()
    } catch {
      case ex: Exception =>
      Assert.assertEquals(ex.getMessage(), "A concept 'time' has id 100004 already existed.")
    }
  }

  @Test
  def should_return_error_when_add_a_concept_which_name_already_existed() {
    val nounDuplicate = new Concept(100005, "noun")
    try {
      conceptTree.addConcept(nounDuplicate, nounRoot)
      Assert.fail()
    } catch {
      case ex: Exception =>
      Assert.assertEquals(ex.getMessage(), "A concept 'noun' has id 100000 already existed.")
    }
  }

  @Test
  def should_add_concept_to_concept_tree_correctly() {
    val nounRoot = conceptTree.getRootConcept()
    val children = conceptTree.getRootConcept().getChildren()
    Assert.assertEquals(4, children.size)
    Assert.assertEquals("object", children.last.getName())
    Assert.assertEquals(100001, children.last.getId())
    Assert.assertEquals(nounRoot, children.last.getParent())
    Assert.assertEquals(children, children.last.getSiblings())

    Assert.assertEquals("process", children(2).getName())
    Assert.assertEquals(100002, children(2).getId())
    Assert.assertEquals("space", children(1).getName())
    Assert.assertEquals(100003, children(1).getId())
    Assert.assertEquals("time", children(0).getName())
    Assert.assertEquals(100004, children(0).getId())
  }

  private def buildTestData() {
    try {
      conceptTree.addConcept(nounRoot)
      conceptTree.addConcept(nounObject, nounRoot)
      addConceptToRoot(nounProcess, nounSpace, nounTime)
    } catch {
      case ex: Exception =>
    }
  }

  private def addConceptToRoot(nounProcess: Concept, nounSpace: Concept, nounTime: Concept) {
    conceptTree.addConcept(nounProcess)
    conceptTree.addConcept(nounSpace)
    conceptTree.addConcept(nounTime)
  }

}
