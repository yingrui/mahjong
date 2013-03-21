package websiteschema.mpsegment.concept

import org.junit.Assert;
import org.junit.Test;

class ConceptTest {

  val rootNounConcept: Concept = new Concept(100000, "noun");
  buildTestData();

  @Test
  def should_have_children() {
    val children = rootNounConcept.getChildren()
    Assert.assertEquals(4, children.size); //多余
    Assert.assertEquals("object", children(0).getName());
    Assert.assertEquals(100001, children(0).getId());
    Assert.assertEquals(rootNounConcept, children(0).getParent());
    Assert.assertEquals(children, children(0).getSiblings());

    Assert.assertEquals("process", children(1).getName());
    Assert.assertEquals(100002, children(1).getId());
    Assert.assertEquals("space", children(2).getName());
    Assert.assertEquals(100003, children(2).getId());
    Assert.assertEquals("time", children(3).getName());
    Assert.assertEquals(100004, children(3).getId());
  }

  @Test
  def should_have_parent() {
    val children = rootNounConcept.getChildren()
    Assert.assertEquals(rootNounConcept, children(0).getParent());
  }

  @Test
  def should_have_siblings() {
    val children = rootNounConcept.getChildren()
    Assert.assertEquals(children, children(0).getSiblings());
  }

  private def buildTestData() {
    val nounObject = new Concept(100001, "object")
    val nounProcess = new Concept(100002, "process")
    val nounSpace = new Concept(100003, "space")
    val nounTime = new Concept(100004, "time")

    rootNounConcept.addChild(nounTime);
    rootNounConcept.addChild(nounSpace);
    rootNounConcept.addChild(nounProcess);
    rootNounConcept.addChild(nounObject);
  }

}
