/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.concept;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ConceptTest {

    Concept rootNounConcept;

    public ConceptTest() {
        rootNounConcept = new Concept(100000, "noun");
        buildTestData();
    }

    @Test
    public void should_concept_have_children() {
        List<Concept> children = rootNounConcept.getChildren();
        Assert.assertEquals(4, children.size());
        Assert.assertEquals("object", children.get(0).getName());
        Assert.assertEquals(100001, children.get(0).getId());
        Assert.assertEquals(rootNounConcept, children.get(0).getParent());
        Assert.assertEquals(children, children.get(0).getSiblings());

        Assert.assertEquals("process", children.get(1).getName());
        Assert.assertEquals(100002, children.get(1).getId());
        Assert.assertEquals("space", children.get(2).getName());
        Assert.assertEquals(100003, children.get(2).getId());
        Assert.assertEquals("time", children.get(3).getName());
        Assert.assertEquals(100004, children.get(3).getId());
    }

    @Test
    public void should_concept_have_parent() {
        List<Concept> children = rootNounConcept.getChildren();
        Assert.assertEquals(rootNounConcept, children.get(0).getParent());
    }

    @Test
    public void should_concept_have_siblings() {
        List<Concept> children = rootNounConcept.getChildren();
        Assert.assertEquals(children, children.get(0).getSiblings());
    }

    private void buildTestData() {
        Concept nounObject = new Concept(100001, "object");
        Concept nounProcess = new Concept(100002, "process");
        Concept nounSpace = new Concept(100003, "space");
        Concept nounTime = new Concept(100004, "time");

        rootNounConcept.addChild(nounObject);
        rootNounConcept.addChild(nounProcess);
        rootNounConcept.addChild(nounSpace);
        rootNounConcept.addChild(nounTime);
    }

}
